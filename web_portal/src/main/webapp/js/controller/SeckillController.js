app.controller("SeckillController",function($scope,SeckillServicer){
	// $scope.contentList = [];

    // 查询所有的秒杀列表的方法:
    $scope.findAll = function(){
        // 向后台发送请求:
        SeckillServicer.findAll().success(function(response){
            $scope.contentList = response;
        });
    }
	
});