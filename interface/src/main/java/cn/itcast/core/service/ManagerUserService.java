package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.user.User;

import java.util.List;

public interface ManagerUserService {


    //运营商后台显示所有用户
    public PageResult findAllUser(Integer page, Integer rows, User user);

    //冻结一个用户
    public void cold(Long id);

    //解冻一个用户
    public void unCold(Long id);

    //限制登录,根据用户名查询冻结情况
    public String coldLogin(String username);

    //统计活跃度
    public List<Integer> countUserActive();


}
