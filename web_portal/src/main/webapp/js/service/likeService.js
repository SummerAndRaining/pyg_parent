//购物车服务层
app.service('likeService',function($http) {

    //添加关注
    this.addGoodsToLikeFromCart = function (itemId) {
        return $http.get('cart/addGoodsToLikeFromCart.do?itemId=' + itemId);
    }
}