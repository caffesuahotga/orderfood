package com.example.orderfood.models.dto;

public class StatisticalDTO {
    private String date;
    private double total;

    public StatisticalDTO(String date, double total) {
        this.date = date;
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
