package com.example.taxiapp;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DriverMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button acceptRequest;
    Location driverLocation = new Location("");
    Location userLocation = new Location("");
    Order selectedOrder;
    ConstraintLayout parent;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.driverMap);
        mapFragment.getMapAsync(this);

        parent = findViewById(R.id.driverMapLayout);

        driverLocation = getIntent().getParcelableExtra("driverLocation");
        userLocation = getIntent().getParcelableExtra("userLocation");

        Type type = new TypeToken<Order>(){}.getType();
        selectedOrder = new Gson().fromJson(getIntent().getStringExtra("selectedOrder"), type);
        acceptRequest = findViewById(R.id.acceptRequest);

        acceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOrder.setTaken(true);
                selectedOrder.setDriver(getIntent().getStringExtra("driverUsername"));
                Log.i("name", selectedOrder.getDriver());

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://parseapi.back4app.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Api api = retrofit.create(Api.class);
                Call<ApiResponseOrderUpdate> call = api.updateOrder(selectedOrder.getObjectId(), selectedOrder);
                call.enqueue(new Callback<ApiResponseOrderUpdate>() {

                    @Override
                    public void onResponse(Call<ApiResponseOrderUpdate> call, Response<ApiResponseOrderUpdate> response) {
                        if (response.isSuccessful()){
                            Snackbar.make(parent, "You took the order!", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponseOrderUpdate> call, Throwable t) {
                        Log.i("error", t.getMessage());
                    }
                });
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng driverPosition = new LatLng(driverLocation.getLatitude(), driverLocation.getLongitude());
        LatLng userPosition = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());

        List<Marker> markers = new ArrayList<>();
        BitmapDescriptor driverIcon = BitmapDescriptorFactory.fromResource(R.drawable.marker);
        BitmapDescriptor userIcon = BitmapDescriptorFactory.fromResource(R.drawable.destination_marker);

        markers.add(mMap.addMarker(new MarkerOptions().position(driverPosition).title("Your location").icon(driverIcon)));
        markers.add(mMap.addMarker(new MarkerOptions().position(userPosition).title("Customer location").icon(userIcon)));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers){
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 400;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
    }
}