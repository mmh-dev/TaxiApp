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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {
    Button signUp, signIn;
    MaterialEditText email, username, phone, password;
    RadioGroup radioGroup;
    String userType;
    TextView dialog_title, dialog_message;
    @Override
    protected void onStart() {
        super.onStart();
        ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(Register.this, MainActivity.class));

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signUp = findViewById(R.id.signUp);
        signIn = findViewById(R.id.signIn);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder signUpDialog = new AlertDialog.Builder(Register.this);
                View signUpLayout = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                signUpDialog.setView(signUpLayout);
                email = signUpLayout.findViewById(R.id.email);
                username = signUpLayout.findViewById(R.id.username);
                phone = signUpLayout.findViewById(R.id.phone);
                password = signUpLayout.findViewById(R.id.password);
                radioGroup = signUpLayout.findViewById(R.id.radioGroup);

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                        switch (checkedId){
                            case R.id.driver_btn:
                                userType = "driver";
                                return;
                            case R.id.customer_btn:
                                userType = "customer";
                                return;
                        }
                    }
                });

                signUpDialog.setPositiveButton("Register", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String txt_username = username.getText().toString();
                        String txt_email = email.getText().toString();
                        String txt_password = password.getText().toString();
                        String txt_phone = phone.getText().toString();

                        if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_phone)){
                            Toast.makeText(Register.this, "Complete all fields!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            register(txt_username, txt_email, txt_password, txt_phone, userType);
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

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder signInDialog = new AlertDialog.Builder(Register.this);
                View signInLayout = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                signInDialog.setView(signInLayout);
                email = signInLayout.findViewById(R.id.email);
                email.setVisibility(View.GONE);
                username = signInLayout.findViewById(R.id.username);
                phone = signInLayout.findViewById(R.id.phone);
                phone.setVisibility(View.GONE);
                password = signInLayout.findViewById(R.id.password);
                radioGroup = signInLayout.findViewById(R.id.radioGroup);
                radioGroup.setVisibility(View.GONE);
                dialog_title = signInLayout.findViewById(R.id.dialog_title);
                dialog_title.setText("Sign-In");
                dialog_message = signInLayout.findViewById(R.id.dialog_message);
                dialog_message.setText("Sign-In with username and password");

                signInDialog.setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String txt_username = username.getText().toString();
                        String txt_password = password.getText().toString();

                        if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_password)){
                            Toast.makeText(Register.this, "Complete all fields!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            login(txt_username, txt_password);
                        }
                    }
                });
                signInDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                signInDialog.create().show();
            }
        });
    }

    private void login(String txt_username, String txt_password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://parseapi.back4app.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<User> call = api.loggingIn(1, txt_username, txt_password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    Toast.makeText(Register.this, "User is Logged in!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Register.this, MainActivity.class));
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("error", t.getMessage());
            }
        });
    }

    private void register(String txt_username, String txt_email, String txt_password, String txt_phone, String userType) {
        User user = new User(txt_username, txt_password, txt_email, txt_phone, userType);
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
                    Toast.makeText(Register.this, "User is registered!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Register.this, MainActivity.class));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("error", t.getMessage());
            }
        });
    }
}