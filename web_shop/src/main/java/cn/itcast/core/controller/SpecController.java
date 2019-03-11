package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.entity.SpecEntity;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.service.SpecService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 规格管理
 */
@RestController
@RequestMapping("/specification")
public class SpecController {

    @Reference
    private SpecService specService;


    //显示商家未审核通过规格实体集合数据
    @RequestMapping("/findAll")
    public  List<Specification> specEntityList(){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        List<SpecEntity> specEntityList = specService.getSepcListFromRedis(userName);
        List<Specification> specificationList = new ArrayList<>();
        if (specEntityList != null) {
            for (SpecEntity specEntity : specEntityList) {
                Specification specification = specEntity.getSpecification();
                specificationList.add(specification);
            }
        }
        return specificationList;
    }

    //商家规格申请
    @RequestMapping("/addSpecFromShop")
    public Result addSpecFromShop(@RequestBody SpecEntity specEntity) {
        try {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            specService.addSpecFromShop(userName,specEntity);
            return new Result(true, "商家规格申请成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "商家规格申请失败!");
        }
    }

    /**
     * 模板下拉select2使用
     * @return
     */
    @RequestMapping("/selectOptionList")
    public List<Map> selectOptionList() {
        //System.out.println(specService.selectOptionList());
        return specService.selectOptionList();
    }
}
