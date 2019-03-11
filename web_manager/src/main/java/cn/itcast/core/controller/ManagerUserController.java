package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.ManagerUserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/allUser")
public class ManagerUserController {

    @Reference
    private ManagerUserService managerUserService;


    //运营商后台查询所有用户
    @RequestMapping("/search")
    public PageResult findAllUser(Integer page, Integer rows, @RequestBody User user){
        return managerUserService.findAllUser(page,rows,user);
    }

    //冻结一个用户
    @RequestMapping("/cold")
    public Result cold(Long id){
        try {
            managerUserService.cold(id);
            return new Result(true,"冻结成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"冻结失败!");
        }

    }

    //解冻一个用户
    @RequestMapping("/unCold")
    public Result unCold(Long id){
        try {
            managerUserService.unCold(id);
            return new Result(true,"解冻成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"解冻失败!");
        }
    }
    //统计用户活跃度
    @RequestMapping("/countUserActive")
    public List<Integer> countUserActive(Long id){
        List<Integer> list = managerUserService.countUserActive();
        return list;
    }

}
