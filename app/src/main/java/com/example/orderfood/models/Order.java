package com.example.orderfood.models;

import java.util.Date;

public class Order {
    private int Id;
    private String NameUserOrder; // tên nhận hàng
    private String Address; // dia chi giao hang
    private String Phone; // số điện thoại nhận hàng
    private String Note;
    private double TotalPrice;
    private int PaymentType; // 1 tiền mặt
    private int Status;// 1 chờ xác nhận, 2 đã xác nhận, 3 đang giao, 4 đã giao-hoàn thành, 5 hủy
    private Date Date;
    private String Feedback;

    private int AddressId; // id dia chi acc
    private int ShipperId;
    private int CustomerId;

    private double ShipLatitude; // tọa đọ shiper
    private double ShipLongtitude; // tọa đọ shiper

    public String getFeedback() {
        return Feedback;
    }

    public void setFeedback(String feedback) {
        Feedback = feedback;
    }

    public Order(int id, String address, int shipperId, int customerId, double shipLatitude, double shipLongtitude) {
        Id = id;
        Address = address;
        ShipperId = shipperId;
        CustomerId = customerId;
        ShipLatitude = shipLatitude;
        ShipLongtitude = shipLongtitude;
    }


    public int getStatus() {
        return Status;
    }


    public void setStatus(int status) {
        Status = status;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public int getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(int paymentType) {
        PaymentType = paymentType;
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }

    public Order() {
    }

    public Order(int id, String nameUserOrder, String address, String phone, String note, int addressId, int shipperId, int customerId, double shipLatitude, double shipLongtitude) {
        Id = id;
        NameUserOrder = nameUserOrder;
        Address = address;
        Phone = phone;
        Note = note;
        AddressId = addressId;
        ShipperId = shipperId;
        CustomerId = customerId;
        ShipLatitude = shipLatitude;
        ShipLongtitude = shipLongtitude;
    }

    public Order(int id, String nameUserOrder, String address, String phone, String note, int shipperId, int customerId, double shipLatitude, double shipLongtitude) {
        Id = id;
        NameUserOrder = nameUserOrder;
        Address = address;
        Phone = phone;
        Note = note;
        ShipperId = shipperId;
        CustomerId = customerId;
        ShipLatitude = shipLatitude;
        ShipLongtitude = shipLongtitude;
    }

    public Order(int id, String nameUserOrder, String address, String phone, String note, double totalPrice, int paymentType, int status, java.util.Date date, String feedback, int addressId, int shipperId, int customerId, double shipLatitude, double shipLongtitude) {
        Id = id;
        NameUserOrder = nameUserOrder;
        Address = address;
        Phone = phone;
        Note = note;
        TotalPrice = totalPrice;
        PaymentType = paymentType;
        Status = status;
        Date = date;
        Feedback = feedback;
        AddressId = addressId;
        ShipperId = shipperId;
        CustomerId = customerId;
        ShipLatitude = shipLatitude;
        ShipLongtitude = shipLongtitude;
    }

    public String getNameUserOrder() {
        return NameUserOrder;
    }

    public void setNameUserOrder(String nameUserOrder) {
        NameUserOrder = nameUserOrder;
    }


    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getAddressId() {
        return AddressId;
    }

    public void setAddressId(int addressId) {
        AddressId = addressId;
    }
}
