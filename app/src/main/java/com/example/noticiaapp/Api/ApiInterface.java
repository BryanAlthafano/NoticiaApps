package com.example.noticiaapp.Api;

import com.example.noticiaapp.Model.Login.Login;
import com.example.noticiaapp.Model.NewsModel;
import com.example.noticiaapp.Model.Register.Register;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    //Login
    @FormUrlEncoded
    @POST("login.php")
    Call<Login> loginResponse(
            @Field("username") String username,
            @Field("password") String password
    );

    //Register
    @FormUrlEncoded
    @POST("register.php")
    Call<Register> registerResponse(
            @Field("fullname") String fullname,
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password
    );

}
