package com.example.nawoo.secretaryproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserInfoActivity extends AppCompatActivity {

    public static Context iContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        iContext = this;

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

        Button b_reset = (Button)findViewById(R.id.user_reset_button);
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(iContext);
        b_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertBuilder
                        .setTitle("알림")
                        .setMessage("적정 수면 시간 : 오후 9:55 적정 기상 시간 : 오전 6:00")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                AlertDialog dialog2 = alertBuilder.create();
                dialog2.show();
            }
        });
    }
}
