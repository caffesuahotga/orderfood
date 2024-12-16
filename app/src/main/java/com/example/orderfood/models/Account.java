package com.example.orderfood.models;

public class Account {
    private int Id;
    private String Name;
    private String Username;
    private String Password;
    private String Phone;
    private int Role; // 0 admin, 1 shipper, 2  customer
    private String Image;
    private String FCMToken; //token push noti

    public String getFCMToken() {
        return FCMToken;
    }

    public void setFCMToken(String FCMToken) {
        this.FCMToken = FCMToken;
    }

    public int getStoreId() {
        return StoreId;
    }

    public void setStoreId(int storeId) {
        StoreId = storeId;
    }

    private int StoreId;

    public Account(int id, String name, String username, String password, String phone, int role, int storeId, String image) {
        Id = id;
        Name = name;
        Username = username;
        Password = password;
        Phone = phone;
        Role = role;
        StoreId = storeId;
        Image = image;
    }

    public Account() {
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public int getRole() {
        return Role;
    }

    public void setRole(int role) {
        Role = role;
    }
}
