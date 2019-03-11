package cn.itcast.core.service;

import cn.itcast.core.common.Constants;
import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemCatQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private ItemCatDao itemCatDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<ItemCat> findByParentId(Long parentId) {
        /**
         * 查询
         */
        ItemCatQuery query = new ItemCatQuery();
        ItemCatQuery.Criteria criteria = query.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<ItemCat> itemCats = itemCatDao.selectByExample(query);

        /**
         * 缓存所有分类数据到redis中, 供portal系统搜索使用
         */
        List<ItemCat> catList = itemCatDao.selectByExample(null);
        if (catList != null) {
            for (ItemCat itemCat : catList) {
                redisTemplate.boundHashOps(Constants.REDIS_CATEGORY).put(itemCat.getName(), itemCat.getTypeId());
            }
        }
        return itemCats;
    }

    @Override
    public ItemCat findOne(Long id) {
        return itemCatDao.selectByPrimaryKey(id);
    }

    @Override
    public List<ItemCat> findAll() {
        return itemCatDao.selectByExample(null);
    }

    @Override
    public void importExcel(String[][] strings) {
        for(int i=0;i<strings.length;i++){
            ItemCat itemCat = new ItemCat();
            itemCat.setParentId(Long.valueOf(strings[i][1]));
            itemCat.setName(strings[i][2]);
            itemCat.setTypeId(Long.valueOf(strings[i][3]));
            itemCatDao.insertSelective(itemCat);
            //brandDao.insertSelective(brand);
            //userService.save(user, null);//这是一个添加方法，dao层写入sql语句即可
        }
    }

    //从redis中获取当前用户所有未审核通过的分类数据
    @Override
    public List<ItemCat> getItemCatFromRedis(String userName) {
        List<ItemCat> itemCatList = (List<ItemCat>) redisTemplate.boundHashOps(Constants.APPLY_CATEGORY_LIST).get(userName);
        if (itemCatList == null) {
            itemCatList = new ArrayList<ItemCat>();
        }
        return itemCatList;
    }

    //根据用户名将分类集合存入到redis中
    @Override
    public void putItemCatListByUserName(String userName, List<ItemCat> itemCatList) {
        redisTemplate.boundHashOps(Constants.APPLY_CATEGORY_LIST).put(userName, itemCatList);
    }

    //商家分类申请
    @Override
    public void addItemToRedis(String userName, ItemCat itemCat) {
        List<ItemCat> itemCatList = getItemCatFromRedis(userName);
        itemCatList.add(itemCat);
        putItemCatListByUserName(userName, itemCatList);
    }

    //从redis中查询所有商家未审核通过的分类数据
    @Override
    public List<ItemCat> findAllApplyItemCatList() {
        Map<String,List<ItemCat>> allApplyItemCatMap = redisTemplate.boundHashOps(Constants.APPLY_CATEGORY_LIST).entries();
        Set<Map.Entry<String, List<ItemCat>>> entries = allApplyItemCatMap.entrySet();
        List<ItemCat> allApplyItemCatList = new ArrayList<>();

        if (entries != null && entries.size() > 0) {
            for (Map.Entry<String, List<ItemCat>> entry : entries) {
                String userName = entry.getKey();
                List<ItemCat> itemCatEntityList = entry.getValue();
                if (itemCatEntityList != null && itemCatEntityList.size() > 0) {
                    for (ItemCat itemCat : itemCatEntityList) {
                        allApplyItemCatList.add(itemCat);
                    }
                }
            }
        }
        return allApplyItemCatList;
    }
}
