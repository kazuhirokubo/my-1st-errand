package com.example.kazu.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    }

    @OnClick(R.id.buttonLogin)
    public void onClick(View view) {
        String textPasswd = textViewPasswd.getText().toString();

        if (textPasswd.equals("1234")) {
            startActivity(new Intent(
                    MainActivity.this,
                    Main2Activity.class)
            );
            textViewPasswd.setText("");
        }else{
          Toast.makeText(this, "パスワードが正しくありません", Toast.LENGTH_LONG).show();
        }
    }
}
