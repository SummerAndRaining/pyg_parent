package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.pojo.template.TypeTemplate;

import java.util.List;
import java.util.Map;

public interface TemplateService {

    public PageResult findPage(Integer page, Integer rows, TypeTemplate template);

    public void add(TypeTemplate template);

    public TypeTemplate findOne(Long id);

    public void update(TypeTemplate template);

    public void delete(Long[] ids);

    public List<Map> findBySpecList(Long id);

    public void importExcel(String[][]strings);

    //从Redis中获取当前用户的模板数据
    public List<TypeTemplate> shopFindFromRedis(String userName);

    //根据用户名将品牌集合存入到redis中
    public void putTemplateListByUserName(String userName, List<TypeTemplate> typeTemplateList);

    //将数据添加到redis中
    public void addTemplateFromShop(TypeTemplate typeTemplate, String userName);

    //从redis中查询所有的模板数据
    public List<TypeTemplate> findAll();

    //审核模板
    public void updateStatus(String[] names);
}
