package com.example.kazu.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Main2Activity extends Activity implements View.OnClickListener {

    private RecyclerView.Adapter adapter;
    private ArrayList<String> list;

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
        list = loadItem();

        adapter = new RecyclerAdapter(list);

        recyclerView.setAdapter(adapter);
    }


    @OnClick({ R.id.buttonAdd, R.id.buttonLogout })
    public void onClick(View view) {
        if (view.getId() == buttonAdd.getId()){
            Toast.makeText(this, "追加ボタンが押されました", Toast.LENGTH_LONG).show();
            addItem();
        }else if(view.getId() == buttonLogout.getId()){
            deleteFile("item.txt");
            finish();
        }else{

        }
    }


    protected void addItem(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        list.add(0, simpleDateFormat.format(date));
        writeItem(simpleDateFormat.format(date));
        adapter.notifyItemInserted(0);
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

    protected ArrayList<String> loadItem(){
        ArrayList<String> list = new ArrayList();
        try{
            FileInputStream in = openFileInput("item.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader( in , "UTF-8"));
            String tmp;
            while((tmp = reader.readLine()) != null){
                list.add(tmp);

            }
            reader.close();
        }catch( IOException e ){
            e.printStackTrace();
        }
        return list;
    }

    private static final class RecyclerAdapter
            extends RecyclerView.Adapter {
        private List mItemList = new ArrayList();

        private RecyclerAdapter(final List itemList) {
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
            textItem.setText(mItemList.get(position).toString());
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
