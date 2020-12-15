package com.example.taxiapp;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.applicationID))
                .clientKey(getString(R.string.clientKey))
                .server(getString(R.string.ParseAPI))
                .build()
        );

    }

}
