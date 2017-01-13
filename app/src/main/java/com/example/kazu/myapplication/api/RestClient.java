package com.example.kazu.myapplication.api;

import android.util.Log;

import com.example.kazu.myapplication.model.CreatedItem;
import com.example.kazu.myapplication.model.Item;
import com.example.kazu.myapplication.model.Judgement;
import com.example.kazu.myapplication.setting.Common;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kbx on 2017/01/10.
 */

public class RestClient {

    private ApiService mApiService;

//    public RestClient(){
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
//
//        mApiService = new Retrofit.Builder()
//                .baseUrl(Common.ENDPOINT_URL)                           // 基本url
//                .addConverterFactory(GsonConverterFactory.create())     // Gson
//                .client(client)
//                .build().create(ApiService.class);
//    }

    public RestClient() {

//        mApiUrl = url;
        mApiService = new Retrofit.Builder()
                .baseUrl(Common.ENDPOINT_URL)
                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(getHttpClientBuilder()
                        .build()).build().create(ApiService.class);
    }

    public Retrofit.Builder getServiceBuilder() {
        return new Retrofit.Builder()
                .baseUrl(Common.ENDPOINT_URL)
                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(getHttpClientBuilder().build());
    }


    public OkHttpClient.Builder getHttpClientBuilder() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request original = chain.request();

                        Request.Builder builder = original.newBuilder();

                        //X-ApplicationVersion
                        //X-PlatformOs
//                        String applicationVersion = .getInstance().getVersionName();
//                        String platformOS = "Android/" + String.valueOf(HeartIsInApplication.getInstance().getReleaseVersion());
//                        builder.addHeader("X-ApplicationVersion", applicationVersion);
//                        builder.addHeader("X-PlatformOs", platformOS);

                        // POST時にlibcore.io.Streams.readAsciiLine()でjava.io.EOFExceptionがスローされるandroidのバグの回避策
                        // KITCAT以降のOSもLTEだとおかしかった
                        builder.addHeader("Connection", "close");

                        builder.method(original.method(), original.body());
                        Request request = builder.build();

                        return chain.proceed(request);
                    }
                });
        return httpClientBuilder;
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
