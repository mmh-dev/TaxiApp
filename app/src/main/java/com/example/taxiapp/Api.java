package com.example.taxiapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
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


    @Headers({
            "X-Parse-Application-Id: hfIm78eb3TwgTowccQGg9Uvm8dCy17tk4O90fspE",
            "X-Parse-REST-API-Key: IuLiQJRoNfhO1I9mVSYq81SvMbouVc5f3D5ioKqq"
    })
    @GET("/login")
    Call<ApiResponseUserLogin> loggingIn (
            @Header("X-Parse-Revocable-Session") int session,
            @Query("username") String username,
            @Query("password") String password);


    @Headers({
            "X-Parse-Application-Id: hfIm78eb3TwgTowccQGg9Uvm8dCy17tk4O90fspE",
            "X-Parse-REST-API-Key: IuLiQJRoNfhO1I9mVSYq81SvMbouVc5f3D5ioKqq"
    })
    @POST("/classes/Order")
    Call<ApiResponseUserSignUp> createOrder (
            @Body Order order);


    @Headers({
            "X-Parse-Application-Id: hfIm78eb3TwgTowccQGg9Uvm8dCy17tk4O90fspE",
            "X-Parse-REST-API-Key: IuLiQJRoNfhO1I9mVSYq81SvMbouVc5f3D5ioKqq"
    })
    @GET("/classes/Order")
    Call<ApiResponseOrder> getOrder ();

    @Headers({
            "X-Parse-Application-Id: hfIm78eb3TwgTowccQGg9Uvm8dCy17tk4O90fspE",
            "X-Parse-REST-API-Key: IuLiQJRoNfhO1I9mVSYq81SvMbouVc5f3D5ioKqq"
    })
    @PUT("/classes/Order/{objectId}")
    Call<ApiResponseOrderUpdate> updateOrder (
            @Path("objectId") String objectId,
            @Body Order order);
}
