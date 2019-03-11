package cn.itcast.core.controller;


import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.CountOrdersService;
import cn.itcast.core.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("countOrders")
public class CountOrdersController {

    @Reference
    private CountOrdersService countOrdersService;

    /*
        @RequestMapping("findAll")     //查询所有用户信息
        public List<User> findAll() {

            List<User> userList = countOrdersService.findAll();
            return userList;
        }*/


    @RequestMapping("search")  //分页查询所有用户数据
    public PageResult search(Integer page, Integer rows, @RequestBody User user) {

            PageResult pageResult = countOrdersService.search(page, rows, user);
            return pageResult;
    }

    @RequestMapping("countOrder")  //查询订单数据库,返回每个用户的订单个数的集合
    public List<Map> countOrder(){
       return countOrdersService.countOrder();
    }
}
