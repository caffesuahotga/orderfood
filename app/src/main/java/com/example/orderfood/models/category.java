package com.example.orderfood.models;

public class category {
    private int imageResource;
    private String name;


    public category(int imageResource, String name ) {
        this.imageResource = imageResource;
        this.name = name;

    }

    public int getImageResource() {
        return imageResource;
    }

    public String getName() {
        return name;
    }


}
