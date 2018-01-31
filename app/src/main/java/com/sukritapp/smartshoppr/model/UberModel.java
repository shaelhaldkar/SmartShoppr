package com.sukritapp.smartshoppr.model;

/**
 * Created by abc on 9/16/2017.
 */

public class UberModel {
    String productImage;
    String productId;
    String productName;
    String productPickInTime;
    String productFare;
    String productFareId;

    public String getProductFareId() {
        return productFareId;
    }

    public void setProductFareId(String productFareId) {
        this.productFareId = productFareId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPickInTime() {
        return productPickInTime;
    }

    public void setProductPickInTime(String productPickInTime) {
        this.productPickInTime = productPickInTime;
    }

    public String getProductFare() {
        return productFare;
    }

    public void setProductFare(String productFare) {
        this.productFare = productFare;
    }

    @Override
    public String toString() {
        return "productId : " + productId +"productFareId : " + productFareId + ",productImage : " + productImage + ",productName : " + productName + ",productPickInTime :" + productPickInTime + ",productFare" + productFare;
    }
}
