// 定义控制器:
app.controller("applyController", function ($scope, $controller, $http, applyService) {
    // AngularJS中的继承:伪继承
    $controller('baseController', {$scope: $scope});

    //页面显示未审核品牌列表
    $scope.findAllApplyBrand = function () {
        // 向后台发送请求获取数据:
        applyService.findAllApplyBrand().success(function (response) {
            // alert(response);
            $scope.list = response;
        });
    }

    $scope.entity = {};//定义审核对象
    //审核商家品牌
    $scope.updateBrand = function () {
        applyService.updateBrand($scope.entity).success(function (response) {
            // {id:xx,name:yy,firstChar:zz}
            if (response.success) {
                //$scope.reloadList();//刷新列表
                // 保存成功
                location = "http://localhost:8081/admin/brand_1.html";
            } else {
                alert(response.message);
            }
        });
    }

    //读取规格列表数据绑定到表单中
    $scope.findRedisSpecList = function () {
        applyService.findRedisSpecList().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //审核商家规格
    $scope.updateStatusSpecification = function (status) {
        applyService.updateStatusSpecification($scope.selectNames).success(function (response) {
            if (response.success) {
                //$scope.reloadList();//刷新列表
                location = "http://localhost:8081/admin/specification_1.html";
                $scope.selectNames = [];
            } else {
                alert(response.message);
            }
        });
    }

    //读取分类列表数据绑定到表单中
    $scope.findAllRedisItemCat = function () {
        applyService.findAllRedisItemCat().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //读取模板列表数据绑定到表单中
    $scope.findRedisTemplateList = function () {
        applyService.findRedisTemplateList().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    // 审核商家模板:
    $scope.updateStatus = function (status) {
        applyService.updateStatus($scope.selectNames).success(function (response) {
            if (response.success) {
                //$scope.reloadList();//刷新列表
                location = "http://localhost:8081/admin/type_template_1.html";
                $scope.selectNames = [];
            } else {
                alert(response.message);
            }
        });
    }

    $scope.brandList = {data: []}
    //$scope.brandList={data:[{id:1,text:'联想'},{id:2,text:'华为'},{id:3,text:'小米'}]};//品牌列表
    // 查询关联的品牌信息:
    $scope.findBrandList = function () {
        brandService.selectOptionList().success(function (response) {
            $scope.brandList = {data: response};
        });
    }

    $scope.specList = {data: []}
    // 查询关联的品牌信息:
    $scope.findSpecList = function () {
        specificationService.selectOptionList().success(function (response) {
            $scope.specList = {data: response};
        });
    }

    //给扩展属性添加行
    $scope.entity = {customAttributeItems: []};
    $scope.addTableRow = function () {
        $scope.entity.customAttributeItems.push({});
    }

    $scope.deleteTableRow = function (index) {
        $scope.entity.customAttributeItems.splice(index, 1);
    }
});
