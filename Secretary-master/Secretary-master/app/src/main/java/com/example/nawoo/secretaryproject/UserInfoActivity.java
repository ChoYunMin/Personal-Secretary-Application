package com.example.nawoo.secretaryproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class UserInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        TextView textID = (TextView)findViewById(R.id.userID);
        TextView textName = (TextView)findViewById(R.id.userName);

        textID.setText("ID : " + SessionControl.loginID);
        textName.setText("Name : " + SessionControl.loginName);
    }
}
