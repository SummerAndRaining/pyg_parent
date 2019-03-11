package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.SpecEntity;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.pojo.specification.Specification;

import java.util.List;
import java.util.Map;

public interface SpecService {

    public PageResult findPage(Integer page, Integer rows, Specification spec);

    public void add(SpecEntity specEntity);

    public SpecEntity findOne(Long id);

    public void update(SpecEntity specEntity);

    public void delete(Long[] ids);

    public List<Map> selectOptionList();

    public void importExcel(String[][] strings);

    //从redis中获取当前用户所有未审核通过的规格数据
    List<SpecEntity> getSepcListFromRedis(String userName);

    //根据用户名将品牌集合存入到redis中
    void putSpecListByUserName(String userName, List<SpecEntity> specEntityList);

    //规格申请
    void addSpecFromShop(String userName, SpecEntity specEntity);

    //从redis中查询所有商家未审核通过的规格数据
    List<SpecEntity> findAllShopSpecFromRedis();

    //审核商家规格
    public void updateStatusSpecification(String[] names);

}
