package cn.itcast.core.service;

import cn.itcast.core.pojo.item.ItemCat;

import java.util.List;

public interface ItemCatService {

    public List<ItemCat> findByParentId(Long parentId);

    public ItemCat findOne(Long id);

    public List<ItemCat> findAll();

    public void importExcel(String[][]strings);

    //从redis中获取当前用户所有未审核通过的规格数据
    List<ItemCat> getItemCatFromRedis(String userName);

    //根据用户名将品牌集合存入到redis中
    void putItemCatListByUserName(String userName, List<ItemCat> itemCatList);

    //商家分类申请
    void addItemToRedis(String userName, ItemCat itemCat);

    //从redis中查询所有商家未审核通过的分类数据
    List<ItemCat> findAllApplyItemCatList();
}
