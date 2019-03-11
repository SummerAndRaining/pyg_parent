package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.seckill.SeckillOrder;

import java.util.List;

public interface SeckillOrderService {

   /* public SeckillOrder findOne(Long id);
    */


    public PageResult search(Integer page, Integer rows, SeckillOrder seckillOrder);


    //查询所有
    public List<SeckillOrder> findAll(String username);





}
