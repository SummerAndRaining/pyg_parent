package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.entity.SpecEntity;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.service.SellerService;
import cn.itcast.core.service.SpecService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 规格管理
 */
@RestController
@RequestMapping("/specification")
public class SpecController {

    @Reference
    private SpecService specService;

    @Reference
    private SellerService sellerService;

    /**
     * 高级查询
     *
     * @param page 当前页
     * @param rows 每页查询多少条数据
     * @param spec 查询条件对象
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows, @RequestBody Specification spec) {
        PageResult pageResult = specService.findPage(page, rows, spec);
        return pageResult;
    }

    @RequestMapping("/add")
    public Result add(@RequestBody SpecEntity specEntity) {
        try {
            specService.add(specEntity);
            return new Result(true, "添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败!");
        }
    }

    @RequestMapping("/findOne")
    public SpecEntity findOne(Long id) {
        SpecEntity one = specService.findOne(id);
        return one;
    }

    @RequestMapping("/update")
    public Result update(@RequestBody SpecEntity specEntity) {
        try {
            specService.update(specEntity);
            return new Result(true, "修改成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败!");
        }
    }

    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            specService.delete(ids);
            return new Result(true, "删除成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败!");
        }
    }

    /**
     * 模板下拉select2使用
     *
     * @return
     */
    @RequestMapping("/selectOptionList")
    public List<Map> selectOptionList() {
        return specService.selectOptionList();
    }

    //显示未审核通过规格实体集合数据
    @RequestMapping("/findAll")
    public List<Specification> findAll() {
        List<Seller> sellerList = sellerService.findAllSeller();
        List<SpecEntity> allShopSpecList = specService.findAllShopSpecFromRedis();
        List<Specification> specificationList = new ArrayList<>();
        if (allShopSpecList != null && allShopSpecList.size() > 0) {
            for (SpecEntity specEntity : allShopSpecList) {
                Specification specification = specEntity.getSpecification();
                specificationList.add(specification);
            }
        }
        return specificationList;
    }

    //审核商家规格
    @RequestMapping("/updateStatusSpecification")
    public Result updateStatusSpecification(String[] names) {
        specService.updateStatusSpecification(names);
        return new Result(true, "商家规格审核通过!");
    }
}
