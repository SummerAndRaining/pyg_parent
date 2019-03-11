package cn.itcast.core.service;

import cn.itcast.core.dao.seckill.SeckillOrderDao;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.pojo.seckill.SeckillOrderQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {


    @Autowired
    private SeckillOrderDao seckillOrderDao;





    public List<SeckillOrder> findAll(String username){
        SeckillOrderQuery seckillOrderQuery = new SeckillOrderQuery();
        SeckillOrderQuery.Criteria criteria = seckillOrderQuery.createCriteria();
        criteria.andSellerIdEqualTo(username);
        List<SeckillOrder> seckillOrderList = seckillOrderDao.selectByExample(seckillOrderQuery);
        return seckillOrderList;
    }

  /*  @Override    //回显
    public SeckillOrder findOne(Long id) {
        SeckillOrder seckillOrder = seckillOrderDao.selectByPrimaryKey(id);
        return seckillOrder;
    }*/

    @Override        //秒杀订单的分页查询和数据展示
    public PageResult search(Integer page, Integer rows, SeckillOrder seckillOrder) {
        //分页查询
        PageHelper.startPage(page,rows);
        //创建查询条件对象,根据订单id查询
        SeckillOrderQuery query = new SeckillOrderQuery();
//        if (seckillOrder!=null){
//            SeckillOrderQuery.Criteria criteria = query.createCriteria();
//           criteria.andIdEqualTo(seckillOrder.getId());
//        }
        //查询秒杀订单的数据
        Page<SeckillOrder> seckillOrderList = (Page<SeckillOrder>) seckillOrderDao.selectByExample(null);
        return new PageResult(seckillOrderList.getTotal(),seckillOrderList.getResult());


    }
}
