package cn.itcast.core.service;

import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订单管理
 * 将当前类交给spring管理
 */
@Service
@Transactional
public class ContentOrderServiceImpl implements ContentOrderService {

    @Autowired
    private OrderDao orderDao;

    @Override
    public PageResult search(Integer page, Integer rows, Order order) {
        //1.创建查询对象
        OrderQuery query = new OrderQuery();
        //2.创建条件对象
        OrderQuery.Criteria criteria = query.createCriteria();
        //3.将前端条件添加到查询对象中
        //3.1判断条件是否为空
        if (order!=null){
            if (order.getBuyerNick()!=null && !"".equals(order.getBuyerNick())){
                criteria.andBuyerNickEqualTo(order.getBuyerNick());
            }
        }

        //4.分页查询
        PageHelper.startPage(page,rows);
        Page<Order> orders = (Page<Order>) orderDao.selectByExample(query);
        return new PageResult(orders.getTotal(),orders.getResult());
    }
}
