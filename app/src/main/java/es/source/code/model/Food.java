package es.source.code.model;

import java.io.Serializable;

/**
 * Created by pengming on 2017/10/13.
 */

public class Food implements Serializable{
    private String foodname;
    private String foodprice;
    private int imageurl;
    private String comment;
    private int number;
    private Boolean isOrdered;

    public Food(String foodname, String foodprice, int imageurl, String comment, int number, Boolean isOrdered){
        this.foodname = foodname;
        this.foodprice = foodprice;
        this.imageurl = imageurl;
        this.comment = comment;
        this.number = number;
        this.isOrdered = isOrdered;
    }

    public Boolean getIsOrdered() {
        return isOrdered;
    }

    public void setIsOrdered(Boolean ordered) {
        isOrdered = ordered;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getFoodprice() {
        return foodprice;
    }

    public void setFoodprice(String foodprice) {
        this.foodprice = foodprice;
    }

    public int getImageurl() {
        return imageurl;
    }

    public void setImageurl(int imageurl) {
        this.imageurl = imageurl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
