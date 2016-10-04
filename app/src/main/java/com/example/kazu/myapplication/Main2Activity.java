package com.example.kazu.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Main2Activity extends Activity implements View.OnClickListener {

    private RecyclerView.Adapter mAdapter;
    private List<ModelList> mList;
    private ModelList mModel;

    @BindView(R.id.buttonAdd) Button buttonAdd;
    @BindView(R.id.buttonLogout) Button buttonLogout;
    @BindView(R.id.recyclerview_list) RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main2);

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_main2_title_bar);

        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mList = loadItem();

        if (mList != null) {
            mAdapter = new RecyclerAdapter(mList);
            recyclerView.setAdapter(mAdapter);
        }
    }

    @OnClick({ R.id.buttonAdd, R.id.buttonLogout })
    public void onClick(View view) {
        if (view.getId() == buttonAdd.getId()){
            addItem();
            writeItem();
        }else if(view.getId() == buttonLogout.getId()){
            resetItem();
            finish();
        }else{

        }
    }

    protected void addItem(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        if(mList==null) {
            mList = new ArrayList<ModelList>();
        }

        mModel = new ModelList(simpleDateFormat.format(date));
        mList.add(0, mModel);


        mAdapter = new RecyclerAdapter(mList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyItemInserted(0);



    }

    protected void resetItem(){
        getSharedPreferences("Array", Context.MODE_PRIVATE).edit().clear().commit();
    }

    protected void writeItem(){
        Gson gson = new Gson();
        String textList = gson.toJson(mList);

        SharedPreferences sharedPref = getSharedPreferences("Array", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("list", textList).commit();
    }


    protected ArrayList<ModelList> loadItem(){
        ArrayList<ModelList> list = new ArrayList<ModelList>();
        try {
            String textList = getSharedPreferences("Array", Context.MODE_PRIVATE)
                    .getString("list", "");

            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<ModelList>>(){}.getType();
            list = gson.fromJson(textList, listType);

        }catch(Exception ex){

        }

        return list;
    }

    private static final class RecyclerAdapter extends RecyclerView.Adapter {
        private List<ModelList> mItemList;

        private RecyclerAdapter(final List<ModelList> itemList) {
            mItemList = itemList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final TextView textItem = (TextView) holder.itemView.findViewById(R.id.item_name);
            textItem.setText(mItemList.get(position).getDatetime());
        }

        @Override
        public int getItemCount() {
            return mItemList.size();
        }

        private static class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView mTextView;

            private ViewHolder(View v) {
                super(v);
                mTextView = (TextView) v.findViewById(R.id.item_name);
            }
        }
    }
}
