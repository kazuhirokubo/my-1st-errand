package com.example.kazu.myapplication;

/**
 * Created by kubox on 2016/10/17.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;


import com.example.kazu.myapplication.api.ApiService;
import com.example.kazu.myapplication.api.RestClient;
import com.example.kazu.myapplication.model.Judgement;
import com.example.kazu.myapplication.setting.Common;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.Integer.parseInt;


public class AuthApi {

//    private Context mContext;
//
//    public AuthApi(Context context) {
//        mContext = context;
//    }
//
//    public response(){
//
//    }
////
////
////    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
////    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
////    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
////
////    Retrofit retrofit = new Retrofit.Builder()
////            .baseUrl(Common.ENDPOINT_URL)  // 基本url
////            .addConverterFactory(GsonConverterFactory.create())     // Gson
////            .client(client)
////            .build();
////
////    ApiService API = retrofit.create(ApiService.class);
////
////    int intPassword;
////    Judgement judge = new Judgement();
////
////    try
////    {
////        intPassword = parseInt(pass[0]);
////        judge = API.apiAuth(intPassword).execute().body();
////    }
////
////    catch(
////    Exception ex
////    )
//
//    {
//        judge.setResult(false);
//    }
//
//    return judge.getResult().
//
//    toString();
}

