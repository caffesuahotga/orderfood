package com.example.orderfood.sqlLite.model;

public class Cart {
    private int ID ;
    private int productID;
    private String name;
    private int quantity;
    private String image;

    public Cart(int ID, int productID, String name, int quantity, String image) {
        this.ID = ID;
        this.productID = productID;
        this.name = name;
        this.quantity = quantity;
        this.image = image;
    }

    public Cart() {
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
}
