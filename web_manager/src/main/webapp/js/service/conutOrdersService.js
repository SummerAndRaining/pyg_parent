// 定义服务层:
app.service("countOrdersService",function($http){
	this.findAll = function(){
		return $http.get("../countOrders/findAll.do");
	}
	
	this.findByPage = function(page,rows){
		return $http.get("../countOrders/findByPage.do?page="+page+"&rows="+rows);
	}
	
	this.save = function(entity){
		return $http.post("../countOrders/add.do",entity);
	}
	
	this.update=function(entity){
		return $http.post("../countOrders/update.do",entity);
	}
	
	this.findById=function(id){
		return $http.get("../countOrders/findOne.do?id="+id);
	}
	
	this.dele = function(ids){
		return $http.get("../countOrders/delete.do?ids="+ids);
	}
	
	this.search = function(page,rows,searchEntity){
		return $http.post("../countOrders/search.do?page="+page+"&rows="+rows,searchEntity);
	}
	
	this.selectOptionList = function(){
		return $http.get("../countOrders/selectOptionList.do");
	}
	this.findOrderCount=function(){
		return $http.get("../countOrders/countOrder.do");
	}
});