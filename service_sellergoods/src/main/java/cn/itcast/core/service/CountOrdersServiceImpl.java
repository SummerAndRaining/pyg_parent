package cn.itcast.core.service;

import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class CountOrdersServiceImpl implements CountOrdersService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private OrderDao orderDao;

    @Override//查询所有用户信息
    public List<User> findAll() {
        List<User> userList = userDao.selectByExample(null);
        return userList;
    }

    @Override   //分页查询,搜索,展示用户信息
    public PageResult search(Integer page, Integer rows, User user) {

        PageHelper.startPage(page, rows);
        UserQuery query = new UserQuery();
        UserQuery.Criteria criteria = query.createCriteria();
        if (user.getNickName() != null && !"".equals(user.getNickName())) {
            criteria.andNickNameLike("%" + user.getNickName() + "%");
        }
        Page<User> userList = (Page<User>) userDao.selectByExample(query);
        return new PageResult(userList.getTotal(), userList.getResult());

    }

    @Override
    public List<Map> countOrder() {


        List<Map> map = orderDao.countOrder();
        for (Map map1 : map) {
            System.out.println(map1);

        }
        return map;
    }
}
