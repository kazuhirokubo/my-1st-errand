package com.example.kazu.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kazu.myapplication.api.RestClient;
import com.example.kazu.myapplication.model.CreatedItem;
import com.example.kazu.myapplication.model.Item;
import com.example.kazu.myapplication.model.Judgement;



import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class Main2Activity extends Activity {

    private static final EventBus BUS = new EventBus();

    @BindView(R.id.listview) ListView mListView;
    @BindView(R.id.title) TextView textTitle;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Context mContext;
    private ListViewAdapter mAdapter;
    private String mUserName;

    @Override
    public void onStart() {
        super.onStart();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onStop() {
        BusProvider.getInstance().unregister(this);
        super.onStop();

    }

    @Subscribe(sticky = true)
    public void onEvent(MessageEvent event){

        Log.d("onEvent", event.msg);

        mUserName = event.msg;
        textTitle.setText(mUserName);
    }

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

        RestClient restClient = new RestClient();
        restClient.deleteItems().enqueue(new Callback<Judgement>() {
            @Override
            public void onResponse(Call<Judgement> call, Response<Judgement> response) {
                if (response.isSuccessful())
                {

                }else{

                }
            }

            @Override
            public void onFailure(Call<Judgement> call, Throwable t) {

            }
        });

        finish();
    }

    /**
     * [追加]ボタン押下
     */
    @OnClick(R.id.buttonAdd)
    protected void addItem(){

        PostItemApi postItem = new PostItemApi(this);
        postItem.response();

        loadItem();

    }

    protected void loadItem(){

        GetItemsApi getItems = new GetItemsApi(this, mListView);
        getItems.request();

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
    public class GetItemsApi {

        public GetItemsApi(Context context, ListView listView){
            mContext = context;
            mListView = listView;
        }

        public void request(){

            RestClient restClient = new RestClient();
            restClient.getItems().enqueue(new Callback<List<Item>>() {

                @Override
                public void onResponse(Call <List<Item>> call, Response <List<Item>> response){

                    if (response.isSuccessful())
                    {
                        mAdapter = new ListViewAdapter(mContext, R.layout.listview_row, response.body());
                        mListView.setAdapter(mAdapter);
                    }else{

                    }
                }

                @Override
                public void onFailure(Call<List<Item>> call, Throwable t) {
                    Log.d("GetItemsApi", "Not 200");
                }
            });
        }
    }

    /*====================================================================================================
     * itemを追加する
     *
     *
     ====================================================================================================*/

    public class PostItemApi {

        public PostItemApi(Context context){
            mContext = context;
        }

        public void response(){
            RestClient restClient = new RestClient();
            restClient.postItem("").enqueue(new Callback<CreatedItem>() {
                @Override
                public void onResponse(Call<CreatedItem> call, Response<CreatedItem> response) {
                    if (response.isSuccessful())
                    {

                    }else{

                    }
                }

                @Override
                public void onFailure(Call<CreatedItem> call, Throwable t) {

                }
            });
        }
    }
}
