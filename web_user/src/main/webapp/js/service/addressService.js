//服务层
app.service('addressService',function($http){
	    	
	//根据登录用户查询收货地址
	this.findAddressList=function(){
		return $http.get('../address/findAddressList.do');
	}

    //获取地区
    this.findByParentId = function(parentId){
        return $http.get("../address/findByParentId.do?parentId="+parentId);
    }



	
});
