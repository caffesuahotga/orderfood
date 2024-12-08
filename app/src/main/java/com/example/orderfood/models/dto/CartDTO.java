package com.example.orderfood.models.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

// dùng cho nạp data lên giao diện
public class CartDTO  implements Serializable {
    private int ID ;
    private int productID;
    private String name;
    private int quantity;
    private String image;
    private double Price;
    private boolean isSelected;

    public CartDTO(int ID, int productID, String name, int quantity, String image, double price) {
        this.ID = ID;
        this.productID = productID;
        this.name = name;
        this.quantity = quantity;
        this.image = image;
        Price = price;
    }

    public CartDTO() {
    }

    public CartDTO(int productID, String name, int quantity, String image, double price) {
        this.productID = productID;
        this.name = name;
        this.quantity = quantity;
        this.image = image;
        Price = price;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

}
