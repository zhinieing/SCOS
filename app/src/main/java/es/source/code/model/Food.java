package es.source.code.model;

/**
 * Created by pengming on 2017/10/13.
 */

public class Food {
    private String foodname;
    private String foodprice;
    private String imageurl;
    private String comments;
    private int number;

    public Food(String foodname, String foodprice, String imageurl, String comments, int number){
        this.foodname = foodname;
        this.foodprice = foodprice;
        this.imageurl = imageurl;
        this.comments = comments;
        this.number = number;
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

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
