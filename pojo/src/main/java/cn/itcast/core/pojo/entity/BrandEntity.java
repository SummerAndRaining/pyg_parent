package cn.itcast.core.pojo.entity;

import cn.itcast.core.pojo.good.Brand;

import java.io.Serializable;

/*
组装Brand品牌和商家名
 */
public class BrandEntity implements Serializable {
    //商家名
    private String userName;
    //品牌
    private String brandName;
    //品牌首字母
    private String firstChar;

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
