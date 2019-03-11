package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.user.User;

import java.util.List;
import java.util.Map;

public interface CountOrdersService {

    public List<User> findAll ();

    public PageResult search (Integer page,Integer rows,User user);

    public List <Map> countOrder();
}
