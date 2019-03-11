 //控制层 
app.controller('seckillOrdersController',function($scope,$controller,$location,seckillOrdersService){
	

	// //搜索
	 $scope.search=function(page,rows){
         ordersService.search(page,rows,$scope.searchEntity).success(
	 		function(response){
	 			$scope.list=response.rows;
	 			$scope.paginationConf.totalItems=response.total;//更新总记录数
	 		}
		);
	}

    //查询所有
    $scope.findAll=function(){
        seckillOrdersService.findAll().success(
            function(response){
                $scope.list=response;
            }
        );
    }



});	
