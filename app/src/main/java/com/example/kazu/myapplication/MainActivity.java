package com.example.kazu.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



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

        AuthApi auth = new AuthApi(this);
        auth.execute(textPasswd);


    }

}
