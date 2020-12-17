package com.example.taxiapp;

public class ApiResponseUserLogin {
    private String objectId;
    private String email;
    private String phone;
    private String userType;
    private String username;
    private String sessionToken;

    public ApiResponseUserLogin(String objectId, String email, String phone, String userType, String username, String sessionToken) {
        this.objectId = objectId;
        this.email = email;
        this.phone = phone;
        this.userType = userType;
        this.username = username;
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

    public String getSessionToken() {
        return sessionToken;
    }
}
