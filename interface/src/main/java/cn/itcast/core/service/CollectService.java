package cn.itcast.core.service;

import cn.itcast.core.pojo.item.Item;

import java.util.List;

public interface CollectService {
    //根据登录用户名,查询收藏
    public List<Item> findAll(String userName);
}
