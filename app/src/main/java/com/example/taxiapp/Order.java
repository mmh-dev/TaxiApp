package com.example.taxiapp;

import android.location.Location;

public class Order {
    private Location location;
    private String username;
    private String address;
    private String phone;

    public Order(Location location, String username, String address, String phone) {
        this.location = location;
        this.username = username;
        this.address = address;
        this.phone = phone;
    }

    public Location getLocation() {
        return location;
    }

    public String getUsername() {
        return username;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }


}
