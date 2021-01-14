package com.example.taxiapp;

public class ApiResponseOrderUpdate {

    private String updatedAt;

    public ApiResponseOrderUpdate(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
