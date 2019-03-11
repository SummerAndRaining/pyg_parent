package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.seckill.SeckillOrder;

import cn.itcast.core.service.SeckillService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("seckill")
public class SeckillController {

    @Reference
    private SeckillService seckillService;

    /**
     * 秒杀订单列表
     *
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("findPageAllSeckill")
    public PageResult findPageAllSeckill(Integer page, Integer rows) {
        //获取登录用户名
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        PageResult pageResult = seckillService.findPageAllSeckill(userName, page, rows, null);

        return pageResult;
    }

    @RequestMapping("update")
    public Result update(Long id) {

        try {
            seckillService.update(id);
            return new Result(true, "订单取消成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "订单取消失败");
        }
    }

}
