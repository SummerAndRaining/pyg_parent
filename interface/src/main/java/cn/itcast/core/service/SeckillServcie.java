package cn.itcast.core.service;

import cn.itcast.core.pojo.seckill.SeckillGoods;

import java.util.List;

public interface SeckillServcie {
    //1.查询所有秒杀商品
    List<SeckillGoods> findAll();
}
