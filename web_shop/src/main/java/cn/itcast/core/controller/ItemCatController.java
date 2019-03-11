package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.service.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference
    private ItemCatService itemCatService;

    @RequestMapping("/findByParentId")
    public List<ItemCat> findByParentId(Long parentId) {
        List<ItemCat> list = itemCatService.findByParentId(parentId);
        return list;
    }

    @RequestMapping("/findOne")
    public ItemCat findOne(Long id) {
        return itemCatService.findOne(id);
    }

    @RequestMapping("/findAll")
    public List<ItemCat> findAll() {
        return itemCatService.findAll();
    }

    //显示商家未审核通过规格实体集合数据
    @RequestMapping("/findItemCatFromRedis")
    public  List<ItemCat> findItemCatFromRedis(){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ItemCat> itemCatList = itemCatService.getItemCatFromRedis(userName);
        return itemCatList;
    }

    //商家分类申请
    @RequestMapping("/addItemCatFromShop")
    public Result addItemCatFromShop(@RequestBody ItemCat itemCat) {
        try {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            itemCatService.addItemToRedis(userName,itemCat);
            return new Result(true, "商家分类申请成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "商家分类申请失败!");
        }
    }
}
