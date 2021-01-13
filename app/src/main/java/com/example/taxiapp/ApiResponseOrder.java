package com.example.taxiapp;

import java.util.List;

public class ApiResponseOrder {

    private List<Order> results;

    public ApiResponseOrder(List<Order> results) {
        this.results = results;
    }

    public List<Order> getResults() {
        return results;
    }

}
