 //控制层 
app.controller('orderController' ,function($scope,$controller,orderService){
	
	$controller('baseController',{$scope:$scope});//继承


    //读取列表数据绑定到表单中  
	$scope.findOrderFromSeller=function(){
		orderService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}
    // 定义一个数组:
     $scope.selectIds = [];
    // 更新复选框：
    $scope.updateSelection = function($event,id){
        // 复选框选中
        if($event.target.checked){
            // 向数组中添加元素
            $scope.selectIds.push(id);
        }else{
            // 从数组中移除
            var idx = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(idx,1);
        }
    }

    //发货的方法
    $scope.sendGood=function(orderId){
        alert(orderId);
        orderService.sendGood(orderId).success(
            function(response){
                if(response.success){
                    location="http://localhost:8082/admin/order-queryAndAddress.html";
                }else{
                    alert(response.message);
                }

            }
        );
    }

    //杨涛-------------------------杨涛-----------------------商家后台-订单查询
});	
