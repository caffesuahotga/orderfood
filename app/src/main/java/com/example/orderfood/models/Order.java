package com.example.orderfood.models;

public class Order {
    private int Id;
    private int AddressId;
    private int ShipperId;
    private int CustomerId;

    private double ShipLatitude; // tọa đọ shiper
    private double ShipLongtitude; // tọa đọ shiper

    public Order(int id, int addressId, int shipperId, int customerId, double shipLatitude, double shipLongtitude) {
        Id = id;
        AddressId = addressId;
        ShipperId = shipperId;
        CustomerId = customerId;
        ShipLatitude = shipLatitude;
        ShipLongtitude = shipLongtitude;
    }

    public Order() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getAddressId() {
        return AddressId;
    }

    public void setAddressId(int addressId) {
        AddressId = addressId;
    }

    public int getShipperId() {
        return ShipperId;
    }

    public void setShipperId(int shipperId) {
        ShipperId = shipperId;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public double getShipLatitude() {
        return ShipLatitude;
    }

    public void setShipLatitude(double shipLatitude) {
        ShipLatitude = shipLatitude;
    }

    public double getShipLongtitude() {
        return ShipLongtitude;
    }

    public void setShipLongtitude(double shipLongtitude) {
        ShipLongtitude = shipLongtitude;
    }
}
