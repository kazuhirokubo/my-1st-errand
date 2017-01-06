package com.example.kazu.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.example.kazu.myapplication.api.ApiService;
import com.example.kazu.myapplication.model.Item;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Main2Activity extends Activity {

    @BindView(R.id.listview)
    ListView mListView;
    private List<ItemModel> mItemList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Context mContext;
    private ListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);

        ButterKnife.bind(this);

        loadItem();


    }

    /**
     * [ログアウト]ボタン押下
     */
    @OnClick(R.id.buttonLogout)
    protected void finishActivity(){
        DeleteApi delItems = new DeleteApi(this);
        delItems.execute();
        finish();
    }

    /**
     * [追加]ボタン押下
     */
    @OnClick(R.id.buttonAdd)
    protected void addItem(){

        PostItemApi postItem = new PostItemApi(this);
        postItem.execute();

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowDateTime = simpleDateFormat.format(date);

        loadItem();

    }

    protected void loadItem(){

        GetItemsApi getItems = new GetItemsApi(this, mListView);
        getItems.execute();

    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            loadItem();
            // 3秒待機
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 3000);
        }
    };


    /*====================================================================================================
     * item一覧を取得する
     *
     *
     ====================================================================================================*/
    public class GetItemsApi extends AsyncTask<Void, Void, String> {

        public GetItemsApi(Context context, ListView listView){
            mContext = context;
            mListView = listView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 一番最初に実行される
        }

        @Override
        protected String doInBackground(Void... params) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://160.16.78.85/kubox/practice/public/")  // 基本url
                    .addConverterFactory(GsonConverterFactory.create())     // Gson
                    .client(client)
                    .build();


            //Interfaceから実装を取得
            ApiService API = retrofit.create(ApiService.class);

            API.apiItems().enqueue(new Callback<List<Item>>() {
                @Override
                public void onResponse(Call<List<Item>> call, retrofit2.Response<List<Item>> response) {
                    if (response.isSuccessful()) {
                        //通信結果をオブジェクトで受け取る
                        List<Item> demo = response.body();
                        Log.d("RETROFIT_TEST", "要素数:" + String.valueOf(demo.size()));

                    } else {
                        //通信が成功したが、エラーcodeが返ってきた場合はこちら
                        Log.d("RETROFIT_TEST", "error_code" + response.code());

                    }
                }

                @Override
                public void onFailure(Call<List<Item>> call, Throwable t) {
                    //通信が失敗した場合など
                    Log.d("RETROFIT_TEST", "suck:" + t.getMessage());
                }
            });

            HttpURLConnection con = null;
            String result = "";
            try {
                con = (HttpURLConnection) new URL("items").openConnection();
                result = InputStreamToString(con.getInputStream());

            } catch (IOException e) {
                return "error";

            } finally {
                if (con != null) {
                    con.disconnect();
                }
            }
            return result;
        }




        @Override
        protected void onPostExecute(String result) {
            if(result.equals("error")) {
                new AlertDialog.Builder(mContext)
                    .setTitle("エラー")
                    .setMessage("APIエラー(GET Items)")
                    .setPositiveButton("OK", null)
                    .show();
                mAdapter = new ListViewAdapter(mContext, R.layout.listview_row, null);
                mListView.setAdapter(mAdapter);
                return;
            } else {
                result = result.replace("null", "\"\"");
                Gson gson = new Gson();
                mItemList = new ArrayList<ItemModel>();
                mItemList = gson.fromJson(result, new TypeToken<List<ItemModel>>() {
                }.getType());
                mAdapter = new ListViewAdapter(mContext, R.layout.listview_row, mItemList);
                mListView.setAdapter(mAdapter);
            }
        }

        /*
         InputStream -> String
          */
        protected String InputStreamToString(InputStream is) throws IOException {

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            return sb.toString();
        }
    }

    /*====================================================================================================
     * itemを追加する
     *
     *
     ====================================================================================================*/

    public class PostItemApi extends AsyncTask<String, Void, String> {

        public PostItemApi(Context context){
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 一番最初に実行される
        }

        @Override
        protected String doInBackground(String... params) {

            String postString = ".";
            HttpURLConnection con = null;
            OutputStream out = null;

            String result = "";
            try {
                con = (HttpURLConnection) new URL("http://dev.domus.jp/kubox/practice/public/item").openConnection();
                con.setRequestMethod("POST");
                out = con.getOutputStream();
                out.write(postString.getBytes("UTF-8"));
                out.flush();
                out.close();
                result = InputStreamToString(con.getInputStream());

            } catch (Exception e) {
                result = "error";

            } finally {
                if (con != null) {
                    con.disconnect();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            PostItemModel res = null;

            if(result.equals("error")) {
                new AlertDialog.Builder(mContext)
                    .setTitle("エラー")
                    .setMessage("APIエラー(POST Item)")
                    .setPositiveButton("OK", null)
                    .show();
                mAdapter = new ListViewAdapter(mContext, R.layout.listview_row, null);
                mListView.setAdapter(mAdapter);
                return;
            }else {
                Gson gson = new Gson();
                res = gson.fromJson(result, PostItemModel.class);
            }

            if(res.getId().isEmpty()){

                new AlertDialog.Builder(mContext)
                    .setTitle("エラー")
                    .setMessage("APIエラー(POST Item)")
                    .setPositiveButton("OK", null)
                    .show();
                mAdapter = new ListViewAdapter(mContext, R.layout.listview_row, null);
                mListView.setAdapter(mAdapter);
                return;
            }
        }

        /*
         InputStream -> String
          */
        protected String InputStreamToString(InputStream is) throws IOException {

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            return sb.toString();
        }
    }

    /*====================================================================================================
     * itemを削除する
     *
     *
     ====================================================================================================*/

    public class DeleteApi extends AsyncTask<String, Void, String> {

        public DeleteApi(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 一番最初に実行される
        }

        @Override
        protected String doInBackground(String... pass) {

            HttpURLConnection con = null;
            String result = "";
            try {
                con = (HttpURLConnection) new URL(
                    "http://dev.domus.jp/kubox/practice/public/items").openConnection();
                con.setRequestMethod("DELETE");
                result = InputStreamToString(con.getInputStream());

            } catch (IOException e) {
                return "error";

            } finally {
                if (con != null) {
                    con.disconnect();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            AuthResponseModel res = null;

            if (result.equals("error")) {
                new AlertDialog.Builder(mContext)
                    .setTitle("エラー")
                    .setMessage("APIエラー(DELETE Items)")
                    .setPositiveButton("OK", null)
                    .show();
                mAdapter = new ListViewAdapter(mContext, R.layout.listview_row, null);
                mListView.setAdapter(mAdapter);
                return;
            } else {
                Gson gson = new Gson();
                res = gson.fromJson(result, AuthResponseModel.class);
            }

            if (res.result.equals("false")) {
                new AlertDialog.Builder(mContext)
                    .setTitle("エラー")
                    .setMessage("APIエラー(DELETE Items)")
                    .setPositiveButton("OK", null)
                    .show();
            }

            mAdapter = new ListViewAdapter(mContext, R.layout.listview_row, null);
            mListView.setAdapter(mAdapter);
            return;
        }


        // InputStream -> String
        private String InputStreamToString(InputStream is) throws IOException {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            return sb.toString();
        }
    }
}
