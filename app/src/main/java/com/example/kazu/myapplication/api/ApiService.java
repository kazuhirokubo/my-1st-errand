package com.example.kazu.myapplication.api;


import com.example.kazu.myapplication.model.CreatedItem;
import com.example.kazu.myapplication.model.Item;
import com.example.kazu.myapplication.model.Judgement;
import com.example.kazu.myapplication.model.UpdatedItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by kbx on 2017/01/06.
 */

public interface ApiService {
    @GET("auth/{id}")
    Call<Judgement> apiAuth(@Path("id") String password);

    @GET("items")
    Call<List<Item>> apiGetItems();

    @FormUrlEncoded
    @POST("item")
    Call<CreatedItem> apiPostItem(@Field("body") String body);

    @FormUrlEncoded
    @PUT("item")
    Call<UpdatedItem> apiPutItem(@Field("id") int id, @Field("body") String body);

    @DELETE("item/{id}")
    Call<Judgement> apiDeleteItem(@Path("id") int id);

    @DELETE("items")
    Call<Judgement> apiDeleteItems();
}