//首页控制器
app.controller('addressController',function($scope,$controller,loginService,addressService,uploadService){
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

	//获取当前用户的地址列表
    $scope.findAddressList=function(){
        addressService.findAddressList().success(
            function(response){
                $scope.addressList=response;
            }
        );
    }

    //图片上传

    $scope.uploadFile = function(){
        // 调用uploadService的方法完成文件的上传
        uploadService.uploadFile().success(function(response){
            if(response.success){
                // 获得url
                $scope.image_entity.url =  response.message;
            }else{
                alert(response.message);
            }
        });
    }



    // 查询一级分类列表:
    $scope.selectProvincesList = function(){
        addressService.findProvincesList.success(function(response){
            $scope.ProvincesList = response;
        });
    }

    // 查询二级分类列表:
    $scope.$watch("entity.address.provinceId",function(newValue,oldValue){
        addressService.findByProvinceId(newValue).success(function(response){
            $scope.cityList = response;
        });
    });

    // 查询三级分类列表:
    $scope.$watch("entity.address.cityId",function(newValue,oldValue){
        addressService.findcityId(newValue).success(function(response){
            $scope.areasList = response;
        });
    });



});



