package com.example.kazu.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by kubox on 2016/10/07.
 */


public class ListViewAdapter extends BaseAdapter {

    private Context mContext;
    private ListViewAdapter mAdapter;
    @BindView(R.id.listview)
    ListView mListView;

    private LayoutInflater mInflater;
    private List<ItemModel> mItemList;
    private int mLayoutId;
    private int mPosition;

    static class ViewHolder {
        TextView id;
        TextView body;
        EditText created_at;
        TextView updated_at;
    }

    public ListViewAdapter(Context context, int itemLayoutId, List<ItemModel> itemList){

        mInflater = LayoutInflater.from(context);
        mContext = context;
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
            holder.created_at = (EditText) convertView.findViewById(R.id.created_at);
            holder.updated_at = (TextView) convertView.findViewById(R.id.updated_at);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        /**
         * 削除ボタン押下
         */
        Button deleteItemButton = (Button)convertView.findViewById(R.id.buttonDelete);
        deleteItemButton.setTag(position);

        deleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = (Integer) v.getTag();
                String id = mItemList.get(position).id;

                Log.d("=====TEST=====", String.valueOf(position));
                Log.d("=====TEST=====", "削除ボタン押下：id " + id + " を削除する");
                DeleteItemApi delItem = new DeleteItemApi(id);
                mItemList.remove(position);
//                mAdapter.notifyDataSetChanged();
                delItem.execute();

            }
        });

        /**
         * 反映ボタン押下
         */
        Button updateItemButton = (Button)convertView.findViewById(R.id.buttonUpdate);
        updateItemButton.setTag(position);

        updateItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int position = (Integer) v.getTag();
                String id = mItemList.get(position).id;
//                String body = mItemList.get(position).body;
                String body = "FixedString";

                Log.d("=====TEST=====", "反映ボタン押下：id " + id + "「" + body + "」を反映する");
            }
        });

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

    /*====================================================================================================
     * itemを削除する
     *
     *
     ====================================================================================================*/

    public class DeleteItemApi extends AsyncTask<String, Void, String> {

        private String mId;
        public DeleteItemApi(String id) {
            mId = id;
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
                    "http://dev.domus.jp/kubox/practice/public/item/"+ mId).openConnection();
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
                    .setMessage("APIエラー(DELETE Item)")
                    .setPositiveButton("OK", null)
                    .show();
                mAdapter = new ListViewAdapter(mContext, R.layout.listview_row, null);
                mListView.setAdapter(mAdapter);
            } else {
                Gson gson = new Gson();
                res = gson.fromJson(result, AuthResponseModel.class);
                Log.d("=====TEST=====","VVVVV");
                mAdapter = new ListViewAdapter(mContext, R.layout.listview_row, mItemList);
                mListView.setAdapter(mAdapter);
            }


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

}

