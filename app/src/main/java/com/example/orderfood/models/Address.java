package com.example.orderfood.models;

public class Address {
    private int Id;
    private String FullAddress;
    private String Latitude;
    private String Longtitude;

    private int AccountId; // id của account


    public Address() {
    }

    public Address(int id, String fullAddress, String latitude, String longtitude, int accountId) {
        Id = id;
        FullAddress = fullAddress;
        Latitude = latitude;
        Longtitude = longtitude;
        AccountId = accountId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getFullAddress() {
        return FullAddress;
    }

    public void setFullAddress(String fullAddress) {
        FullAddress = fullAddress;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongtitude() {
        return Longtitude;
    }

    public void setLongtitude(String longtitude) {
        Longtitude = longtitude;
    }

    public int getAccountId() {
        return AccountId;
    }

    public void setAccountId(int accountId) {
        AccountId = accountId;
    }
}
