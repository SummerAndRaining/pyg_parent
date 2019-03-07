package cn.itcast.core.controller;


import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.ContentOrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 订单管理
 * 将当前类交给spring管理
 */
@RestController
@RequestMapping("/contentorder")
public class ContentorderController {

    /**
     * 获取接口对象
     */
    @Reference
    private ContentOrderService contentOrderService;

    /**
     * 1.分页查询所有订单
     * @param page  当前页
     * @param rows  每页显示条数
     * @param order 订单对象
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows,@RequestBody Order order){
        return contentOrderService.search(page,rows,order);
    }
}
