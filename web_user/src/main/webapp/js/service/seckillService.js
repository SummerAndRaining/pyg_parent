//服务层
app.service('seckillService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.findPageAllSeckill=function(page,rows){
		var obj =  $http.get('../seckill/findPageAllSeckill.do?page='+page+'&rows='+rows);
        console.log(obj);
        return obj;
	}


    //取消订单
    this.update=function(id){
        return $http.post('../seckill/update.do?id='+id);
    }

	
});
