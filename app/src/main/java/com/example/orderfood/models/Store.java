package com.example.orderfood.models;

public class Store {
    public  int Id;
    public String Name;
    private String Latitude;
    private String Longtitude;

    public Store() {
    }

    public int getId() {
        return Id;
    }

    public Store(int id, String name, String latitude, String longtitude) {
        Id = id;
        Name = name;
        Latitude = latitude;
        Longtitude = longtitude;
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
}
