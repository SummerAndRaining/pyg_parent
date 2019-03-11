//服务层
app.service('seckillOrdersService',function($http){
	    	

	//搜索
	 this.searchs=function(page,rows){
	 	return $http.get('../seckillOrder/search.do?page='+page+'&rows='+rows);
	 }

    //查询所有
    this.findAll=function(){
        return $http.get('../seckillOrder/findAll.do');
    }


});
