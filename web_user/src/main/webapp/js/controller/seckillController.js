//首页控制器
app.controller('seckillController',function($scope,$controller,seckillService,loginService){
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


	//查询秒杀订单列表
    $scope.findPageAllSeckill=function(page,rows){
        seckillService.findPageAllSeckill(page,rows).success(
            function(response){
                console.log(response);
                $scope.list=response.rows;
            }
        );
    }

    //取消秒杀

    $scope.update = function(id){
        seckillService.update(id).success(function(response){
            // 判断保存是否成功:
            if(response.success==true){
                // 保存成功
                // alert(response.message);
                findPageAllSeckill(1,10);
            }else{
                // 保存失败
                alert(response.message);
            }
        });
    }


});



