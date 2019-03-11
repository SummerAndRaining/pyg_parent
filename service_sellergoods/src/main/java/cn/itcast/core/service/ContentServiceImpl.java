package cn.itcast.core.service;

import cn.itcast.core.common.Constants;
import cn.itcast.core.dao.ad.ContentDao;
import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.pojo.ad.Content;
import cn.itcast.core.pojo.ad.ContentQuery;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.item.ItemCat;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ContentServiceImpl implements  ContentService{

    @Autowired
    private ContentDao contentDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ItemCatDao itemCatDao;

    @Override
    public PageResult search(Integer page, Integer rows, Content content) {
        PageHelper.startPage(page, rows);
        Page<Content> contentList = (Page<Content>) contentDao.selectByExample(null);
        return new PageResult(contentList.getTotal(), contentList.getResult());
    }

    @Override
    public void add(Content content) {
        //1. 添加广告数据到数据库
        contentDao.insertSelective(content);
        //2. 删除redis中对应的广告集合数据, 根据分类删
        redisTemplate.boundHashOps(Constants.REDIS_CONTENT_LIST).delete(content.getCategoryId());

    }

    @Override
    public Content findOne(Long id) {
        return contentDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(Content content) {
        //1. 根据当前广告id, 查询数据库, 能够查询到数据库中保存的广告对象
        Content oldContent = findOne(content.getId());
        //2. 根据老的分类id, 删除redis中对应的广告集合数据
        redisTemplate.boundHashOps(Constants.REDIS_CONTENT_LIST).delete(oldContent.getCategoryId());
        //3. 根据页面传入的广告对象中, 新的分类id, 删除redis中对应的广告集合数据
        redisTemplate.boundHashOps(Constants.REDIS_CONTENT_LIST).delete(content.getCategoryId());
        //4. 将数据修改到数据库中保存
        contentDao.updateByPrimaryKeySelective(content);
    }

    @Override
    public void delete(Long[] ids) {
        if (ids != null) {
            for (Long id : ids) {
                //1. 根据广告id, 查询广告对象
                Content content = findOne(id);
                //2. 根据广告对象中的分类id, 删除redis中对应的广告集合数据
                redisTemplate.boundHashOps(Constants.REDIS_CONTENT_LIST).delete(content.getCategoryId());
                //3. 删除数据库中对应的广告数据
                contentDao.deleteByPrimaryKey(id);
            }
        }
    }

    @Override
    public List<Content> findByCategoryId(Long categoryId) {
        ContentQuery query = new ContentQuery();
        ContentQuery.Criteria criteria = query.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<Content> contents = contentDao.selectByExample(query);
        return contents;
    }

    @Override
    public List<Content> findByCategoryIdFromRedis(Long categoryId) {
        //1. 从redis中获取广告集合数据
        List<Content> contentList = (List<Content>)redisTemplate.boundHashOps(Constants.REDIS_CONTENT_LIST).get(categoryId);
        //2. 判断是否能够获取到广告集合数据
        if (contentList != null && contentList.size() > 0) {
            //3. 如果获取到则直接返回
            return contentList;
        } else {
            //4. 如果获取不到则从数据库中获取, 然后存入redis中一份, 下回就可以直接从redis中获取数据
            contentList = findByCategoryId(categoryId);
            redisTemplate.boundHashOps(Constants.REDIS_CONTENT_LIST).put(categoryId, contentList);
            return contentList;
        }

    }

    //楼层广告展示
    @Override
    public List<ItemCat> findItemCatList() {
        List<ItemCat> itemCatList = (List<ItemCat>) redisTemplate.boundHashOps("itemCat").get("indexItemCat");
        if (itemCatList==null){
            List<ItemCat> itemCatList1 = itemCatDao.findItemCatListByParentId(0L);
            for (ItemCat itemCat1 : itemCatList1) {
                List<ItemCat> itemCatList2 = itemCatDao.findItemCatListByParentId(itemCat1.getId());
                for (ItemCat itemCat2 : itemCatList2) {
                    List<ItemCat> itemCatList3 = itemCatDao.findItemCatListByParentId(itemCat2.getId());
                    itemCat2.setItemCatList(itemCatList3);
                }
                itemCat1.setItemCatList(itemCatList2);
            }
            redisTemplate.boundHashOps("itemCat").put("indexItemCat",itemCatList1);
            return itemCatList1;
        }
        return  itemCatList;
    }
}
