package com.example.kazu.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ボタンの取得
        Button buttonLogin = (Button)findViewById(R.id.buttonLogin);
        // リスナーの登録
        buttonLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        TextView textViewPasswd = (TextView) findViewById(R.id.textPasswd);
        String textPasswd = textViewPasswd.getText().toString();

        if (view.getId() == R.id.buttonLogin && textPasswd.equals("1234")) {
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
