// 定义服务层:
app.service("brandService", function ($http) {
    this.findAll = function () {
        return $http.get("../brand/findAll.do");
    }

    this.findByPage = function (page, rows) {
        return $http.get("../brand/findByPage.do?page=" + page + "&rows=" + rows);
    }

    this.save = function (entity) {
        return $http.post("../brand/add.do", entity);
    }

    this.update = function (entity) {
        return $http.post("../brand/update.do", entity);
    }

    this.findById = function (id) {
        return $http.get("../brand/findOne.do?id=" + id);
    }

    this.dele = function (ids) {
        return $http.get("../brand/delete.do?ids=" + ids);
    }

    this.search = function (page, rows, searchEntity) {
        return $http.post("../brand/search.do?page=" + page + "&rows=" + rows, searchEntity);
    }

    this.selectOptionList = function () {
        return $http.get("../brand/selectOptionList.do");
    }

    this.uploadFile = function () {
        // 向后台传递数据:
        var form = new FormData();
        // 向formData中添加数据:
        var file = document.getElementById("file").files[0];
        form.append('file', file);

        $http({
            method: 'post',
            url: '../upload01/uploadFile01.do',
            data: form,
            headers: {'Content-Type': undefined},// Content-Type : text/html  text/plain
            transformRequest: angular.identity
        })
    }
});