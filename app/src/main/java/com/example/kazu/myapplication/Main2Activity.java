package com.example.kazu.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Main2Activity extends Activity {

    @BindView(R.id.listview) ListView mListView;

    private BaseAdapter mAdapter;
    private List<ListViewItemModel> mItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);

        ButterKnife.bind(this);

        mItemList = loadItem();

//        mItemList = new ArrayList<ListViewItemModel>();
//        mItemList.add(new ListViewItemModel("2016/10/01 12:00:00"));
//        mItemList.add(new ListViewItemModel("2016/10/01 11:00:00"));
//        mItemList.add(new ListViewItemModel("2016/10/01 10:00:00"));

        mAdapter = new ListViewAdapter(this.getApplicationContext(), R.layout.listview_row, mItemList);

        // ListViewにadapterをセット
        mListView.setAdapter(mAdapter);
    }

    @OnClick(R.id.buttonLogout)
    protected void finishActivity(){
        mItemList.clear();
        mAdapter.notifyDataSetChanged();
        deleteFile("item.txt");
        finish();
    }

    @OnClick(R.id.buttonAdd)
    protected void addItem(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String nowDateTime = simpleDateFormat.format(date);
        mItemList.add(0, new ListViewItemModel(nowDateTime));
        mAdapter.notifyDataSetChanged();
        writeItem(nowDateTime);
    }

    protected void writeItem(String text){
        try{
            FileOutputStream out = openFileOutput("item.txt", MODE_PRIVATE|MODE_APPEND);
            out.write(text.getBytes());
            out.write(10);

        }catch( IOException e ){
            e.printStackTrace();
        }

    }

    protected List<ListViewItemModel> loadItem(){
        List<ListViewItemModel> list = new ArrayList<ListViewItemModel>();
        try{
            FileInputStream in = openFileInput("item.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader( in , "UTF-8"));
            String tmp;
            while((tmp = reader.readLine()) != null){
                list.add(new ListViewItemModel(tmp));
            }
            reader.close();
        }catch( IOException e ){
            e.printStackTrace();
        }
        Collections.reverse(list);
        return list;
    }
}