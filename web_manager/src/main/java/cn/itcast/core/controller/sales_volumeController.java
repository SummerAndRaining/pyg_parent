package cn.itcast.core.controller;


import cn.itcast.core.service.Sales_volumeService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 运营商销量统计
 */
@RestController
@RequestMapping("/sales_volume")
public class sales_volumeController {

    @Reference
    private Sales_volumeService salesVolumeService;


    /**
     * 1. 运营商销量折线图统计
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     * @throws ParseException
     */
    @RequestMapping("/findSalesVolume")
    public Map<String, Object> findSalesVolume(String startDate, String endDate) throws ParseException {
        System.out.println(1);
        if (startDate != null && endDate != null) {
          /*  DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            DateFormat df02 = new SimpleDateFormat("MM/dd");
            Date startDate02 = df.parse(startDate);
            Date endDate02 = df.parse(endDate);*/

            //1.调用业务层查询销量统计折线图数据
            return salesVolumeService.findSalesVolume(startDate,endDate,null);
        }
        return null;
    }

    /**
     * 2. 查询所有销量
     * @return
     */
    @RequestMapping("/findOrderVolume")
    public Map<Object,Object> findOrderVolume(){
        return salesVolumeService.findOrderVolume();
    }
}
