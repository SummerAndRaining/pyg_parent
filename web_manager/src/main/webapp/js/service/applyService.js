// 定义服务层:
app.service("applyService", function ($http) {
    //页面显示未审核品牌列表
    this.findAllApplyBrand = function () {
        return $http.get('../brand/findAllApplyBrand.do');
    }
    //审核商家品牌
    this.updateBrand = function (entity) {
        return $http.get('../brand/updateBrand.do?entity='+entity);
    }

    //读取规格列表数据绑定到表单中
    this.findRedisSpecList=function(){
        return $http.get('../specification/findAll.do');
    }
    this.updateStatusSpecification = function(names){
        return $http.get('../specification/updateStatusSpecification.do?names='+names);
    }

    //读取模板列表数据绑定到表单中
    this.findAllRedisItemCat=function(){
        return $http.get('../itemCat/findAllRedisItemCat.do');
    }

    //读取模板列表数据绑定到表单中
    this.findRedisTemplateList=function(){
        return $http.get('../typeTemplate/findAll.do');
    }
    this.updateStatus = function(names){
        return $http.get('../typeTemplate/updateStatus.do?names='+names);
    }

});