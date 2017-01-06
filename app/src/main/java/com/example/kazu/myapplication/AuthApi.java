package com.example.kazu.myapplication;

/**
 * Created by kubox on 2016/10/17.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.kazu.myapplication.api.ApiService;
import com.example.kazu.myapplication.model.Judgement;
import com.example.kazu.myapplication.setting.Common;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.Integer.parseInt;


public class AuthApi extends AsyncTask<String, Void, String> {

    private Context mContext;

    public AuthApi(Context context){
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // 一番最初に実行される
    }

    @Override
    protected String doInBackground(String... pass) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Common.ENDPOINT_URL)  // 基本url
                .addConverterFactory(GsonConverterFactory.create())     // Gson
                .client(client)
                .build();

        ApiService API = retrofit.create(ApiService.class);

        int intPassword;
        Judgement judge = new Judgement();
        try{
            intPassword = parseInt(pass[0]);
            judge= API.apiAuth(intPassword).execute().body();
        }catch (Exception ex){
            judge.setResult(false);
        }

        return judge.getResult().toString();
    }

    @Override
    protected void onPostExecute(String result) {

//        AuthResponseModel res = null;


        if(result.equals("true")){

            mContext.startActivity(new Intent(
                mContext,
                Main2Activity.class)
            );
            ((MainActivity) mContext).textViewPasswd.setText("");
        }else{
            new AlertDialog.Builder(mContext)
                    .setTitle("エラー")
                    .setMessage("APIエラー(GET Auth)")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }
    }
}
