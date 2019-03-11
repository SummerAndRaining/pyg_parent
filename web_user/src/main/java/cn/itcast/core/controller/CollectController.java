package cn.itcast.core.controller;

import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.service.CollectService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.sun.corba.se.spi.ior.IORTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("collect")
public class CollectController {

    @Reference
    CollectService collectService;

    @RequestMapping("findAll")
    public List<Item> findAll(String userName) {
        List<Item> collectServiceAll = collectService.findAll(userName);
        return collectServiceAll;
    }
}
