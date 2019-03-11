package cn.itcast.core.service;

import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ActiveMQQueue smsDestination;

    @Value("${template_code}")
    private String template_code;
    @Value("${sign_name}")
    private String sign_name;

    @Autowired
    private UserDao userDao;

    @Override
    public void sendCode(final String phone) {
        //1. 生成随机6位以内的数字作为验证码
        final String code = String.valueOf((long)(Math.random() * 1000000));
        //2. 将手机号作为key, 验证码作为value存入redis中
        redisTemplate.boundValueOps(phone).set(code, 10, TimeUnit.MINUTES);
        //3. 将手机号, 验证码, 签名, 模板编号等信息组成消息对象发送给消息服务器
        jmsTemplate.send(smsDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = new ActiveMQMapMessage();
                //手机号
                mapMessage.setString("mobile", phone);
                //模板编号
                mapMessage.setString("template_code", template_code);
                //签名
                mapMessage.setString("sign_name", sign_name);

                //验证码
                Map<String, String> codeMap = new HashMap<>();
                codeMap.put("code", code);
                mapMessage.setString("param", JSON.toJSONString(codeMap));
                return mapMessage;
            }
        });
    }

    @Override
    public boolean checkSmsCode(String phone, String smsCode) {
        //1. 判断手机号和验证码不为空
        if (phone == null || "".equals(phone) || smsCode == null || "".equals(smsCode)) {
            return false;
        }
        //2. 根据手机号到redis中获取我们保存的验证码
        String redisSmsCode = (String)redisTemplate.boundValueOps(phone).get();
        //3. 判断使用手机号是否能够取出验证码
        if (redisSmsCode == null || "".equals(redisSmsCode)) {
            return false;
        }
        //4. 判断我们的验证码和页面传入的验证码是否一致
        if (!smsCode.equals(redisSmsCode)) {
            return false;
        }
        return true;
    }

    @Override
    public void add(User user) {
        userDao.insertSelective(user);
    }

    public static void main(String[] args) {
        long s = (long)(Math.random() * 1000000);
        System.out.println("=======" + s);
    }


    /**
     * 根据登录用户查询登录用户信息
     * @param userName
     * @return
     */
    @Override
    public User findUser(String userName) {

        UserQuery userQuery = new UserQuery();
        UserQuery.Criteria criteria = userQuery.createCriteria();
        criteria.andUsernameEqualTo(userName);
        List<User> userList = userDao.selectByExample(userQuery);

        if(userList!=null && userList.size()>0){
            return userList.get(0);
        }else {
            return new User();
        }

    }

    /**
     * 个人信息修改
     * @param user
     */
    @Override
    public void updateUser(User user) {


        //新建user对象,根据网页传入过来的值,对新建的user重新赋值更新到数据库
        //这样重新赋值部分的话,不会影响数据库user的创建时间和其它的值
        User dbUser = findUser(user.getUsername());

        dbUser.setNickName(user.getNickName());
        dbUser.setSex(user.getSex());
        dbUser.setBirthday(user.getBirthday());
        //个人信息修改时间
        dbUser.setUpdated(new Date());

        //创建查询对象
        UserQuery userQuery = new UserQuery();
        //创建查询条件
        UserQuery.Criteria criteria = userQuery.createCriteria();
        criteria.andUsernameEqualTo(user.getUsername());
        //更新保存个人信息
        userDao.updateByExample(dbUser,userQuery);

    }


    //新增收货人信息
   /* @Override
    public void addUserAddress(Address address) {

        addressDao.insertSelective(address);

    }*/




    public List<Order> findAll(String userName) {
        return null;
    }


}
