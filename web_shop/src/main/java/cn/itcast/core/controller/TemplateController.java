package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.service.TemplateService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/typeTemplate")
public class TemplateController {

    @Reference
    private TemplateService templateService;



    @RequestMapping("/findOne")
    public TypeTemplate findOne(Long id) {
        return templateService.findOne(id);
    }

    /**
     *
     * @param id
     */
    @RequestMapping("/findBySpecList")
    public List<Map> findBySpecList(Long id) {
        List<Map> specList = templateService.findBySpecList(id);
        return specList;
    }


    //显示商家未审核通过模板实体集合数据
    @RequestMapping("/findAll")
    public List<TypeTemplate> shopFindFromRedis() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        List<TypeTemplate> templateList = templateService.shopFindFromRedis(userName);
        return templateList;
    }

    //商家模板申请
    @RequestMapping("/addTemplateFromShop")
    public Result addTemplateFromShop(@RequestBody TypeTemplate typeTemplate){
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            templateService.addTemplateFromShop(typeTemplate,name);
            return new Result(true,"商家模板申请成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"商家模板申请失败");
        }

    }

}
