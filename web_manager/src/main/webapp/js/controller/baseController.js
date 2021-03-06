app.controller("baseController", function ($scope) {
    // 分页的配置的信息
    $scope.paginationConf = {
        currentPage: 1, // 当前页数
        totalItems: 10, // 总记录数
        itemsPerPage: 10, // 每页显示多少条记录
        perPageOptions: [10, 20, 30, 40, 50],// 显示多少条下拉列表
        onChange: function () { // 当页码、每页显示多少条下拉列表发生变化的时候，自动触发了
            $scope.reloadList();// 重新加载列表
        }
    };

    $scope.reloadList = function () {
        // $scope.findByPage($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    }

    // 定义一个数组:
    $scope.selectIds = [];
    // 更新复选框：
    $scope.updateSelection = function ($event, id) {
        // 复选框选中
        if ($event.target.checked) {
            // 向数组中添加元素
            $scope.selectIds.push(id);
        } else {
            // 从数组中移除
            var idx = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(idx, 1);
        }

    }

    // 定义方法：获取JSON字符串中的某个key对应值的集合
    //jsonStr需要转换的json字符串,
    //key需要获取的json中的哪个key的值
    $scope.jsonToString = function (jsonStr, key) {
        // 将字符串转成JSOn:
        var jsonObj = JSON.parse(jsonStr);

        var value = "";
        for (var i = 0; i < jsonObj.length; i++) {

            if (i > 0) {
                value += ",";
            }
            //jsonObj[i]的值例如:{"id":1,"text":"联想"},
            //jsonObj[i][key]由于key传入的值是text所以就可以转换成jsonObj[i]['text'], 获取出来就是  "联想"
            value += jsonObj[i][key];
        }
        return value;
    }

    // 定义一个数组:
    $scope.selectEntitys = [];
    // 更新复选框：
    $scope.updateSelectionBrandEntity = function ($event, entity) {
        // 复选框选中
        if ($event.target.checked) {
            // 向数组中添加元素
            $scope.selectEntitys.push(entity);
        } else {
            // 从数组中移除
            var idx = $scope.selectEntitys.indexOf(entity);
            $scope.selectEntitys.splice(idx, 1);
        }

    // 定义一个数组:
    $scope.selectNames = [];
    // 更新复选框：
    $scope.updateSelectionName = function ($event, name) {
        // 复选框选中
        if ($event.target.checked) {
            // 向数组中添加元素
            $scope.selectNames.push(name);
        } else {
            // 从数组中移除
            var idx = $scope.selectNames.indexOf(name);
            $scope.selectNames.splice(idx, 1);
        }
    }



    }

});