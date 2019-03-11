package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.order.Order;

import java.util.List;

public interface OrderService {

    /**
     * 保存订单
     * 涉及到三张表, payLog支付日志, order订单表, orderItem订单详情表
     *
     * @param order 页面提交的订单对象数据, 这个对象不直接保存到数据库中, 只是需要页面提交过来的
     *              收货人地址, 收货人姓名, 收货人电话, 支付方式等信息.
     */
    public void add(Order order);

    /**
     * 根据支付单号修改, 支付状态为已支付
     *
     * @param out_trade_no 支付单号
     */
    public void updatePayLogAndOrderStatus(String out_trade_no);

    //杨涛---------------------------杨涛---------------------商家后台-订单查询


    //查询订单
    List<Order> findOrderFromSeller(String username);

    //修改状态发货
    void update(Long id);

    /*List<Order> dateOrder(String seller_id, Date createTime, Date finshTime);*/
    //杨涛---------------------------杨涛---------------------商家后台-订单查询

    public List<Order> getList();

    //根据用户名查询所有订单
   // public List<Order> findAll(String userName);


    //根据用户分页查询所有订单
    public PageResult search(String userName,Integer page, Integer rows, Order order);

}
