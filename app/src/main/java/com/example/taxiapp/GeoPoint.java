package com.example.taxiapp;

public class GeoPoint {
    private String __type;
    private double latitude;
    private double longitude;

    public GeoPoint(String __type, double latitude, double longitude) {
        this.__type = __type;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
