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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
    LocationManager locationManager;
    ListView listView;
    Location driverLocation = new Location("");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        adapter = new ArrayAdapter<>(OrderList.this, android.R.layout.simple_list_item_activated_1, requestsList);
        listView = findViewById(R.id.orderListView);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

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

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Location customerLocation = new Location("");
                customerLocation.setLongitude(orderList.get(position).getLocation().getLongitude());
                customerLocation.setLatitude(orderList.get(position).getLocation().getLatitude());
                Intent intent = new Intent(OrderList.this, DriverMapsActivity.class);
                intent.putExtra("driverLocation", driverLocation);
                intent.putExtra("userLocation", customerLocation);
                startActivity(intent);
            }
        });
    }
}