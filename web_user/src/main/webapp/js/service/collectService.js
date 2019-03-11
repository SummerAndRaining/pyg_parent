//服务层
app.service('collectService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../collect/findAll.do');
	}

	
});
