package cn.itcast.core.service;

import cn.itcast.core.common.Constants;
import cn.itcast.core.dao.good.BrandDao;
import cn.itcast.core.pojo.entity.BrandEntity;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.good.BrandQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Brand> findAll() {
        return brandDao.selectByExample(null);
    }

    @Override
    public PageResult findPage(Brand brand, Integer page, Integer rows) {
        //创建查询对象
        BrandQuery query = new BrandQuery();
        //组装条件
        if (brand != null) {
            //创建sql语句中的where条件对象
            BrandQuery.Criteria criteria = query.createCriteria();
            if (brand.getFirstChar() != null && !"".equals(brand.getFirstChar())) {
                criteria.andFirstCharEqualTo(brand.getFirstChar());
            }
            if (brand.getName() != null && !"".equals(brand.getName())) {
                criteria.andNameLike("%" + brand.getName() + "%");
            }
        }
        PageHelper.startPage(page, rows);
        //查询
        Page<Brand> brandList = (Page<Brand>) brandDao.selectByExample(query);
        return new PageResult(brandList.getTotal(), brandList.getResult());
    }

    @Override
    public void add(Brand brand) {
        //添加的时候, 传入参数brand对象中的所有属性必须有值, 都参与添加
        //brandDao.insert();
        //添加的时候会判断brand传入参数对象中的属性是否有值, 如果没有值不参与添加, 如果有值再拼接到sql语句中参与添加
        brandDao.insertSelective(brand);
    }

    @Override
    public Brand findOne(Long id) {
        return brandDao.selectByPrimaryKey(id);
    }

    /**
     * update tb_brand set xxx=xxx where name=xxx
     *
     * @param brand
     */
    @Override
    public void update(Brand brand) {
        //根据查询条件更新, 这个条件不包括主键条件, 判断传入的参数brand中是否有为null的属性, 如果为null的属性不参与更新
        //brandDao.updateByExampleSelective(, )
        //根据查询条件更新, 这个条件不包括主键条件
        //brandDao.updateByExample(, )
        //根据主键作为条件更新
        //brandDao.updateByPrimaryKey();
        //根据主键作为条件更新, 判断传入的参数brand品牌对象中的属性如果为null不参与更新, 如果不为null才会被拼接到sql语句中执行
        brandDao.updateByPrimaryKeySelective(brand);

    }

    @Override
    public void delete(Long[] ids) {
        if (ids != null) {
            for (Long id : ids) {
                brandDao.deleteByPrimaryKey(id);
            }
        }
    }

    @Override
    public List<Map> selectOptionList() {
        List<Map> list = brandDao.selectOptionList();
        return list;
    }

    @Override
    public void importExcel(String[][] strings) {
        for(int i=0;i<strings.length;i++){
            Brand brand = new Brand();
            brand.setName(strings[i][1]);
            brand.setFirstChar(strings[i][2]);
            brandDao.insertSelective(brand);
            //userService.save(user, null);//这是一个添加方法，dao层写入sql语句即可
        }
    }

    //从redis中获取当前用户所有未审核通过的品牌数据
    @Override
    public List<Brand> getBrandListFromRedis(String userName) {
        List<Brand> brandList = (List<Brand>) redisTemplate.boundHashOps(Constants.APPLY_BRAND_LIST).get(userName);
        if (brandList == null) {
            brandList = new ArrayList<Brand>();
        }
        return brandList;
    }

    //根据用户名将品牌集合存入到redis中
    @Override
    public void putBrandListByUserName(String userName, List<Brand> brandList) {
        redisTemplate.boundHashOps(Constants.APPLY_BRAND_LIST).put(userName, brandList);
    }

    //商家品牌申请
    @Override
    public void addShopBrandToRedis(String userName, Brand brand) {
        //1.从redis中获取未审核的品牌集合
        List<Brand> brandList = getBrandListFromRedis(userName);
        brandList.add(brand);
        //2.根据当前登录用户名申请规格
        putBrandListByUserName(userName, brandList);
    }

    //显示未审核品牌列表
    @Override
    public List<BrandEntity> findAllApplyBrand() {
        //List<BrandEntity> brandEntities = (List<BrandEntity>) redisTemplate.boundListOps(Constants.APPLY_BRAND_LIST).range(0, 1000);
        Map<String, List<Brand>> brandEntries = redisTemplate.boundHashOps(Constants.APPLY_BRAND_LIST).entries();
        Set<Map.Entry<String, List<Brand>>> entries = brandEntries.entrySet();
        List<BrandEntity> allApplyBrandEntityList = new ArrayList<>();

        if (entries != null && entries.size() > 0) {
            for (Map.Entry<String, List<Brand>> entry : entries) {
                String userName = entry.getKey();
                List<Brand> brandList = entry.getValue();
                if (brandList != null && brandList.size() > 0) {
                    for (Brand brand : brandList) {
                        BrandEntity brandEntity = new BrandEntity();
                        brandEntity.setUserName(userName);
                        brandEntity.setBrandName(brand.getName());
                        brandEntity.setFirstChar(brand.getFirstChar());
                        allApplyBrandEntityList.add(brandEntity);
                    }
                }
            }
        }
        return allApplyBrandEntityList;
    }

    //商家品牌审核
    @Override
    public void updateBrand(BrandEntity[] brandEntityArray) {
        //1.从redis中获取所有商家未审核的品牌集合
        List<BrandEntity> allBrandEntityList = findAllApplyBrand();
        //2.将审核通过的品牌数据添加到数据库中
        if (brandEntityArray != null && brandEntityArray.length > 0) {
            for (BrandEntity brandEntity : brandEntityArray) {
                if (allBrandEntityList != null && allBrandEntityList.size() > 0) {
                    //定义一个集合存储未审核品牌实体类
                    List<BrandEntity> list = new ArrayList<>();
                    for (BrandEntity redisEntity : allBrandEntityList) {
                        //TODO  已经确定审核通过的品牌数据
                        if (brandEntity.getUserName().equals(redisEntity.getUserName()) && brandEntity.getBrandName().equals(redisEntity.getBrandName())) {
                            //2.将审核通过的品牌数据添加到数据库中
                            Brand brand = new Brand();
                            brand.setName(redisEntity.getBrandName());
                            brand.setFirstChar(redisEntity.getFirstChar());
                            brandDao.insertSelective(brand);
                        } else {
                            list.add(redisEntity);
                        }
                        //3.删除redis中已审核通过的品牌数据
                        redisTemplate.boundHashOps(Constants.APPLY_TEMPLATE_LIST).put(redisEntity.getUserName(), list);
                    }
                }
            }
        }
    }
}
