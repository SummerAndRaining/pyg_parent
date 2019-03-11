package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.service.SeckillOrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("seckillOrder")
public class SeckillOrdersController {

    @Reference
    private SeckillOrderService seckillOrderService;


    //订单回显
  /* @RequestMapping("findOne")
    public SeckillOrder findOne(){
      return seckillOrderService.findOne(id);

   }*/
    @RequestMapping("/search")    //秒杀订单的分页查询和数据展示
    public PageResult search(Integer page, Integer rows, @RequestBody SeckillOrder seckillOrder) {
        //获取到域中的卖家姓名
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        //将卖家姓名添加到秒杀订单对象中
        seckillOrder.setSellerId(userName);
        //进行分页查询和数据的展示
        PageResult pageResult = seckillOrderService.search(page, rows, seckillOrder);
        return pageResult;

    }

    //查询所有
    @RequestMapping("/findAll")
    public List<SeckillOrder> findAll() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<SeckillOrder> seckillOrderList = seckillOrderService.findAll(username);
        return seckillOrderList;
    }


}
