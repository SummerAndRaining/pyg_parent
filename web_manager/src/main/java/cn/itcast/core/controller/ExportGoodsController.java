package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.service.ExcelUtils01;
import cn.itcast.core.service.GoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("export")
public class ExportGoodsController {

    @Reference
    private GoodsService goodsService;


    @RequestMapping("exportexcel")
    public void  exportexcel(HttpServletResponse response) throws Exception {
        Workbook wb = new HSSFWorkbook();
        String headers[] = { "id", "卖家名称", "商品名称","品牌id","价格","是否删除" };
        int rowIndex = 0;
        Sheet sheet = wb.createSheet();
        Row row = sheet.createRow(rowIndex++);
        for (int i = 0; i < headers.length; i++) { // 先写表头
            row.createCell(i).setCellValue(headers[i]);
        }
        List<Goods> list = goodsService.getGoodsList();
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(list.get(i).getId());
            row.createCell(2).setCellValue(list.get(i).getGoodsName());
            row.createCell(1).setCellValue(list.get(i).getSellerId());
            row.createCell(3).setCellValue(list.get(i).getBrandId());
            row.createCell(4).setCellValue(list.get(i).getIsDelete());
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + new String("手动导出excel.xls".getBytes("utf-8"), "iso8859-1"));
        response.setContentType("application/ynd.ms-excel;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        wb.write(out);
        System.out.println(wb);
        out.flush();
        out.close();
    }


    }

















//        try {
////			创建工作簿
//            HSSFWorkbook wb = new HSSFWorkbook();
////			创建sheet
//            HSSFSheet sheet = wb.createSheet("列表");
////			创建表头
//            HSSFRow row = sheet.createRow(0);
//            //创建单元格
//            HSSFCell cell = row.createCell(0);
//            cell.setCellValue("id");
//            HSSFCell cell1 = row.createCell(1);
//            cell1.setCellValue("买家名称");
//            HSSFCell cell2 = row.createCell(2);
//            cell2.setCellValue("商品名称");
//            HSSFCell cell3 = row.createCell(3);
//            cell3.setCellValue("defalut_item_id");
//            HSSFCell cell4 = row.createCell(4);
//            cell4.setCellValue("audit_status");
//            HSSFCell cell5 = row.createCell(5);
//            cell5.setCellValue("is_maketable");
//            HSSFCell cell6 = row.createCell(6);
//            cell6.setCellValue("brand_id");
//            HSSFCell cell7 = row.createCell(7);
//            cell7.setCellValue("caption");
//            HSSFCell cell8 = row.createCell(8);
//            cell8.setCellValue("category1_id");
//            HSSFCell cell9 = row.createCell(9);
//            cell9.setCellValue("category2_id");
//            HSSFCell cell10 = row.createCell(10);
//            cell10.setCellValue("category3_id");
//            HSSFCell cell11 = row.createCell(11);
//            cell11.setCellValue("price");
//            HSSFCell cell12 = row.createCell(12);
//            cell11.setCellValue("type_template_id");
//            HSSFCell cell13 = row.createCell(13);
//            cell11.setCellValue("is_enable_spec");
//            HSSFCell cell14 = row.createCell(14);
//            cell11.setCellValue("is_delete");
//
//            //User user = new User();
//            //Goods goods = new Goods();
//            //List<User> list = userService.getUserList(user);
//            List<Goods> goodsList = goodsService.getGoodsList();
//            System.out.println(goodsList);
//            //System.out.println(list);
//            for (int i = 0; i < goodsList.size(); i++) {
//                //User us = list.get(i);
//                Goods goods = goodsList.get(i);
//                //创建表头
//                HSSFRow lrow = sheet.createRow(i + 1);
//                //创建单元格
//                HSSFCell lcell = lrow.createCell(0);
//                lcell.setCellValue(goods.getId());
//                HSSFCell lcell1 = lrow.createCell(1);
//                lcell1.setCellValue(goods.getSellerId());
//                HSSFCell lcell2 = lrow.createCell(2);
//                lcell2.setCellValue(goods.getGoodsName());
//                HSSFCell lcell3 = lrow.createCell(3);
//                //lcell3.setCellValue("null");
//                HSSFCell lcell4 = lrow.createCell(4);
//                lcell3.setCellValue(goods.getAuditStatus());
//                HSSFCell lcell5 = lrow.createCell(5);
//                lcell3.setCellValue(goods.getIsMarketable());
//               // HSSFCell lcell6 = lrow.createCell(6);
//                lcell3.setCellValue(goods.getBrandId());
//                HSSFCell lcell7 = lrow.createCell(7);
//                lcell3.setCellValue(goods.getCaption());
//                HSSFCell lcell8 = lrow.createCell(8);
//                lcell3.setCellValue(goods.getCategory1Id());
//                HSSFCell lcell9 = lrow.createCell(9);
//                lcell3.setCellValue(goods.getCategory2Id());
//                HSSFCell lcell10 = lrow.createCell(10);
//                lcell3.setCellValue(goods.getCategory3Id());
//                HSSFCell lcell11 = lrow.createCell(11);
//                lcell3.setCellValue(goods.getPrice().longValue());
//                HSSFCell lcell12 = lrow.createCell(12);
//                lcell3.setCellValue(goods.getTypeTemplateId());
//                HSSFCell lcell13 = lrow.createCell(13);
//                lcell3.setCellValue(goods.getIsEnableSpec());
//                HSSFCell lcell14 = lrow.createCell(14);
//                //lcell3.setCellValue(goods.getIsDelete());
//            }
//            //根据response获取输出流
//            response.setContentType("application/force-download"); // 设置下载类型
//            response.setHeader("Content-Disposition", "attachment;filename=goods.xls"); // 设置文件的名称
//            OutputStream oStream=response.getOutputStream(); // 输出流
//            //把工作薄写入到输出流
//            wb.write(oStream);
//            return new Result(true,"成功");
//        } catch (Exception e) {
////            // TODO: handle exception
////            try {
////                oStream.close();
////            } catch (Exception e1) {
////                e1.printStackTrace();
////            }
//            e.printStackTrace();
//            return new Result(false,"失败");
//        }
