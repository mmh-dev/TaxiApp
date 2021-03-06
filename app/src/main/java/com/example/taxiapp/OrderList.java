package com.example.taxiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderList extends AppCompatActivity {

    List<String> requestsList = new ArrayList<>();
    List<Order> orderList = new ArrayList<>();
    ArrayAdapter <String> adapter;
    ListView listView;
    Location driverLocation = new Location("");

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        buildLocationRequest();


        adapter = new ArrayAdapter<>(OrderList.this, android.R.layout.simple_list_item_activated_1, requestsList);
        listView = findViewById(R.id.orderListView);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(OrderList.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://parseapi.back4app.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<ApiResponseOrder> call = api.getOrder();
        call.enqueue(new Callback<ApiResponseOrder>() {

            @Override
            public void onResponse(Call<ApiResponseOrder> call, Response<ApiResponseOrder> response) {
                if (response.isSuccessful()){
                    orderList.clear();
                    orderList.addAll(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<ApiResponseOrder> call, Throwable t) {
                Log.i("error", t.getMessage());
            }
        });

        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for (Location location: locationResult.getLocations()) {
                    requestsList.clear();
                    driverLocation = location;
                    for (int i = 0; i <orderList.size() ; i++) {
                        Location userLocation = new Location("");
                        userLocation.setLatitude(orderList.get(i).getLocation().getLatitude());
                        userLocation.setLongitude(orderList.get(i).getLocation().getLongitude());
                        float distance = location.distanceTo(userLocation) / 1000;
                        String distanceF = String.format("%.02f", distance);
                        requestsList.add(distanceF + " км");
                    }
                    listView.setAdapter(adapter);
                }
            }
        };
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Location customerLocation = new Location("");
                customerLocation.setLongitude(orderList.get(position).getLocation().getLongitude());
                customerLocation.setLatitude(orderList.get(position).getLocation().getLatitude());
                Intent intent = new Intent(OrderList.this, DriverMapsActivity.class);
                intent.putExtra("driverLocation", driverLocation);
                intent.putExtra("userLocation", customerLocation);
                intent.putExtra("selectedOrder", new Gson().toJson(orderList.get(position)));
                intent.putExtra("driverUsername", getIntent().getStringExtra("driverUsername"));
                intent.putExtra("driverPhone", getIntent().getStringExtra("driverPhone"));

                startActivity(intent);
            }
        });
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
            }
        }
    }
}