package cn.itcast.core.service;

import cn.itcast.core.common.Constants;
import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.dao.specification.SpecificationOptionDao;
import cn.itcast.core.dao.template.TypeTemplateDao;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.pojo.specification.SpecificationOption;
import cn.itcast.core.pojo.specification.SpecificationOptionQuery;
import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.pojo.template.TypeTemplateQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
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
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TypeTemplateDao templateDao;

    @Autowired
    private SpecificationOptionDao optionDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SellerDao sellerDao;

    @Override
    public PageResult findPage(Integer page, Integer rows, TypeTemplate template) {
        /**
         * 缓存品牌集合和规格集合数据到redis中供portal系统搜索使用
         */
        //查询所有模板数据
        List<TypeTemplate> templates = templateDao.selectByExample(null);
        if (templates != null) {
            for (TypeTemplate typeTemplate : templates) {
                //获取品牌json字符串
                String brandJsonStr = typeTemplate.getBrandIds();
                List<Map> maps = JSON.parseArray(brandJsonStr, Map.class);
                //缓存品牌集合到redis中
                redisTemplate.boundHashOps(Constants.REDIS_BRADND_LIST).put(typeTemplate.getId(), maps);

                //根据模板id获取规格和规格选项集合数据
                List<Map> speList = findBySpecList(typeTemplate.getId());
                redisTemplate.boundHashOps(Constants.REDIS_SPEC_LIST).put(typeTemplate.getId(), speList);
            }
        }

        /**
         * 分页查询
         */
        TypeTemplateQuery query = new TypeTemplateQuery();
        TypeTemplateQuery.Criteria criteria = query.createCriteria();
        if (template != null) {
            if (template.getName() != null && !"".equals(template.getName())) {
                criteria.andNameLike("%" + template.getName() + "%");
            }
        }

        PageHelper.startPage(page, rows);
        Page<TypeTemplate> templateList = (Page<TypeTemplate>) templateDao.selectByExample(query);
        return new PageResult(templateList.getTotal(), templateList.getResult());
    }

    @Override
    public void add(TypeTemplate template) {
        templateDao.insertSelective(template);
    }

    @Override
    public TypeTemplate findOne(Long id) {
        return templateDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(TypeTemplate template) {
        templateDao.updateByPrimaryKeySelective(template);
    }

    @Override
    public void delete(Long[] ids) {
        if (ids != null) {
            for (Long id : ids) {
                templateDao.deleteByPrimaryKey(id);
            }
        }
    }

    @Override
    public void importExcel(String[][] strings) {
        for(int i=0;i<strings.length;i++){
            TypeTemplate template = new TypeTemplate();
//            template.setId(Long.valueOf(strings[i][0]));
            template.setName(strings[i][1]);
            template.setSpecIds(strings[i][2]);
            template.setBrandIds(strings[i][3]);
            template.setCustomAttributeItems(strings[i][4]);
//            System.out.println(template);
            templateDao.insertSelective(template);

            //userService.save(user, null);//这是一个添加方法，dao层写入sql语句即可
        }


    }

    @Override
    public List<Map> findBySpecList(Long id) {
        //1. 根据模板id查询模板对象
        TypeTemplate typeTemplate = templateDao.selectByPrimaryKey(id);
        //2. 从模板对象中获取规格集合数据
        String specJsonStr = typeTemplate.getSpecIds();
        //3. 解析规格集合数据
        List<Map> specList = JSON.parseArray(specJsonStr, Map.class);

        //4. 遍历解析后的规格集合数据
        if (specList != null) {
            for (Map map : specList) {
                //5. 在遍历过程中, 根据规格id查询规格选项集合数据
                Long specId = Long.parseLong(String.valueOf(map.get("id")));
                SpecificationOptionQuery query = new SpecificationOptionQuery();
                SpecificationOptionQuery.Criteria criteria = query.createCriteria();
                criteria.andSpecIdEqualTo(specId);
                List<SpecificationOption> optionList = optionDao.selectByExample(query);
                //6. 将规格选项集合数据封装到规格集合中
                map.put("options", optionList);

            }
        }
        return specList;
    }

    //从redis中获取当前用户所有未审核通过的模板数据
    @Override
    public List<TypeTemplate> shopFindFromRedis(String userName) {
        List<TypeTemplate> typeTemplateList = (List<TypeTemplate>) redisTemplate.boundHashOps(Constants.APPLY_TEMPLATE_LIST).get(userName);
        if (typeTemplateList == null) {
            typeTemplateList = new ArrayList<TypeTemplate>();
        }
        return typeTemplateList;
    }

    //根据用户名将模板集合存入到redis中
    @Override
    public void putTemplateListByUserName(String userName, List<TypeTemplate> typeTemplateList) {
        redisTemplate.boundHashOps(Constants.APPLY_TEMPLATE_LIST).put(userName,typeTemplateList);
    }

    //模板申请
    @Override
    public void addTemplateFromShop(TypeTemplate typeTemplate, String userName) {
        List<TypeTemplate> templateList = (List<TypeTemplate>) redisTemplate.boundHashOps(Constants.APPLY_TEMPLATE_LIST).get(userName);
        if (templateList != null) {
            templateList.add(typeTemplate);
        } else {
            templateList = new ArrayList<>();
            templateList.add(typeTemplate);
        }
        redisTemplate.boundHashOps(Constants.APPLY_TEMPLATE_LIST).put(userName, templateList);
    }

    //从redis中查询所有商家的模板数据
    @Override
    public List<TypeTemplate> findAll() {
        //定义一个集合,用于存储所有商家的所有模板集合
        Map<String, List<TypeTemplate>> allApplyTypeMap = redisTemplate.boundHashOps(Constants.APPLY_TEMPLATE_LIST).entries();
        Set<Map.Entry<String, List<TypeTemplate>>> entries = allApplyTypeMap.entrySet();
        List<TypeTemplate> allApplyTemplateEntityList = new ArrayList<>();

        if (entries != null && entries.size() > 0) {
            for (Map.Entry<String, List<TypeTemplate>> entry : entries) {
                String userName = entry.getKey();
                List<TypeTemplate> typeTemplates = entry.getValue();
                if (typeTemplates != null && typeTemplates.size() > 0) {
                    for (TypeTemplate typeTemplate : typeTemplates) {
                        allApplyTemplateEntityList.add(typeTemplate);
                    }
                }
            }
        }
        return allApplyTemplateEntityList;
    }

    //审核商家模板
    @Override
    public void updateStatus(String[] names) {
        //1从redis中获取所有的用户及对应要申请的muban

        if (names != null) {

            //获取所有的用户名
            List<Seller> sellers = sellerDao.selectByExample(null);
            if (sellers != null) {
                //遍历用户名
                for (Seller seller : sellers) {
                    //获取每个用户在redis的模板集合
                    List<TypeTemplate> typeTemplateList = (List<TypeTemplate>) redisTemplate.boundHashOps(Constants.APPLY_TEMPLATE_LIST).get(seller.getSellerId());
                    List<TypeTemplate> arrayList = new ArrayList<>();
                    //遍历模板集合
                    if (typeTemplateList != null && typeTemplateList.size() > 0) {
                        for (TypeTemplate typeTemplate : typeTemplateList) {
                            for (String name : names) {
                                if (typeTemplate.getName().equals(name)) {
                                    templateDao.insertSelective(typeTemplate);

                                    arrayList.add(typeTemplate);
                                }
//                            typeTemplateList.remove(typeTemplate);
                            }
                        }
                        for (TypeTemplate typeTemplate : arrayList) {
                            typeTemplateList.remove(typeTemplate);
                        }
                        if (typeTemplateList.size() > 0) {
                            redisTemplate.boundHashOps(Constants.APPLY_TEMPLATE_LIST).put(seller.getSellerId(), typeTemplateList);
                        } else {
                            redisTemplate.boundHashOps(Constants.APPLY_TEMPLATE_LIST).delete(seller.getSellerId());
                        }
                    }
                }
            }
        }
    }

}
