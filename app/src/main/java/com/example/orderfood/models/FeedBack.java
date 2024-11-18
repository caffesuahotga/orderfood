package com.example.orderfood.models;

public class FeedBack {
    private int Id;
    private int OrderDetailId;

    private String Content;
    private int Star;

    public FeedBack(int id, int orderDetailId, String content, int star) {
        Id = id;
        OrderDetailId = orderDetailId;
        Content = content;
        Star = star;
    }

    public FeedBack() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getOrderDetailId() {
        return OrderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        OrderDetailId = orderDetailId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getStar() {
        return Star;
    }

    public void setStar(int star) {
        Star = star;
    }
}
