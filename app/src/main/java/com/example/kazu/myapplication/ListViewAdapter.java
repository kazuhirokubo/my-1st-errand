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
    private List<ListViewItemModel> mItemList;
    private int mLayoutId;

    static class ViewHolder {
        TextView date;
    }

    public ListViewAdapter(Context context, int itemLayoutId, List<ListViewItemModel> itemList ){

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
            holder.date = (TextView) convertView.findViewById(R.id.textViewDate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Log.d("testUserName", mItemList.get(position).getDate());

        holder.date.setText(mItemList.get(position).getDate());
        return convertView;
    }

    @Override
    public int getCount() {
        return mItemList.size();
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

