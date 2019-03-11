package cn.itcast.core.controller;


import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.service.SeckillGoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 秒杀商品审核
 */
@RestController
@RequestMapping("/SeckillGoodsOrder")
public class SeckillGoodsOrderController {

    @Reference
    private SeckillGoodsService seckillGoodsService;

    /**
     * 1. 查询所有未审核商品
     *
     * @param page
     * @param rows
     * @param seckillGoods
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows, @RequestBody SeckillGoods seckillGoods) {
        //1. 调用业务层查询未审核秒杀商品
        return seckillGoodsService.search(page, rows, seckillGoods);
    }

    /**
     * 2. 查询所有商户名字
     *
     * @return
     */
    @RequestMapping("/findName")
    public List<Seller> findName() {
        return seckillGoodsService.findAllName();
    }

    /**
     * 3. 查询所有商品名字
     *
     * @return
     */
    @RequestMapping("/findAllGoods")
    public List<Goods> findAllGoods() {
        return seckillGoodsService.findAllGoods();
    }

    /**
     * 4. 查询所有库存集合名字
     *
     * @return
     */
    @RequestMapping("/finditemname")
    public List<Item> finditemname() {
        return seckillGoodsService.finditemname();
    }

    /**
     * 5. 根据秒杀商品id改变数据库中秒杀商品状态
     *
     * @param ids    更改秒杀商品状态id集合
     * @param status 改变的商品状态
     * @return
     */
    @RequestMapping("/updateStatus")
    public Result updateStatus(Long[] ids, String status) {
        try {
            //1. 更改商品id状态
            if (ids != null && ids.length > 0) {
                seckillGoodsService.updateStatus(ids, status);
                if (status=="2"){
                    return new Result(true,"驳回审核");
                }
                return new Result(true, "商品秒杀店家审核通过");
            }
            return new Result(false, "未勾选商品");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "商品秒杀店家非法");
        }
    }
}
