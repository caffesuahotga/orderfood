package com.example.orderfood.models;

public class product {


    private String name;
    private int image_source;
    private String price;
    private Double rate;

    // Constructor
    public product(String name, int image_source, String price, Double rate) {

        this.name = name;
        this.image_source = image_source;
        this.price = price;
        this.rate = rate;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageSource() {
        return image_source;
    }

    public void setImageSource(int image_source) {
        this.image_source = image_source;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Double getRate(){ return rate;}

    public void setRate(Double rate){this.rate = rate;}
}
