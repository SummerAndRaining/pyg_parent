package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.order.Order;

//订单管理
public interface ContentOrderService {
    //1.分页查询订单
    PageResult search(Integer page, Integer rows, Order order);
}
