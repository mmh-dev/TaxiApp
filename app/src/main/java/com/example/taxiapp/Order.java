package com.example.taxiapp;

public class Order {
    private GeoPoint location;
    private String username;
    private String address;
    private String phone;
    private Boolean isTaken;
    private String driver;

    public Order(GeoPoint location, String username, String address, String phone, Boolean isTaken) {
        this.location = location;
        this.username = username;
        this.address = address;
        this.phone = phone;
        this.isTaken = isTaken;
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

    public Boolean getTaken() {
        return isTaken;
    }

    public void setTaken(Boolean taken) {
        isTaken = taken;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }
}
