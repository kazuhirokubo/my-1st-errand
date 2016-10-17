package com.example.kazu.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;


import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Main2Activity extends Activity {


    @BindView(R.id.listview) ListView mListView;

    private BaseAdapter mAdapter;
    private List<Item> mItemList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.Builder builder = new Configuration.Builder(getBaseContext());
        builder.addModelClass(Item.class);
        ActiveAndroid.initialize(builder.create(), true);

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

        deleteItem();

        finish();
    }

    @OnClick(R.id.buttonAdd)
    protected void addItem(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String nowDateTime = simpleDateFormat.format(date);
        Item item = new Item();
        item.setDateAt(nowDateTime);
        mItemList.add(0, item);
        mAdapter.notifyDataSetChanged();
        writeItem(nowDateTime);
    }

    protected void writeItem(String dateTimeString){

        Item item = new Item();
        item.dateAt = dateTimeString;
        item.save();

    }

    protected void deleteItem(){

        new Delete().from(Item.class).execute();

    }

    protected List<Item> loadItem(){

        List<Item> item = new Select()
            .from(Item.class)
            .orderBy("date_at DESC")
            .execute();

        return item;

    }
}
