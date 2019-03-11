//服务层
app.service('allUserService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../allUser/findAll.do');
	}
	//分页 
	this.findPage=function(page,rows){
		return $http.get('../allUser/findPage.do?page='+page+'&rows='+rows);
	}
	//查询实体
	this.findOne=function(id){
		return $http.get('../allUser/findOne.do?id='+id);
	}
	//增加 
	this.add=function(entity){
		return  $http.post('../allUser/add.do',entity );
	}
	//修改 
	this.update=function(entity){
		return  $http.post('../allUser/update.do',entity );
	}
	//删除
	// this.dele=function(ids){
	// 	return $http.get('../allUser/delete.do?ids='+ids);
	// }
	//搜索
	this.search=function(page,rows,searchEntity){
		return $http.post('../allUser/search.do?page='+page+"&rows="+rows, searchEntity);
	}
	//冻结
    this.cold=function(id){
        return $http.post('../allUser/cold.do?id='+id);
    }
    //冻结
    this.unCold=function(id){
        return $http.post('../allUser/unCold.do?id='+id);
    }
    // //统计活跃度
    // this.countUserActive=function(){
    //     return $http.post('../allUser/countUserActive.do?');
    // }

    //统计活跃度2
    this.dele=function(){
        return $http.get('../allUser/countUserActive.do');
    }
});
