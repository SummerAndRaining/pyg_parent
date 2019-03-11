package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.service.SeckillOrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("seckillOrder")
public class SeckillOrderController {

    @Reference
    private SeckillOrderService seckillOrderService;

    @RequestMapping("search")    //分页查询,搜索和查询所有
    public PageResult search(Integer page, Integer rows, @RequestBody SeckillOrder seckillOrder){
        //1.获取当前域中的用户名
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        //2.将用户名添加到秒杀订单对象中
        seckillOrder.setSellerId(userName);
        //3.执行查询
        PageResult pageResult = seckillOrderService.search(page, rows, seckillOrder);
        return pageResult;


    }

}
