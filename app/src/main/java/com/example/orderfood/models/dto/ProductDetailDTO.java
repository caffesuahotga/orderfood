package com.example.orderfood.models.dto;

import java.util.ArrayList;

public class ProductDetailDTO {
    private int PID;
    private String Name ;
    private String Description ;
    private int Star;
    private int Min;
    private ArrayList<String> ListImage = new ArrayList<>(); // ảnh sản phẩm

    public ProductDetailDTO() {
    }

    public ProductDetailDTO(int PID, String name, String description, int star, int min, ArrayList<String> listImage) {
        this.PID = PID;
        Name = name;
        Description = description;
        Star = star;
        Min = min;
        ListImage = listImage;
    }

    public int getPID() {
        return PID;
    }

    public void setPID(int PID) {
        this.PID = PID;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setStar(int star) {
        Star = star;
    }

    public void setMin(int min) {
        Min = min;
    }

    public void setListImage(ArrayList<String> listImage) {
        ListImage = listImage;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public int getStar() {
        return Star;
    }

    public int getMin() {
        return Min;
    }

    public ArrayList<String> getListImage() {
        return ListImage;
    }
}

