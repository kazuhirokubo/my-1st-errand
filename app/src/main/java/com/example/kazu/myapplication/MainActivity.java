package com.example.kazu.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.core.Main;


public class MainActivity extends Activity implements View.OnClickListener {


    @BindView(R.id.buttonLogin) Button buttonLogin;
    @BindView(R.id.textPasswd) TextView textViewPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    /**
     * ログインボタン押下時
     * @param view
     */
    @OnClick(R.id.buttonLogin)
    public void onClick(View view) {

        final String textPasswd = textViewPasswd.getText().toString();

        AuthApi auth = new AuthApi(this);

//        AuthApi auth =new AuthApi(new AuthInterface() {
//
//            @Override
//            public void processFinish(AuthResponseModel output) {
//
//                if(output.result.equals("true")){
//
//                    startActivity(new Intent(
//                    MainActivity.this,
//                    Main2Activity.class)
//                    );
//                    textViewPasswd.setText("");
//
//                }else{
//                    Toast.makeText(MainActivity.this, "認証エラー", Toast.LENGTH_LONG).show();
//
////                    new AlertDialog.Builder(MainActivity.this)
////                        .setTitle("title")
////                        .setMessage("message")
////                        .setPositiveButton("OK", null)
////                        .show();
//                }
//            }
//        });

        auth.execute(textPasswd);


    }

}
