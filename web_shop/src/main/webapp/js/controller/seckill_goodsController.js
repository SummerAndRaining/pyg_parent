 //控制层 
app.controller('seckill_goodsController' ,function($scope,$controller,$location,seckill_goodsService,uploadService){




    //读取列表数据获取用户名
    $scope.findName=function(){
        seckill_goodsService.findName().success(
            function(response){
                $scope.entity=response;
            }
        );
    }



    // 查询一级分类列表:
    $scope.selectItemCat1List = function(){
        seckill_goodsService.findByParentId().success(function(response){
            $scope.itemCat1List = response;
        });
    }

    // 监听查询
    $scope.$watch("entity.goodsId",function(newValue,oldValue){
        seckill_goodsService.findByParent(newValue).success(function(response){
            $scope.itemCat2List = response;
        });
    });

    // 查询价格:
    $scope.$watch("entity.itemId",function(newValue,oldValue){
        seckill_goodsService.findPriceOld(newValue).success(function(response){
            $scope.entity.price = response;
        });
    });


    //保存
    $scope.save=function(){

        seckill_goodsService.add( $scope.entity).success(
            function(response){
                if(response.success){
                    alert(response.message);
                    $scope.reloadList();//刷新列表
                }else{
                    alert(response.message);
                }
            }
        );
    }


    //上传图片
    $scope.uploadFile = function(){
        // 调用uploadService的方法完成文件的上传
        uploadService.uploadFile().success(function(response){
            if(response.success){
                // 获得url
                $scope.entity.smallPic =  response.message;
            }else{
                alert(response.message);
            }
        });
    }


});	
