package es.source.code.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by pengming on 2017/10/13.
 */

public class Food extends BmobObject implements Serializable{
    private String foodName;
    private Integer foodPrice;
    private String imageUrl;
    private String comment;
    private String kind;
    private Integer number;
    private Integer orderedNumber;
    private Boolean isOrdered;

    public Food(){

    }

    public Food(String foodName, Integer foodPrice, String imageUrl, String comment, String kind, Integer number, Integer orderedNumber, Boolean isOrdered){
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.imageUrl = imageUrl;
        this.comment = comment;
        this.kind = kind;
        this.number = number;
        this.orderedNumber = orderedNumber;
        this.isOrdered = isOrdered;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Integer getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(Integer foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getOrderedNumber() {
        return orderedNumber;
    }

    public void setOrderedNumber(Integer orderedNumber) {
        this.orderedNumber = orderedNumber;
    }

    public Boolean getIsOrdered() {
        return isOrdered;
    }

    public void setIsOrdered(Boolean ordered) {
        isOrdered = ordered;
    }
}
