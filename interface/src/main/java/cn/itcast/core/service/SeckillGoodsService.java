package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seller.Seller;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品秒杀申请
 */
public interface SeckillGoodsService {
    //1. 获取商家名称
    Seller findName(String userName);

    //2. 根据商家用户名搜索商家拥有的商品
    List<Goods> findByParentId(String name);

    //3. 根据商品id查询库存集合
    List<Item> findByParent(Long id);

    //4. 根据库存集合id查询价格
    BigDecimal findPriceOld(Long id);

    //5. 添加秒杀申请
    void add(SeckillGoods seckillGoods);

    //6.运营商查询所有未审核商品
    PageResult search(Integer page, Integer rows, SeckillGoods title);

    //7. 查询所有商户名字
    List<Seller> findAllName();

    //8. 查询所有商品名字
    List<Goods> findAllGoods();

    //9.查询所有库存集合名字
    List<Item> finditemname();

    //10. 根据秒杀商品id改变数据库中秒杀商品状态
    void updateStatus(Long[] ids, String status);
}
