 //控制层 
app.controller('allUserController' ,function($scope,$controller   ,allUserService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
        allUserService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){
        allUserService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){
        allUserService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=allUserService.update( $scope.entity ); //修改
		}else{
			serviceObject=allUserService.add( $scope.entity  );//增加
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	// //批量删除
	// $scope.dele=function(){
	// 	//获取选中的复选框
     //    allUserService.dele( $scope.selectIds ).success(
	// 		function(response){
	// 			if(response.success){
	// 				$scope.reloadList();//刷新列表
	// 				$scope.selectIds = [];
	// 			}
	// 		}
	// 	);
	// }
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){
        allUserService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	//冻结
    $scope.cold=function(id){
        allUserService.cold(id).success(
            function(response){
                if (response.success){
                	alert(response.message);
                	location="http://localhost:8081/admin/allUser1.html"
				}else {
                    alert(response.message);
                }
            }
        );
    }
    //解冻
    $scope.unCold=function(id){
        allUserService.unCold(id).success(
            function(response){
                if (response.success){
                    alert(response.message);
                    location="http://localhost:8081/admin/allUser1.html"
                }else {
                    alert(response.message);
                }
            }
        );
    }
    // //统计用户活跃度
    // $scope.countUserActive=function(){
    //     allUserService.countUserActive().success(
    //         function(response){
    //             $scope.activeList=response;
    //         }
    //     );
    // }
    //统计用户活跃度 2
    $scope.dele=function(){
        //获取选中的复选框
        allUserService.dele( ).success(
            function(response){
                $scope.activeList=response;
            }
        );
    }
});	
