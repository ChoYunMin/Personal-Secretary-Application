package com.example.nawoo.secretaryproject;

/**
 * Created by dbsal on 2017-11-30.
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_sun).setOnClickListener(mClickListener);
        findViewById(R.id.btn_mon).setOnClickListener(mClickListener);
        findViewById(R.id.btn_tue).setOnClickListener(mClickListener);
        findViewById(R.id.btn_wed).setOnClickListener(mClickListener);
        findViewById(R.id.btn_thu).setOnClickListener(mClickListener);
        findViewById(R.id.btn_fri).setOnClickListener(mClickListener);
        findViewById(R.id.btn_sat).setOnClickListener(mClickListener);
    }

    Button.OnClickListener mClickListener = new View.OnClickListener(){
        public void onClick(View v){

        }
    };
}