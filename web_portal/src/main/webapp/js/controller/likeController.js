//关注购物车控制层
app.controller('likeController',function($scope,cartService) {
    //购物车移到关注
    $scope.addGoodsToLikeFromCart = function (itemId) {
        alert("移入关注后您可以在我的关注中查看!");
        cartService.addGoodsToLikeFromCart(itemId).success(
            function (response) {
                if (response.success) {//如果成功
                        location = "http://localhost:8080/cart.html";
                } else {
                    alert(response.message);
                    location = "http://localhost:8080/login.html";//跳转到登录页面
                }
            }
        )

    }

}