package com.example.taxiapp;

public class ApiResponseUserLogin {
    private String objectId;
    private String email;
    private String phone;
    private String userType;
    private String username;
    private String createdAt;
    private String updatedAt;
    private String sessionToken;

    public ApiResponseUserLogin(String objectId, String email, String phone, String userType, String username, String createdAt, String updatedAt, String sessionToken) {
        this.objectId = objectId;
        this.email = email;
        this.phone = phone;
        this.userType = userType;
        this.username = username;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.sessionToken = sessionToken;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserType() {
        return userType;
    }

    public String getUsername() {
        return username;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}
