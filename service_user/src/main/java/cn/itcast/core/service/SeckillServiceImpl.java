package cn.itcast.core.service;

import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.dao.seckill.SeckillOrderDao;
import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.pojo.seckill.SeckillOrderQuery;
import cn.itcast.core.pojo.seller.Seller;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SeckillServiceImpl implements SeckillService{

    @Autowired
    private SeckillOrderDao seckillOrderDao;

    @Autowired
    private SellerDao sellerDao;

    @Autowired
    private SeckillGoodsDao seckillGoodsDao;


    //分页查询所有秒杀订单

    @Override
    public PageResult findPageAllSeckill(String userName, Integer page, Integer rows, SeckillOrder seckillOrder) {
        //创建查询条件对象
        SeckillOrderQuery seckillOrderQuery= new SeckillOrderQuery();

        if(seckillOrderQuery!=null){
            //创建查询条件
            SeckillOrderQuery.Criteria criteria = seckillOrderQuery.createCriteria();
            criteria.andUserIdEqualTo(userName);
        }
//        int count = seckillOrderDao.countByExample(seckillOrderQuery);
        PageHelper.startPage(page, rows);
        Page<SeckillOrder> seckillOrderPage = (Page<SeckillOrder>) seckillOrderDao.selectByExample(seckillOrderQuery);

        //判断订单集合不为空,并循环
        if(seckillOrderPage!=null){
            for (SeckillOrder order : seckillOrderPage.getResult()) {
                //取出order里面的seller_id详情
                String sellerId = order.getSellerId();
                //根据商家id,查找出商家对象
                Seller seller = sellerDao.selectByPrimaryKey(sellerId);
                //根据商家对象,找出商家名称
                String sellerNickName = seller.getNickName();
                //将商家名称赋值给order里面的商家名称
                order.setSellerNickName(sellerNickName);

                //根据order里面的商品id,查询秒杀商品对象
                Long seckillId = order.getSeckillId();

                //
                SeckillGoods seckillGoods = seckillGoodsDao.selectByPrimaryKey(seckillId);


                order.setSeckillGoods(seckillGoods);




            }
        }

        //将结果封装到pageResult中返回
        PageResult pageResult = new PageResult(seckillOrderPage.getTotal(), seckillOrderPage.getResult());
        return pageResult;
    }


    //取消订单
    @Override
    public void update(Long id) {

        //创建一个新订单 集合表
     //   SeckillOrder seckillOrder = new SeckillOrder();
      //  seckillOrder.setId(id);
        //取消订单
       // seckillOrder.setStatus("8");

        SeckillOrder seckillOrder2 = seckillOrderDao.selectByPrimaryKey(id);
        seckillOrder2.setStatus("8");


        seckillOrderDao.updateByPrimaryKeySelective(seckillOrder2);

        //根据order里面的id,找出秒杀商品id
        SeckillOrder seckillOrder1 = seckillOrderDao.selectByPrimaryKey(id);

        //根据秒杀商品id,找到秒杀商品对象
        SeckillGoods seckillGoods = seckillGoodsDao.selectByPrimaryKey(seckillOrder1.getSeckillId());

        //秒杀商品的库存+1
        seckillGoods.setStockCount(seckillGoods.getStockCount()+1);

        //将秒杀商品重新封存
        seckillGoodsDao.updateByPrimaryKeySelective(seckillGoods);
    }
}
