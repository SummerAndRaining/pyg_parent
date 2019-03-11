package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
   private OrderService orderService;

    //根据商家id查询所有订单
    @RequestMapping("/findOrderFromSeller")
    public List<Order> findAll(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Order> orderList = orderService.findOrderFromSeller(username);
        return orderList;
    }

    //发货
    @RequestMapping("/sendGood")
    public Result sendGood(Long orderId){
        try {
            orderService.update(orderId);
            return new Result(true,"发货成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"发货失败");
        }
    }
//    @RequestMapping("dateOrder")
//    public List<Order> dateOrder(Date createTime ,Date finshTime){
//
//        String  seller_id= SecurityContextHolder.getContext().getAuthentication().getName();
//        List<Order> orderList = orderService.dateOrder(seller_id, createTime, finshTime);
//        return orderList;
//    }

//    @RequestMapping("search")
//    public PageResult search(Integer page, Integer rows, @RequestBody Order order){
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        order.setSellerId(username);
//        PageResult pageResult = orderService.search(page, rows, order);
//        return pageResult;
//    }

//    @RequestMapping("update")
//    public Result update(Long[] ids){
//        try {
//            orderService.update(ids);
//            return new Result(true,"fa成功");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new Result(false,"修改失败");
//        }
//    }
}
