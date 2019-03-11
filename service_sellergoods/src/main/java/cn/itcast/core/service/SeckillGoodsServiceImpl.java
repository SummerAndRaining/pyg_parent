package cn.itcast.core.service;

import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.good.GoodsQuery;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillGoodsQuery;
import cn.itcast.core.pojo.seller.Seller;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商家秒杀商品申请
 */
@Service
@Transactional
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

    @Autowired
    private SellerDao sellerDao;

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private SeckillGoodsDao seckillGoodsDao;

    /**
     * 1. 获取商家用户名
     *
     * @return
     */
    @Override
    public Seller findName(String userName) {
        //1.获取当前登陆名
        Seller seller = sellerDao.selectByPrimaryKey(userName);
        //2.将当前登陆用户名返回
        return seller;
    }

    /**
     * 2. 根据商家用户名搜索商家拥有的商品
     *
     * @param name
     * @return
     */
    @Override
    public List<Goods> findByParentId(String name) {
        //1. 创建条件对象
        GoodsQuery query = new GoodsQuery();
        //2. 创建条件查询对象
        GoodsQuery.Criteria criteria = query.createCriteria();
        //3. 添加条件
        criteria.andSellerIdEqualTo(name);
        //4. 根据条件查询商品
        List<Goods> goods = goodsDao.selectByExample(query);
        return goods;
    }

    /**
     * 3. 根据商品id查询库存集合
     *
     * @param id 商品id
     * @return
     */
    @Override
    public List<Item> findByParent(Long id) {
        //1.创建查询条件
        ItemQuery query = new ItemQuery();
        //2.创建查询条件对象
        ItemQuery.Criteria criteria = query.createCriteria();
        //3.向查询条件对象添加条件
        criteria.andGoodsIdEqualTo(id);
        //4.查询库存集合
        return itemDao.selectByExample(query);
    }

    /**
     * 4. 根据库存集合id查询价格
     *
     * @param id
     * @return
     */
    @Override
    public BigDecimal findPriceOld(Long id) {
        Item item = itemDao.selectByPrimaryKey(id);
        BigDecimal price = item.getPrice();
        return price;
    }

    /**
     * 5. 添加秒杀申请
     *
     * @param seckillGoods 商品秒杀申请数据
     */
    @Override
    public void add(SeckillGoods seckillGoods) {
        //1. 初始化数据
        //1.1添加申请日期
        seckillGoods.setCreateTime(new Date());
        //1.2添加审核状态 0未审核,1已审核
        seckillGoods.setStatus("0");
        //1.3添加秒杀商品数,初始化0
        seckillGoods.setNum(0);

        //2.调用方法添加数据库
        seckillGoodsDao.insertSelective(seckillGoods);
    }

    /**
     * 6. 运营商查询所有未审核商品
     *
     * @param page
     * @param rows
     * @param
     * @return
     */
    @Override
    public PageResult search(Integer page, Integer rows, SeckillGoods title) {
        //1.创建查询条件对象
        SeckillGoodsQuery query = new SeckillGoodsQuery();
        //2.创建查询条件
        SeckillGoodsQuery.Criteria criteria = query.createCriteria();
        //3.向查询条件中添加条件
        if (title != null && !"".equals(title)) {
            if (title.getTitle() != null && !"".equals(title.getTitle())) {
                criteria.andTitleLike(title.getTitle());
            }
        }
        //4.向查询条件中添加只查询未审核商品条件
        criteria.andStatusEqualTo("0");
        //5.查询
        PageHelper.startPage(page, rows);
        Page<SeckillGoods> seckillGoods = (Page<SeckillGoods>) seckillGoodsDao.selectByExample(query);
        return new PageResult(seckillGoods.getTotal(), seckillGoods.getResult());
    }

    /**
     * 7. 查询所有商户名字
     * @return
     */
    @Override
    public List<Seller> findAllName() {
        List<Seller> sellers = sellerDao.selectByExample(null);
        return sellers;
    }

    /**
     * 8. 查询所有商品名字
     * @return
     */
    @Override
    public List<Goods> findAllGoods() {
        List<Goods> goods = goodsDao.selectByExample(null);
        return goods;
    }

    /**
     * 9. 查询所有库存集合名字
     * @return
     */
    @Override
    public List<Item> finditemname() {
        return itemDao.selectByExample(null);
    }

    /**
     * 10. 根据秒杀商品id改变数据库中秒杀商品状态
     * @param ids
     * @param status
     */
    @Override
    public void updateStatus(Long[] ids, String status) {
        //1. 遍历审核商品ids数组
        for (Long id : ids) {
            //2. 创建更改对象
            SeckillGoods seckillGoods = new SeckillGoods();
            //3. 向对象中添加条件
            seckillGoods.setId(id);
            seckillGoods.setStatus(status);
            seckillGoods.setCheckTime(new Date());
            //2. 调用持久层将未审核状态,变为已审核状态
            seckillGoodsDao.updateByPrimaryKeySelective(seckillGoods);
        }
    }
}
