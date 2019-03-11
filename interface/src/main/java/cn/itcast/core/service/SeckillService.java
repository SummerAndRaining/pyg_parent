package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.seckill.SeckillOrder;

import java.util.List;

public interface SeckillService {
    //秒杀订单列表
    public PageResult findPageAllSeckill(String userName, Integer page, Integer rows, SeckillOrder seckillOrder);

    //取消订单
    public void update(Long id);

}
