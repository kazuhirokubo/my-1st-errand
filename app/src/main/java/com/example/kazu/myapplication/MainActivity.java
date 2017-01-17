package com.example.kazu.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.kazu.myapplication.api.RestClient;
import com.example.kazu.myapplication.model.Judgement;
import com.example.kazu.myapplication.validation.Common;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends Activity implements View.OnClickListener {

    private static final EventBus BUS = new EventBus();

    @BindView(R.id.buttonLogin) Button buttonLogin;
    @BindView(R.id.textUserName) TextView textViewUserName;
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

        final String userName = textViewUserName.getText().toString();
        final String textPasswd = textViewPasswd.getText().toString();

        boolean isMailAddress = Common.isMailAddress(userName);
        boolean isRequiredLength = Common.isRequiredLength(textPasswd);

        if (isMailAddress == true && isRequiredLength == true) {

            RestClient restClient = new RestClient();
            restClient.auth(textPasswd).enqueue(new Callback<Judgement>() {
                @Override
                public void onResponse(Call<Judgement> call, Response<Judgement> response) {

                    if (response.isSuccessful()) {

                        Log.d("*****", response.body().getResult().toString());

                        if (response.body().getResult() == true) {

                            BusProvider.getInstance().postSticky(new MessageEvent(textViewUserName.getText().toString()));

                            Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                            startActivity(intent);

                            textViewPasswd.setText("");
                        }

                    } else {
                        Log.d("*****", "Not 200");
                    }
                }

                @Override
                public void onFailure(Call<Judgement> call, Throwable t) {
                    Log.d("*****", "Fail");
                }
            });

        }else{
            if (isMailAddress == false) {

                AlertDialog.Builder alertMailAddress = new AlertDialog.Builder(MainActivity.this);
                alertMailAddress.setMessage("メールアドレスを入力してください");
                alertMailAddress.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textViewUserName.setText("");
                        textViewUserName.requestFocus();
                    }
                });
                alertMailAddress.show();
                return;
            }

            if (isRequiredLength == false) {

                AlertDialog.Builder alertMailAddress = new AlertDialog.Builder(MainActivity.this);
                alertMailAddress.setMessage("パスワードは４〜8文字で入力してください");
                alertMailAddress.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textViewPasswd.setText("");
                        textViewPasswd.requestFocus();
                    }
                });
                alertMailAddress.show();
                return;
            }

        }
    }

}
