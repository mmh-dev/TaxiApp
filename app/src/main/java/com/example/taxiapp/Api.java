package com.example.taxiapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {
    @Headers({
            "X-Parse-Application-Id: hfIm78eb3TwgTowccQGg9Uvm8dCy17tk4O90fspE",
            "X-Parse-REST-API-Key: IuLiQJRoNfhO1I9mVSYq81SvMbouVc5f3D5ioKqq"
    })

    @POST("/users")
    Call<ApiResponseUserSignUp> createUser (
            @Header("X-Parse-Revocable-Session") int session,
            @Body User user);

    @GET("/login")
    Call<ApiResponseUserLogin> loggingIn (
            @Header("X-Parse-Revocable-Session") int session,
            @Query("username") String username,
            @Query("password") String password);
}
