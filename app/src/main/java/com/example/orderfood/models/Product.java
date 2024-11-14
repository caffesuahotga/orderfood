package com.example.orderfood.models;

import java.util.ArrayList;

public class Product {

    private int Id;

    private String name;
    private int image_source;
    private ArrayList<String> Image; // danh sách ảnh món ăn n
    private String price;
    private Double rate;
    private int minutes; // thời gian làm món ăn
    private String Description;

    private int StoreID;


    public int getId() {
        return Id;
    }

    public Product() {
    }

    public Product(int id, String name, int image_source, ArrayList<String> image, String price, Double rate, int minutes, String description, int storeID) {
        Id = id;
        this.name = name;
        this.image_source = image_source;
        Image = image;
        this.price = price;
        this.rate = rate;
        this.minutes = minutes;
        Description = description;
        StoreID = storeID;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage_source() {
        return image_source;
    }

    public void setImage_source(int image_source) {
        this.image_source = image_source;
    }

    public ArrayList<String> getImage() {
        return Image;
    }

    public void setImage(ArrayList<String> image) {
        Image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getStoreID() {
        return StoreID;
    }

    public void setStoreID(int storeID) {
        StoreID = storeID;
    }
}
