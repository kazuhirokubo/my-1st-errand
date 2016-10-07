package com.example.kazu.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.ListView;
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

        mAdapter = new ListViewAdapter(this.getApplicationContext(), R.layout.listview_row, mItemList);

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
