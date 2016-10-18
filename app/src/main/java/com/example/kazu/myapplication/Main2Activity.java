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

    @OnClick(R.id.buttonLogout)
    protected void finishActivity(){
//        mItemList.clear();
//        mAdapter.notifyDataSetChanged();
//
//        deleteItem();
//
//        finish();
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
//        ItemModel item = new ItemModel("", "", nowDateTime, "");
//        mItemList.add(item);
//        mAdapter.notifyDataSetChanged();

    }


    protected void deleteItem(){

//        new Delete().from(Item.class).execute();

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
     *
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

            HttpURLConnection con = null;
            String result = "";
            try {
                con = (HttpURLConnection) new URL("http://dev.domus.jp/kubox/practice/public/items").openConnection();
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

        private Context mContext;
        private ListViewAdapter mAdapter;

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

            if(res.id.isEmpty()){

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
}
