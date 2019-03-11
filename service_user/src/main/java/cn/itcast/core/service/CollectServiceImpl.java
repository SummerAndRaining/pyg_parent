package cn.itcast.core.service;

import cn.itcast.core.common.Constants;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.pojo.item.Item;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CollectServiceImpl implements CollectService {
    @Autowired
    private ItemDao itemDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Item> findAll(String userName) {

//        List<Item> itemList = new ArrayList<>();

        //根据当前登录用户从redis中取出收藏的商品集合

        List<Item> itemList = (List<Item>) redisTemplate.boundHashOps(Constants.REDIS_LIKE_LIST).get(userName);
//        //判断
//        if (itemList != null) {
////            for (Item redisItem : itemList) {
////                Item item = itemDao.selectByPrimaryKey(redisItem.getId());
////                itemList.add(item);
////            }
//
//        }
        return itemList;

    }
}
