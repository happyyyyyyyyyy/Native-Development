package com.example.test.api;

import com.example.test.dto.DogDto;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Retrofit_interface {

    @GET("v1/breeds/{UserID}")
    Call<DogDto> test_api_get(
            @Header("Authorization") String apiKey,
            @Path("UserID") String userid); //UserID에 들어가는 값 받아오기

    @GET("v1/breeds")
    Call<ArrayList<DogDto>> test_api_get2(
            @Header("Authorization") String apiKey,
            @Query("limit") String limit);

    @GET("v1/breeds/search")
    Call<ArrayList<DogDto>> test_api_get3(@Query("q") String q);
}
