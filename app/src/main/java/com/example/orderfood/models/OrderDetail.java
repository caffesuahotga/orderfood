package com.example.orderfood.models;

public class OrderDetail {
    private int Id;
    private int OrderId;
    private int ProductId;

    private int Price;
    private int Amount;

    public OrderDetail(int id, int orderId, int productId, int price, int amount) {
        Id = id;
        OrderId = orderId;
        ProductId = productId;
        Price = price;
        Amount = amount;
    }

    public OrderDetail() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }
}
