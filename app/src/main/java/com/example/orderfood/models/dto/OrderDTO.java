package com.example.orderfood.models.dto;

import java.util.ArrayList;

public class OrderDTO {
    private String NameUserOrder; // tên nhận hàng
    private String Address; // dia chi giao hang
    private String Phone; // số điện thoại nhận hàng
    private String Note;
    private double TotalPrice;

    private int AddressId; // id dia chi acc
    private int ShipperId;
    private int CustomerId;

    private double ShipLatitude; // tọa đọ shiper
    private double ShipLongtitude; // tọa đọ shiper

    // danh sách món ăn
    ArrayList<OrderProductDTO> Products = new ArrayList<OrderProductDTO>();

    public OrderDTO(String nameUserOrder, String address, String phone, String note, int addressId, int shipperId, int customerId, double shipLatitude, double shipLongtitude, ArrayList<OrderProductDTO> products) {
        NameUserOrder = nameUserOrder;
        Address = address;
        Phone = phone;
        Note = note;
        AddressId = addressId;
        ShipperId = shipperId;
        CustomerId = customerId;
        ShipLatitude = shipLatitude;
        ShipLongtitude = shipLongtitude;
        Products = products;
    }

    public OrderDTO() {
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getNameUserOrder() {
        return NameUserOrder;
    }

    public void setNameUserOrder(String nameUserOrder) {
        NameUserOrder = nameUserOrder;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
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

    public ArrayList<OrderProductDTO> getProducts() {
        return Products;
    }

    public void setProducts(ArrayList<OrderProductDTO> products) {
        Products = products;
    }
}
