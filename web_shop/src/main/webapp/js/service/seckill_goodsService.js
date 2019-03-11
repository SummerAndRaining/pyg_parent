//服务层
app.service('seckill_goodsService', function ($http) {


    //查询商家名称
    this.findName = function () {
        return $http.post('../seckillgoods/findName.do');
    }

    //查询商品
    this.findByParentId = function () {
        return $http.post('../seckillgoods/findByParentId.do');
    }

    //查询库存集合
    this.findByParent = function (id) {
        return $http.post('../seckillgoods/findByParent.do?id=' + id);
    }

    //查询价格
    this.findPriceOld = function (id) {
        return $http.post('../seckillgoods/findPriceOld.do?id=' + id);
    }

    //增加
    this.add = function (seckillMap) {
        return $http.post('../seckillgoods/add.do', seckillMap);
    }
    //修改
    this.update = function (entity) {
        return $http.post('../seckillgoods/update.do', entity);
    }
});
