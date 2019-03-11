package cn.itcast.core.service;

import cn.itcast.core.common.Constants;
import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.dao.specification.SpecificationDao;
import cn.itcast.core.dao.specification.SpecificationOptionDao;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.SpecEntity;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.pojo.specification.SpecificationOption;
import cn.itcast.core.pojo.specification.SpecificationOptionQuery;
import cn.itcast.core.pojo.specification.SpecificationQuery;
import cn.itcast.core.pojo.template.TypeTemplate;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class SpecServiceImpl implements SpecService {

    @Autowired
    private SpecificationDao specDao;

    @Autowired
    private SpecificationOptionDao optionDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SellerDao sellerDao;

    @Override
    public PageResult findPage(Integer page, Integer rows, Specification spec) {
        //创建查询对象
        SpecificationQuery query = new SpecificationQuery();
        //创建条件对象
        SpecificationQuery.Criteria criteria = query.createCriteria();
        if (spec != null) {
            if (spec.getSpecName() != null && !"".equals(spec.getSpecName())) {
                criteria.andSpecNameLike("%" + spec.getSpecName() + "%");
            }
        }

        PageHelper.startPage(page, rows);
        Page<Specification> specList = (Page<Specification>) specDao.selectByExample(query);
        return new PageResult(specList.getTotal(), specList.getResult());
    }

    @Override
    public void add(SpecEntity specEntity) {
        //1. 保存规格对象
        specDao.insertSelective(specEntity.getSpecification());
        //2. 遍历规格选项集合
        if (specEntity.getSpecificationOptionList() != null) {
            for (SpecificationOption option : specEntity.getSpecificationOptionList()) {
                //设置规格选项对象中的规格对象的主键id
                option.setSpecId(specEntity.getSpecification().getId());
                //3. 遍历过程中保存规格选项对象
                optionDao.insertSelective(option);
            }
        }


    }

    @Override
    public SpecEntity findOne(Long id) {
        //1. 根据规格id查询规格表数据
        Specification specification = specDao.selectByPrimaryKey(id);

        //2. 根据规格id查询规格选项表数据
        SpecificationOptionQuery query = new SpecificationOptionQuery();
        SpecificationOptionQuery.Criteria criteria = query.createCriteria();
        criteria.andSpecIdEqualTo(id);
        List<SpecificationOption> optionList = optionDao.selectByExample(query);

        //3. 将规格数据和规格选项数据封装到实体类中返回
        SpecEntity entity = new SpecEntity();
        entity.setSpecification(specification);
        entity.setSpecificationOptionList(optionList);
        return entity;
    }

    @Override
    public void update(SpecEntity specEntity) {
        //1. 根据规格实体更新保存
        specDao.updateByPrimaryKeySelective(specEntity.getSpecification());

        //2. 根据规格id删除规格选项对应的数据
        SpecificationOptionQuery query = new SpecificationOptionQuery();
        SpecificationOptionQuery.Criteria criteria = query.createCriteria();
        criteria.andSpecIdEqualTo(specEntity.getSpecification().getId());
        optionDao.deleteByExample(query);

        //3. 遍历页面传入的新的规格选项集合数据
        if (specEntity.getSpecificationOptionList() != null) {
            for (SpecificationOption option : specEntity.getSpecificationOptionList()) {
                //设置规格选项的外键
                option.setSpecId(specEntity.getSpecification().getId());
                //4. 保存新的规格选项对象
                optionDao.insertSelective(option);
            }
        }

    }

    @Override
    public void delete(Long[] ids) {
        if (ids != null) {
            for (Long id : ids) {
                //1.根据规格id删除规格表数据
                specDao.deleteByPrimaryKey(id);
                //2. 根据规格id删除规格选项表数据
                SpecificationOptionQuery query = new SpecificationOptionQuery();
                SpecificationOptionQuery.Criteria criteria = query.createCriteria();
                criteria.andSpecIdEqualTo(id);
                optionDao.deleteByExample(query);
            }
        }

    }

    @Override
    public List<Map> selectOptionList() {
        return specDao.selectOptionList();
    }

    @Override
    public void importExcel(String[][] strings) {
        for(int i=0;i<strings.length;i++){
            Specification specification = new Specification();
            specification.setSpecName(strings[i][1]);
            specDao.insertSelective(specification);
            //userService.save(user, null);//这是一个添加方法，dao层写入sql语句即可
        }
    }

    //从redis中获取当前用户所有未审核通过的规格数据
    @Override
    public List<SpecEntity> getSepcListFromRedis(String userName) {
        List<SpecEntity> specEntityList = (List<SpecEntity>) redisTemplate.boundHashOps(Constants.APPLY_SPEC_LIST).get(userName);
        if (specEntityList == null) {
            specEntityList = new ArrayList<SpecEntity>();
        }
        return specEntityList;
    }

    //根据用户名将规格集合存入到redis中
    @Override
    public void putSpecListByUserName(String userName, List<SpecEntity> specEntityList) {
        redisTemplate.boundHashOps(Constants.APPLY_SPEC_LIST).put(userName, specEntityList);
    }

    //规格申请
    @Override
    public void addSpecFromShop(String userName, SpecEntity specEntity) {
        //1.从redis中获取未审核的规格集合
        List<SpecEntity> specEntityList = getSepcListFromRedis(userName);
        //2.根据当前登录用户名申请规格
        specEntityList.add(specEntity);
        putSpecListByUserName(userName,specEntityList);
    }

    //从redis中查询所有商家的规格数据
    @Override
    public List<SpecEntity> findAllShopSpecFromRedis() {
        //根据商家名从redis中获取所有未审核的规格集合数据
        Map<String, List<SpecEntity>> allApplySpecMap = redisTemplate.boundHashOps(Constants.APPLY_SPEC_LIST).entries();
        Set<Map.Entry<String, List<SpecEntity>>> entries = allApplySpecMap.entrySet();
        List<SpecEntity> allApplySpecEntityList = new ArrayList<>();

        if (entries != null && entries.size() > 0) {
            for (Map.Entry<String, List<SpecEntity>> entry : entries) {
                String userName = entry.getKey();
                List<SpecEntity> specEntityList = entry.getValue();
                if (specEntityList != null && specEntityList.size() > 0) {
                    for (SpecEntity specEntity : specEntityList) {
                        allApplySpecEntityList.add(specEntity);
                    }
                }
            }
        }
        return allApplySpecEntityList;
    }

    //审核商家规格
    @Override
    public void updateStatusSpecification(String[] names) {
        if (names != null) {
            //从redis中获取所有商家所拥有未审核的规格集合数据
            List<Seller> allSellerList = sellerDao.selectByExample(null);
            if (allSellerList != null && allSellerList.size() > 0) {
                for (Seller seller : allSellerList) {
                    List<SpecEntity> specEntityList = getSepcListFromRedis(seller.getSellerId());
                    //新建集合存储已审核通过的规格实体数据
                    List<SpecEntity> arrayList = new ArrayList<>();
                    if (specEntityList != null && specEntityList.size() > 0) {
                        for (SpecEntity specEntity : specEntityList) {
                            Specification specification = specEntity.getSpecification();
                            if (specification.getSpecName() != null) {
                                for (String name : names) {
                                    if (name.equals(specification.getSpecName())) {
                                        //将对应商家已审核的规格数据添加到数据库中
                                        specDao.insertSelective(specification);
                                        Long id = specification.getId();
                                        //2. 遍历规格选项集合
                                        if (specEntity.getSpecificationOptionList() != null) {
                                            for (SpecificationOption option : specEntity.getSpecificationOptionList()) {
                                                //设置规格选项对象中的规格对象的主键id
                                                option.setSpecId(specification.getId());
                                                //3. 遍历过程中保存规格选项对象
                                                optionDao.insertSelective(option);
                                            }
                                        }
                                        arrayList.add(specEntity);
                                    }
                                }
                            }
                        }
                        //将已审核通过的规格数据从redis中删除
                        for (SpecEntity specEntity : arrayList) {
                            specEntityList.remove(specEntity);
                        }
                        if (specEntityList != null && specEntityList.size() > 0) {
                            redisTemplate.boundHashOps(Constants.APPLY_SPEC_LIST).put(seller.getSellerId(), specEntityList);
                        } else {
                            redisTemplate.boundHashOps(Constants.APPLY_SPEC_LIST).delete(seller.getSellerId());
                        }
                    }
                }
            }
        }
    }
}
