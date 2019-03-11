package cn.itcast.core.controller;


import cn.itcast.core.service.Sales_volumeService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 运营商销量统计
 */
@RestController
@RequestMapping("/sales_volume")
public class sales_volumeController {

    @Reference
    private Sales_volumeService salesVolumeService;


    /**
     * 1. 商家日销量折线图统计
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     * @throws ParseException
     */
    @RequestMapping("/findShopVolume")
    public Map<String, Object> findShopVolume(String startDate, String endDate) throws ParseException {


        //1.判断传入时间是否为空
        if (startDate!=null && endDate!=null){
            //2. 获取前登陆用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            return salesVolumeService.findSalesVolume(startDate,endDate,userName);
        }
        return null;
    }
}
