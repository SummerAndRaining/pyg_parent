package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.service.BrandService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 品牌申请
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    //显示商家未审核通过规格实体集合数据
    @RequestMapping("/findAll")
    public  List<Brand> findAll(){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Brand> brandList = brandService.getBrandListFromRedis(userName);
        return brandList;
    }

    //商家品牌申请
    @RequestMapping("/addShopBrandToRedis")
    public Result addShopBrandToRedis(@RequestBody Brand brand) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            brandService.addShopBrandToRedis(userName,brand);
            return  new Result(true, "商家品牌申请成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, "商家品牌申请失败!");
        }
    }

    /**
     * 查询品牌所有数据, 返回, 给模板中select2下拉框使用, 数据格式是select2下拉框规定的
     * 例如: $scope.brandList={data:[{id:1,text:'联想'},{id:2,text:'华为'},{id:3,text:'小米'}]}
     */
    @RequestMapping("/selectOptionList")
    public List<Map> selectOptionList() {
        return brandService.selectOptionList();
    }

}
