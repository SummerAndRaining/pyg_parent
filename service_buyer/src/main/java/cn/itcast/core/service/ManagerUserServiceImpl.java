package cn.itcast.core.service;

import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Service
public class ManagerUserServiceImpl implements ManagerUserService {

    @Autowired
    private UserDao userDao;

    @Override
    //分页查询
    public PageResult findAllUser(Integer page, Integer rows, User user) {
        PageHelper.startPage(page,rows);
        UserQuery userQuery = new UserQuery();
        UserQuery.Criteria criteria = userQuery.createCriteria();
        if (user!=null){
            if (user.getUsername()!=null&&!"".equals(user.getUsername())){
                System.out.println(user.getUsername());
                criteria.andUsernameLike("%"+user.getUsername()+"%");
            }
        }
        Page<User> userList = (Page<User>) userDao.selectByExample(userQuery);
        return new PageResult(userList.getTotal(),userList.getResult());
    }

    //冻结一个用户
    public void cold(Long id){
        User user = new User();
        user.setId(id);
        user.setStatus("2");
        userDao.updateByPrimaryKeySelective(user);
    }

    //解冻一个用户
    public void unCold(Long id){
        User user = new User();
        user.setId(id);
        user.setStatus("1");
        userDao.updateByPrimaryKeySelective(user);
    }

    //限制登录,根据用户名查询冻结情况
    public String coldLogin(String username){
        UserQuery userQuery = new UserQuery();
        UserQuery.Criteria criteria = userQuery.createCriteria();
        criteria.andUsernameEqualTo(username);
        User users = (User) userDao.selectByExample(userQuery);
        return users.getStatus();
    }

    //统计活跃度
    public List<Integer> countUserActive(){
        int a = 0;//一个月的计数器
        int b = 0;//三个月的计数器
        int c = 0;//一年的计数器
        Date currentDate = new Date();//当前日期
        List<User> userList = userDao.selectByExample(null);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (User user : userList) {
            Date lastLoginTime = user.getLastLoginTime();
            //计算相差多少天
            int days = differentDays(lastLoginTime, currentDate);
            if (days<=30){
                a++;
            }else if (days>30&&days<=90){
                b++;
            }else {
                c++;
            }
        }
        ArrayList<Integer> list = new ArrayList<>();
        list.add(a);
        list.add(b+a);
        list.add(c+b+a);
        return list;

    }
    //计算两个日期相差多少天
    private  int differentDays(Date date1,Date date2){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        int day1 = calendar1.get(Calendar.DAY_OF_YEAR);
//        System.out.println(day1);
        int day2 = calendar2.get(Calendar.DAY_OF_YEAR);
//        System.out.println(day2);
        int year1 = calendar1.get(Calendar.YEAR);
        int year2 = calendar2.get(Calendar.YEAR);

        if (year1 != year2)  //不同年
        {
            int timeDistance = 0;
            for (int i = year1 ; i < year2 ;i++){ //闰年
                if (i%4==0 && i%100!=0||i%400==0){
                    timeDistance += 366;
                }else { // 不是闰年
                    timeDistance += 365;
                }
            }
            return  timeDistance + (day2-day1);
        }else{// 同年
            return day2-day1;
        }

    }

}
