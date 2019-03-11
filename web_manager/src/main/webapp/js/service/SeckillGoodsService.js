//服务层
app.service('SeckillGoodsService',function($http){
	    	
	/*//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../SeckillGoodsOrder/findAll.do');
	}
	//分页 
	this.findPage=function(page,rows){
		return $http.get('../SeckillGoodsOrder/findPage.do?page='+page+'&rows='+rows);
	}
	//查询实体
	this.findOne=function(id){
		return $http.get('../SeckillGoodsOrder/findOne.do?id='+id);
	}
	//增加 
	this.add=function(entity){
		return  $http.post('../SeckillGoodsOrder/add.do',entity );
	}
	//修改 
	this.update=function(entity){
		return  $http.post('../SeckillGoodsOrder/update.do',entity );
	}
	//删除
	this.dele=function(ids){
		return $http.get('../SeckillGoodsOrder/delete.do?ids='+ids);
	}*/
	//搜索
	this.search=function(page,rows,searchEntity){
		return $http.post('../SeckillGoodsOrder/search.do?page='+page+"&rows="+rows, searchEntity);
	}

    //分页
    this.findPage=function(page,rows){
        return $http.get('../SeckillGoodsOrder/findPage.do?page='+page+'&rows='+rows);
    }

    //查询名字
    this.findName=function(){
        return $http.get('../SeckillGoodsOrder/findName.do');
    }


    //查询商品名字
    this.findAllGoods=function(){
        return $http.get('../SeckillGoodsOrder/findAllGoods.do');
    }


    //查询库存集合
    this.finditemname=function(){
        return $http.get('../SeckillGoodsOrder/finditemname.do');
    }


    //审核商品
	this.updateStatus = function(ids,status){
		return $http.get('../SeckillGoodsOrder/updateStatus.do?ids='+ids+"&status="+status);
	}
});
