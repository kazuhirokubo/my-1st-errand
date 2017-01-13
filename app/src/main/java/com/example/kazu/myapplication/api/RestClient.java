package com.example.kazu.myapplication.api;

import android.util.Log;

import com.example.kazu.myapplication.model.CreatedItem;
import com.example.kazu.myapplication.model.Item;
import com.example.kazu.myapplication.model.Judgement;
import com.example.kazu.myapplication.setting.Common;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kbx on 2017/01/10.
 */

public class RestClient {

    private ApiService mApiService;

    public RestClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        mApiService = new Retrofit.Builder()
                .baseUrl(Common.ENDPOINT_URL)                           // 基本url
                .addConverterFactory(GsonConverterFactory.create())     // Gson
                .client(client)
                .build().create(ApiService.class);
    }

    public Call<Judgement> auth(String password){
        return mApiService.apiAuth(password);
    }

    public Call<Judgement> deleteItems(){
        return mApiService.apiDeleteItems();
    }

    public Call<List<Item>> getItems(){
        return mApiService.apiGetItems();
    }

    public Call<CreatedItem> postItem(String body){
        return mApiService.apiPostItem(body);
    }
}
