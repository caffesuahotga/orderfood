package com.example.orderfood.models.dto;

import java.io.Serializable;

public class FavoriteDTO implements Serializable {
    private int ID;
    private int productID;
    private String name;
    private double price;
    private String image;
    private double rate;
    private String description;

    public FavoriteDTO() {
    }

    public FavoriteDTO(int ID,int productID, String name, double price, String image, double rate, String description) {
        this.ID = ID;
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.image = image;
        this.rate = rate;
        this.description = description;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
