package com.example.kazu.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.kazu.myapplication.api.RestClient;
import com.example.kazu.myapplication.model.Judgement;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends Activity implements View.OnClickListener {


    @BindView(R.id.buttonLogin) Button buttonLogin;
    @BindView(R.id.textPasswd) TextView textViewPasswd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        ImageView imageView = (ImageView) findViewById(R.id.gifView);
        GlideDrawableImageViewTarget target = new GlideDrawableImageViewTarget(imageView);
        Glide.with(this).load(R.raw.mario).into(target);

    }

    /**
     * ログインボタン押下時
     * @param view
     */
    @OnClick(R.id.buttonLogin)
    public void onClick(View view) {

        final String textPasswd = textViewPasswd.getText().toString();

        RestClient restClient = new RestClient();
        restClient.auth(textPasswd).enqueue(new Callback<Judgement>() {
            @Override
            public void onResponse(Call<Judgement> call, Response<Judgement> response) {
                if (response.isSuccessful()) {

                    Log.d("*****", response.body().getResult().toString());

                    if(response.body().getResult() == true){

                        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                        startActivity(intent);

                        textViewPasswd.setText("");
                    }

                }else{
                    Log.d("*****", "Not 200");
                }
            }

            @Override
            public void onFailure(Call<Judgement> call, Throwable t) {
                Log.d("*****", "Fail");
            }
        });
    }

}
