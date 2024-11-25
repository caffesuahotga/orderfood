package com.example.orderfood.models.dto;

public class FeedBackDTO {
    private String ImageUser;
    private String NameUser;
    private String Content;
    private double Star;

    public FeedBackDTO() {
    }

    public FeedBackDTO(String imageUser, String nameUser, String content, int star) {
        ImageUser = imageUser;
        NameUser = nameUser;
        Content = content;
        Star = star;
    }

    public String getImageUser() {
        return ImageUser;
    }

    public void setImageUser(String imageUser) {
        ImageUser = imageUser;
    }

    public String getNameUser() {
        return NameUser;
    }

    public void setNameUser(String nameUser) {
        NameUser = nameUser;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public double getStar() {
        return Star;
    }

    public void setStar(int star) {
        Star = star;
    }
}
