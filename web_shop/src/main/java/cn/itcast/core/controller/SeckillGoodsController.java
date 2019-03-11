package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.service.SeckillGoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品秒杀店家申请
 */
@RestController
@RequestMapping("/seckillgoods")
public class SeckillGoodsController {

    //获取业务层接口
    @Reference
    private SeckillGoodsService seckillGoodsService;

    /**
     * 1. 获取商家用户名,
     *
     * @return
     */
    @RequestMapping("/findName")
    public Seller findName() {
        //1.查询当前登陆用户id
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        //2.获取当前登陆用户名
        return seckillGoodsService.findName(userName);
    }

    /**
     * 2. 根据商家用户名搜索商家拥有的商品
     *
     * @return
     */
    @RequestMapping("/findByParentId")
    public List<Goods> findByParentId(){
        //1.查询当前登陆用户id
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        //2.根据当前登路用户名获取商品集合
        List<Goods> goods = seckillGoodsService.findByParentId(name);
        System.out.println(goods);
        return goods;
    }

    /**
     * 3. 根据商品id查询库存集合
     * @param id  商品id
     * @return
     */
    @RequestMapping("/findByParent")
    public List<Item> findByParent(Long id){
        List<Item> itemList = seckillGoodsService.findByParent(id);
        System.out.println(itemList);
        return itemList;
    }

    /**
     * 4.根据库存集合id查询价格
     * @param id
     * @return
     */
    @RequestMapping("/findPriceOld")
    public BigDecimal findPriceOld(Long id){
        BigDecimal priceOld = seckillGoodsService.findPriceOld(id);
        return priceOld;
    }

    /**
     * 5. 添加秒杀申请
     * @param seckillGoods  申请信息数据
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody SeckillGoods seckillGoods){
        try {
            if (seckillGoods.getGoodsId()!=null && seckillGoods.getItemId()!=null
                    && seckillGoods.getTitle()!=null && seckillGoods.getPrice()!=null
                    && seckillGoods.getCostPrice()!=null && seckillGoods.getStockCount()!=null) {
                seckillGoodsService.add(seckillGoods);
                return new Result(true,"申请成功");
            }
            return new Result(false,"填写信息");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"申请失败");
        }
    }
}