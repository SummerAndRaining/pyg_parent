//首页控制器
app.controller('collectController',function($scope,$controller,loginService,collectService){
    // AngularJS中的继承:伪继承
    $controller('baseController',{$scope:$scope});
    //显示当前用户名
	$scope.showName=function(){
			loginService.showName().success(
					function(response){
						$scope.loginName=response.loginName;
					}
			);
	}


	//查询收藏购物车的所有
    $scope.findAll=function(){
        collectService.findAll().success(
            function(response){
                $scope.list=response;
            }
        );
    }


});



