package cn.itcast.core.controller;

import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.service.SeckillServcie;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 秒杀查询
 */
@RestController
@RequestMapping("/Seckill")
public class SeckillController {

    @Reference
    private SeckillServcie seckillServcie;

    /**
     * 1.查询所有秒杀商品
     * @return
     */
    @RequestMapping("/findAll")
    private List<SeckillGoods> findAll(){
        return seckillServcie.findAll();
    }
}
