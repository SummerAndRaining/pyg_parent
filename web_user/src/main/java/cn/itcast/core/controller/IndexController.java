package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.BuyerCart;
import cn.itcast.core.pojo.order.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("login")
public class IndexController {

    //登录用户回显
    @RequestMapping("name")
    public Map<String,String> backName(){
        //1.获取登录用户
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        //2如果登录成功,回显当前登录用户名
        Map<String ,String> map = new HashMap<>();
        map.put("loginName",userName);
        return map;

    }



}
