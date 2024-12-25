package com.example.orderfood.models.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.orderfood.models.FeedBack;

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
    private String feedback = "Trống";
    private int OrderDetailId;
    private int Star;

    public int getStar() {
        return Star;
    }

    public void setStar(int star) {
        Star = star;
    }

    public CartDTO(int ID, int productID, String name, int quantity, String image, double price) {
        this.ID = ID;
        this.productID = productID;
        this.name = name;
        this.quantity = quantity;
        this.image = image;
        Price = price;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public CartDTO() {
    }

    public int getOrderDetailId() {
        return OrderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        OrderDetailId = orderDetailId;
    }

    public CartDTO(int productID, String name, int quantity, String image, double price, String fb, int odId, int sao) {
        this.productID = productID;
        this.name = name;
        this.quantity = quantity;
        this.image = image;
        Price = price;
        feedback = fb;
        OrderDetailId =odId;
        Star = sao;
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
