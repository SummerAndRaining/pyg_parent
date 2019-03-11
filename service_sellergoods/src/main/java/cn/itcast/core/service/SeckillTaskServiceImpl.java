package cn.itcast.core.service;

import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillGoodsQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SeckillTaskServiceImpl implements SeckillTaskService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsDao seckillGoodsDao;

    @Override
    @Scheduled(cron = "* * * * * ?")
    public void refreshSeckillGoods() {
        System.out.println("执行了任务调度" + new Date());
        //查询所有的秒杀商品键集合
        List<Long> ids = new ArrayList(redisTemplate.boundHashOps("seckillGoods").keys());
        //查询正在秒杀的商品列表


        SeckillGoodsQuery query = new SeckillGoodsQuery();
        SeckillGoodsQuery.Criteria criteria = query.createCriteria();
        //审核通过
        criteria.andStatusEqualTo("1");
        //剩余库存大于0
        criteria.andStockCountGreaterThan(0);
        //开始时间小于等于当前时间
        criteria.andStartTimeLessThanOrEqualTo(new Date());
        //结束时间大于当前时间
        criteria.andEndTimeGreaterThan(new Date());
        //派出缓存中已经有的商品
        criteria.andIdNotIn(ids);
        List<SeckillGoods> seckillGoods = seckillGoodsDao.selectByExample(query);


        System.out.println(seckillGoods);

        if (seckillGoods != null && seckillGoods.size() > 0) {
            for (SeckillGoods seckillGood : seckillGoods) {
                redisTemplate.boundHashOps("seckillGoods").put(seckillGood.getId(), seckillGood);
            }
        }
        System.out.println("将" + seckillGoods.size() + "条商品装入缓存");
    }

    @Override
    @Scheduled(cron = "* * * * * ?")
    public void removeSeckillGoods() {
        System.out.println("移除秒杀商品任务在执行");

        List<SeckillGoods> seckillGoods = redisTemplate.boundHashOps("seckillGoods").values();
        if (seckillGoods!=null){
            for (SeckillGoods seckillGood : seckillGoods) {
                if (seckillGood.getEndTime().getTime()<new Date().getTime()){
                    redisTemplate.boundHashOps("seckillGoods").delete(seckillGood.getId());
                    System.out.println("移除秒杀商品============="+seckillGood.getTitle());
                }
            }
        }
        System.out.println("移除秒杀商品任务结束");
    }
}
