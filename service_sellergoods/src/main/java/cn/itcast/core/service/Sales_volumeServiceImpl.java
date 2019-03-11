package cn.itcast.core.service;

import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderItemQuery;
import cn.itcast.core.pojo.order.OrderQuery;
import cn.itcast.core.pojo.seller.Seller;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 统计销量
 */
@Service
@Transactional
public class Sales_volumeServiceImpl implements Sales_volumeService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private SellerDao sellerDao;

    /**
     * 1. 统计日销量折线图
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    @Override
    public Map<String, Object> findSalesVolume(String startDate, String endDate, String userName) throws ParseException {
        //1. 将字符串时间转换为日期时间
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat df02 = new SimpleDateFormat("MM/dd");
        Date startDate02 = df.parse(startDate);
        Date endDate02 = df.parse(endDate);

        long time1 = startDate02.getTime();
        long time2 = endDate02.getTime();

        List<String> list = new ArrayList<>();
        List<Integer> list02 = new ArrayList<>();

        //创建map集合,接收数据并将数据返回
        Map<String, Object> map = new HashMap<>();
        for (long i = time1; i <= time2; i = i + (60 * 60 * 24 * 1000)) {
            System.out.println("=======" + new Date(i));
            list.add(df02.format(new Date(i)));

            //2. 根据时间查询订单id
            //2.1 创建查询条件对象
            OrderQuery query = new OrderQuery();
            //2.2 创建查询条件
            OrderQuery.Criteria criteria = query.createCriteria();
            //2.3 向查询条件中添加条件
            //添加事件条件
            criteria.andCreateTimeBetween(new Date(i), new Date(i + (60 * 60 * 24 * 1000)));
            //添加商家条件
            if (userName != null && !"".equals(userName)) {
                criteria.andSellerIdEqualTo(userName);
            }

            //2.4 调用持久层进行查询
            List<Order> orders = orderDao.selectByExample(query);

            //5. 创建数字保存当日销售总量
            Integer numAll = 0;

            //2.5 遍历orders,获取ordersid

            if (orders != null && orders.size() > 0) {
                for (Order order : orders) {
                    //获取orders中的id
                    Long orderId = order.getOrderId();

                    //通过orderId获取当前订单中的订单商品总数量
                    //3. 查询orderitem
                    //3.1 创建查询条件对象
                    OrderItemQuery orderItemQuery = new OrderItemQuery();
                    //3.2 创建查询条件
                    OrderItemQuery.Criteria orderItemQueryCriteria = orderItemQuery.createCriteria();
                    //3.3 向查询条件中添加条件
                    orderItemQueryCriteria.andOrderIdEqualTo(orderId);
                    //3.4 调用持久层进行查询
                    List<OrderItem> orderItemList = orderItemDao.selectByExample(orderItemQuery);
                    System.out.println(orderItemList);

                    //4. 遍历orderItemList类获取其中每个订单项的数量
                    if (orderItemList != null && orderItemList.size() > 0) {
                        for (OrderItem orderItem : orderItemList) {
                            Integer num = orderItem.getNum();
                            numAll = numAll + num;
                        }
                    }
                }
                //6. 将总数量保存到list集合中
                list02.add(numAll);
            } else {
                list02.add(numAll);
            }
        }

        map.put("time", list);
        map.put("salesVolumeList", list02);
        return map;
    }

    /**
     * 2. 查询所有销量
     *
     * @return
     */
    @Override
    public Map<Object, Object> findOrderVolume() {
        //创建map集合用于返回数据
        Map<Object, Object> map = new HashMap<>();
        //1. 调用持久层获取所有商户用户id
        List<String> orderVolume = orderItemDao.findOrderVolume();

        //8.创建返回数据集合
        //8.1创建返回商家名集合
        List<String> userName = new ArrayList<>();
        //8.2创建返回总数量集合
        List<Integer> numAll = new ArrayList<>();
        for (String sellieName : orderVolume) {
            Seller seller = sellerDao.selectByPrimaryKey(sellieName);
            String sellerName = seller.getName();

            //8.3创建num用于接收当前用户总购买量
            Integer num = 0;

            //2. 根据商户id获取商家,查询三年内商家的总销量
            //3 查询订单
            //3.1 创建查询对象
            OrderQuery query = new OrderQuery();
            //3.2 创建查询条件
            OrderQuery.Criteria criteria = query.createCriteria();
            //3.3 向查询条件中添加条件
            //添加商家条件
            criteria.andSellerIdEqualTo(sellieName);
            //添加时间条件
            //1.创建当前时间
            long time1 = new Date().getTime();

            //2.创建三年前时间
            long time2 = time1 - (60 * 60 * 24 * 1000 * 30 * 12 * 4);

            //3.添加时间条件
            //criteria.andCreateTimeBetween(new Date(time2), new Date(time1));

            //3.4 查询订单
            List<Order> orders = orderDao.selectByExample(query);

            //4. 查询订单项

            if (orders != null && orders.size() > 0) {
                for (Order order : orders) {
                    //5.获取订单id
                    Long orderId = order.getOrderId();

                    //6.根据订单id获取订单项
                    //6.1创建查询对象
                    OrderItemQuery itemQuery = new OrderItemQuery();
                    //6.2创建查询条件对象
                    OrderItemQuery.Criteria itemQueryCriteria = itemQuery.createCriteria();
                    //6.3想查询条件对象添加查询条件
                    itemQueryCriteria.andOrderIdEqualTo(orderId);
                    //6.4查询订单项
                    List<OrderItem> orderItemList = orderItemDao.selectByExample(itemQuery);

                    //7. 获取当订单的商品总量
                    if (orderItemList != null && orderItemList.size() > 0) {
                        for (OrderItem orderItem : orderItemList) {
                            //7.1获取订单项的商品总购买量
                            num = num + orderItem.getNum();
                        }
                    }
                }
            }
            //向list集合中添加数据
            //9.1添加商家
            userName.add(sellerName);

            //9.2添加销售数量
            numAll.add(num);
        }
        System.out.println(userName);
        System.out.println(numAll);
        //10.向map集合中添加数据
        //10.1添加商家
        map.put("orderName", userName);
        //10.2添加销售数量
        map.put("num", numAll);
        return map;
    }

}
