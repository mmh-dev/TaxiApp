package com.example.taxiapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

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
    ImageView exit_btn_driver;
    SharedPreferences preferences;

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

        exit_btn_driver = findViewById(R.id.exit_btn_driver);
        exit_btn_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("token", "");
                editor.apply();
                Toast.makeText(DriverMapsActivity.this, "Driver is logged out!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DriverMapsActivity.this, Register.class));
            }
        });

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
                            String uri = "google.navigation:q=" + userLocation.getLatitude() + "," + userLocation.getLongitude() + "&mode=d";
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse(uri));
                            intent.setPackage("com.google.android.apps.maps");
                            startActivity(intent);
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

        markers.add(mMap.addMarker(new MarkerOptions().position(driverPosition).title("You're here").icon(driverIcon)));
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