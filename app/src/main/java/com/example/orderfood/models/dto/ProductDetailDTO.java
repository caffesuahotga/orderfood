package com.example.orderfood.models.dto;

import com.example.orderfood.models.FeedBack;

import java.util.ArrayList;

public class ProductDetailDTO {
    private int PID;
    private String Name ;
    private String Description ;
    private double Star;
    private double Min;
    private ArrayList<String> ListImage = new ArrayList<String>(); // ảnh sản phẩm
    private ArrayList<FeedBackDTO> ListFeedBack = new ArrayList<FeedBackDTO>();

    public ProductDetailDTO() {
    }

    public ProductDetailDTO(int PID, String name, String description, int star, int min, ArrayList<String> listImage, ArrayList<FeedBackDTO> listFeedBack) {
        this.PID = PID;
        Name = name;
        Description = description;
        Star = star;
        Min = min;
        ListImage = listImage;
        ListFeedBack = listFeedBack;
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

    public void setStar(double star) {
        Star = star;
    }

    public void setMin(double min) {
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

    public double getStar() {
        return Star;
    }

    public double getMin() {
        return Min;
    }

    public ArrayList<String> getListImage() {
        return ListImage;
    }

    public ArrayList<FeedBackDTO> getListFeedBack() {
        return ListFeedBack;
    }

    public void setListFeedBack(ArrayList<FeedBackDTO> listFeedBack) {
        ListFeedBack = listFeedBack;
    }
}

