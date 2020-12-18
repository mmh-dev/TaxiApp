package com.example.taxiapp;

public class Order {
    private GeoPoint location;
    private String username;
    private String address;
    private String phone;

    public Order(GeoPoint location, String username, String address, String phone) {
        this.location = location;
        this.username = username;
        this.address = address;
        this.phone = phone;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
