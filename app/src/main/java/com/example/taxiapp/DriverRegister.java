package com.example.taxiapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DriverRegister extends AppCompatActivity {
    TextView customerApp_link;
    Button signUp_driver, signIn_driver;
    MaterialEditText email, username, phone, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);

        customerApp_link = findViewById(R.id.customerApp_link);
        signUp_driver = findViewById(R.id.signUp_driver);
        signIn_driver = findViewById(R.id.signIn_driver);

        customerApp_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DriverRegister.this, CustomerRegister.class));
            }
        });

        signUp_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder signUpDialog = new AlertDialog.Builder(DriverRegister.this);
                View signUpLayout = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                signUpDialog.setView(signUpLayout);
                email = signUpLayout.findViewById(R.id.email);
                username = signUpLayout.findViewById(R.id.username);
                phone = signUpLayout.findViewById(R.id.phone);
                password = signUpLayout.findViewById(R.id.password);
                signUpDialog.setPositiveButton("Register", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String txt_username = username.getText().toString();
                        String txt_email = email.getText().toString();
                        String txt_password = password.getText().toString();
                        String txt_phone = phone.getText().toString();

                        if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_phone)){
                            Toast.makeText(DriverRegister.this, "Complete all fields!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            register(txt_username, txt_email, txt_password, txt_phone);
                        }
                    }
                });
                signUpDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                signUpDialog.create().show();
            }
        });

        signIn_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DriverRegister.this, Login.class));
            }
        });
    }

    private void register(String txt_username, String txt_email, String txt_password, String txt_phone) {
        User user = new User(txt_username, txt_password, txt_email, txt_phone, "Driver");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://parseapi.back4app.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<User> call = api.createUser(1, user);
        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    Toast.makeText(DriverRegister.this, "Driver is registered!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DriverRegister.this, Login.class));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("error", t.getMessage());
            }
        });
    }
}