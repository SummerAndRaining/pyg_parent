package cn.itcast.core.controller;

import cn.itcast.core.common.PhoneFormatCheckUtils;
import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.pojo.entity.BuyerCart;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.OrderService;
import cn.itcast.core.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.aspectj.weaver.ast.Var;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    @Reference
    private OrderService orderService;



    /**
     * 生成随机六位以内数字作为验证码, 发送到指定手机号上
     * @param phone 手机号
     * @return
     */
    @RequestMapping("/sendCode")
    public Result sendCode(String phone) {
        try {
            if (phone == null || "".equals(phone)) {
                return new Result(false, "请正确填写手机号!");
            }
            if (!PhoneFormatCheckUtils.isPhoneLegal(phone)) {
                return new Result(false, "手机号不正确!");
            }
            userService.sendCode(phone);
            return new Result(true , "短信发送成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false , "短信发送失败!");
        }
    }

    /**
     * 完成注册, 保存用户
     * @param user      用户对象
     * @param smscode   页面填写的验证码
     */
    @RequestMapping("/add")
    public Result add(@RequestBody User user, String smscode) {
        try {
            //1. 校验验证码是否正确
            boolean isCheck = userService.checkSmsCode(user.getPhone(), smscode);
            //2. 如果验证码或者手机号不正确返回错误信息
            if (!isCheck) {
                return new Result(false, "手机号或者验证码填写错误!");
            }
            //3. 保存用户
            user.setCreated(new Date());
            user.setUpdated(new Date());
            //用户注册来源默认为pc端浏览器注册
            user.setSourceType("1");
            //用户状态默认为正常
            user.setStatus("Y");
            userService.add(user);
            return new Result(true, "注册成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "注册失败!");
        }
    }


    //查询所有订单
    /*@RequestMapping("/findAll")
    public List<BuyerCart> findAll(){
      String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        List<BuyerCart> all = orderService.findAll(userName);
        return all;

    }*/

    //分页查询所有订单

    @RequestMapping("search")
    public PageResult search(Integer page, Integer rows, @RequestBody Order order){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        PageResult pageResult = orderService.search(userName, page, rows, order);
        return pageResult;

    }

//    @RequestMapping("/findAll")
//    public List<Order> findAll(){
//        String name = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        List<Order> all = orderService.findAll(name);
//        return all;
//
//    }


    //查询登录用户信息回显
    @RequestMapping("findUser")
    public User findUser(){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
       User user = userService.findUser(userName);
        return user;
    }

    /**
     * 新增收货人信息
     * @param address 收货人地址
     * @return
     */
   /* @RequestMapping("addUserAddress")
    public Result addUserAddress(@RequestBody Address address){

        try {
            //获取登录用户名
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();

            address.setUserId(userName);
            userService.addUserAddress(address);
            return new Result(true,"地址添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"地址添加失败!");
        }
    }*/

    /**
     * 修改个信息地址
     * @param user
     * @return
     */
   @RequestMapping("updateUser")
   public Result updateUser(@RequestBody User user){
       try {
           String userName = SecurityContextHolder.getContext().getAuthentication().getName();
           user.setUsername(userName);
           userService.updateUser(user);
           return  new Result(true,"个人信息修改成功");
       } catch (Exception e) {
           e.printStackTrace();
           return new Result(false,"个人信息修改失败");
       }

   }
}
