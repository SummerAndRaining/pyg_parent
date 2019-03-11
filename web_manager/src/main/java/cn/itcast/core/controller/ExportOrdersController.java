package cn.itcast.core.controller;


import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("/exportOrder")
public class ExportOrdersController {
    @Reference
    private OrderService orderService;

    @RequestMapping("/exportexcel")
    public void exportexcel(HttpServletResponse response) throws Exception {
        Workbook wb = new HSSFWorkbook();
        String headers[] = {"订单名称", "payment", "status", "创建时间", "修改时间", "用户名称", "收件人地址", "收件人电话", "收件人名称", "卖家名称"};
        int rowIndex = 0;
        Sheet sheet = wb.createSheet();
        Row row = sheet.createRow(rowIndex++);
        for (int i = 0; i < headers.length; i++) { // 先写表头
            row.createCell(i).setCellValue(headers[i]);
        }
        List<Order> list = orderService.getList();
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(list.get(i).getOrderId());
            row.createCell(2).setCellValue(list.get(i).getStatus());
            row.createCell(1).setCellValue(String.valueOf(list.get(i).getPayment()));
            row.createCell(3).setCellValue(list.get(i).getCreateTime());
            row.createCell(4).setCellValue(list.get(i).getUpdateTime());
            row.createCell(5).setCellValue(list.get(i).getUserId());
            row.createCell(6).setCellValue(list.get(i).getReceiverAreaName());
            row.createCell(7).setCellValue(list.get(i).getReceiverMobile());
            row.createCell(8).setCellValue(list.get(i).getReceiver());
            row.createCell(9).setCellValue(list.get(i).getSellerId());
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + new String("手动导出excel.xls".getBytes("utf-8"), "iso8859-1"));
        response.setContentType("application/ynd.ms-excel;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        wb.write(out);
        //System.out.println(wb);
        out.flush();
        out.close();
    }
}
