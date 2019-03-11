// 定义控制器:
app.controller("brandController", function ($scope, $controller, $http, brandService) {
    // AngularJS中的继承:伪继承
    $controller('baseController', {$scope: $scope});

    //读取列表数据绑定到表单中
    $scope.findAll=function(){
        brandService.findAll().success(
            function(response){
                $scope.list=response;
            }
        );
    }

    // $scope.entity = {};
    // 申请品牌的方法:
    $scope.save = function () {
        // 区分是保存还是修改
        var object;
        if ($scope.entity.id != null) {
            // 更新
            object = brandService.update($scope.entity);
        } else {
            // 保存
            object = brandService.save($scope.entity);
        }
        object.success(function (response) {
            // {success:true,message:xxx}
            // 判断保存是否成功:
            if (response.success == true) {
                // 保存成功
                alert(response.message);
                location="http://localhost:8082/admin/brand.html";

            } else {
                // 保存失败
                alert(response.message);
            }
        });
    }


});
