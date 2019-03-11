package cn.itcast.core.service;

import java.text.ParseException;
import java.util.Map;

/**
 * 销量统计Echarts表
 */
public interface Sales_volumeService {
    //1. 查询销售折线图
    Map<String,Object> findSalesVolume(String startDate, String endDate, String userName) throws ParseException;

    //2. 查询所有销量
    Map<Object,Object> findOrderVolume();
}
