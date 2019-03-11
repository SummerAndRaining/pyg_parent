package cn.itcast.core.service;


import cn.itcast.core.pojo.user.User;

public interface UserService {

    public void sendCode(String phone);

    public boolean checkSmsCode(String phone, String smsCode);

    public  void  add(User user);

    //查询登录用户信息
    public User findUser(String userName);

    //新增收货人信息地
    //  public void addUserAddress( Address address);

    //修改个人信息
    public void  updateUser(User user);
}
