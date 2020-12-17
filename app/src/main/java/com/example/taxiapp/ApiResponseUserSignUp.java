package com.example.taxiapp;

import java.util.List;

public class ApiResponseUserSignUp {
    private String objectId;
    private String createdAt;
    private String sessionToken;

    public ApiResponseUserSignUp(String objectId, String createdAt, String sessionToken) {
        this.objectId = objectId;
        this.createdAt = createdAt;
        this.sessionToken = sessionToken;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}
