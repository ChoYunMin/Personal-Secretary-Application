package com.example.nawoo.secretaryproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

        Button b_join = (Button)findViewById(R.id.user_modify_button);
        b_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        InfoModifyActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });
    }
}
