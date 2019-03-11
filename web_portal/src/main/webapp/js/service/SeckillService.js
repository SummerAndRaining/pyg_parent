app.service("SeckillServicer",function($http){

    this.findAll = function(){
        return $http.get("Seckill/findAll.do");
    }
});