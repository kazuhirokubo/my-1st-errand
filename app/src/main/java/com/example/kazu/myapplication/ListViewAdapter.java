package com.example.kazu.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kubox on 2016/10/07.
 */


public class ListViewAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ItemModel> mItemList;
    private int mLayoutId;

    static class ViewHolder {
        TextView id;
        TextView body;
        TextView created_at;
        TextView updated_at;
    }

    public ListViewAdapter(Context context, int itemLayoutId, List<ItemModel> itemList ){

        mInflater = LayoutInflater.from(context);
        mLayoutId = itemLayoutId;
        mItemList = itemList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;


        if (convertView == null) {
            Log.d("convertView == null()", "position= "+position);
            convertView = mInflater.inflate(mLayoutId, null);
            holder = new ViewHolder();
            holder.id = (TextView) convertView.findViewById(R.id.id);
            holder.body = (TextView) convertView.findViewById(R.id.body);
            holder.created_at = (TextView) convertView.findViewById(R.id.created_at);
            holder.updated_at = (TextView) convertView.findViewById(R.id.updated_at);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Log.d("testUserName", mItemList.get(position).created_at);

        holder.id.setText(mItemList.get(position).id);
        holder.body.setText(mItemList.get(position).body);
        holder.created_at.setText(mItemList.get(position).created_at);
        holder.updated_at.setText(mItemList.get(position).updated_at);
        return convertView;
    }

    @Override
    public int getCount() {
        if(mItemList==null){
            return 0;
        }else {
            return mItemList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}

