package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.BrandEntity;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.good.Brand;

import java.util.List;
import java.util.Map;

public interface BrandService {

    public List<Brand> findAll();

    public PageResult findPage(Brand brand, Integer page, Integer rows);

    public void add(Brand brand);

    public Brand findOne(Long id);

    public void update(Brand brand);

    public void delete(Long[] ids);

    public List<Map> selectOptionList();

    public  void importExcel(String[][] strings);

    //从redis中获取当前用户所有未审核通过的规格数据
    List<Brand> getBrandListFromRedis(String userName);

    //根据用户名将品牌集合存入到redis中
    void putBrandListByUserName(String userName,List<Brand> brandList);

    //商家品牌申请
    public void addShopBrandToRedis(String userName, Brand brand);

    //显示未审核品牌列表
    public List<BrandEntity> findAllApplyBrand();

    //商家品牌审核
    public void updateBrand(BrandEntity[] brandEntityArray);

}
