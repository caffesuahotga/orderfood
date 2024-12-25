package com.example.orderfood.models;

import java.util.Date;
import java.util.UUID;

public class Noti {
    private String Id;
    private String Title;
    private String Content;
    private Date Date;
    private int OrderId;
    private int AccountId;

    // Constructor để tự động tạo GUID cho Id
    public Noti() {
        this.Id = UUID.randomUUID().toString();
    }

    // Constructor tùy chỉnh nếu bạn cần cung cấp thêm thông tin khi khởi tạo
    public Noti(String title, String content, int accountId) {
        this.Id = UUID.randomUUID().toString();
        this.Title = title;
        this.Content = content;
        this.AccountId = accountId;
    }

    public Noti(String title, String content, java.util.Date date, int orderId, int accountId) {
        this.Id = UUID.randomUUID().toString();
        Title = title;
        Content = content;
        Date = date;
        OrderId = orderId;
        AccountId = accountId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getAccountId() {
        return AccountId;
    }

    public void setAccountId(int accountId) {
        AccountId = accountId;
    }
}

