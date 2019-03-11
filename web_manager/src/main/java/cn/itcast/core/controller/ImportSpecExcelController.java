package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.service.BrandService;
import cn.itcast.core.service.ExcelUtils01;
import cn.itcast.core.service.SpecService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;



@RestController
@RequestMapping("/uploadSpec")
public class ImportSpecExcelController {
    @Reference
    private SpecService specService;

    @RequestMapping("/uploadFile")
    public Result ajaxUploadExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        InputStream in =null;
        MultipartFile file = multipartRequest.getFile("file");
        if(file.isEmpty()){
            throw new Exception("文件不存在！");
        }

        in = file.getInputStream();
        String[][] strings = ExcelUtils01.readexcellByInput(in, file.getOriginalFilename(), 1);
        specService.importExcel(strings);

//        PrintWriter out = null;
//        response.setCharacterEncoding("utf-8");  //防止ajax接受到的中文信息乱码
//        out = response.getWriter();
//        out.print("文件导入成功！");
//        out.flush();
//        out.close();
        return new Result(true,"上传成功");
    }
}
