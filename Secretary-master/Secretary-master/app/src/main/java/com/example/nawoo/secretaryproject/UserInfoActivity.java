package com.example.nawoo.secretaryproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserInfoActivity extends AppCompatActivity {

    public static Context iContext;

    SimpleDateFormat dateFormat;
    Date wu;
    Date sp;
    Date properWakeUp;
    Date properSleep;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);


        SessionControl.wakeUp = "07:00:00";
        SessionControl.sleep = "20:00:00";

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

                try{
                    dateFormat = new SimpleDateFormat("HH:mm:ss");
                    properWakeUp = dateFormat.parse("07:00:00");
                    wu = dateFormat.parse(SessionControl.wakeUp);
                    properSleep = dateFormat.parse("22:00:00");
                    sp = dateFormat.parse(SessionControl.sleep);
                }catch(ParseException e)
                {

                }

                Calendar ppWakeUp = Calendar.getInstance();
                Calendar WakUp = Calendar.getInstance();
                Calendar ppSleep = Calendar.getInstance();
                Calendar slep = Calendar.getInstance();
                ppWakeUp.setTime(properWakeUp);
                WakUp.setTime(wu);
                ppSleep.setTime(properSleep);
                slep.setTime(sp);

                long ppWakeUpTime = ppWakeUp.getTimeInMillis()/3600000;
                long WakUpTime = WakUp.getTimeInMillis()/3600000;
                long ppSleepTime = ppSleep.getTimeInMillis()/3600000;
                long slepTime = slep.getTimeInMillis()/3600000;

                long calSleep = ppSleepTime - slepTime;
                calSleep = calSleep * 1000 * 60 * 5;
                calSleep = ppSleep.getTimeInMillis() - calSleep;

                final String sleepString = DateFormat.format("HH:mm:ss", new Date(calSleep)).toString();

                long calWake = ppWakeUpTime - WakUpTime;
                calWake = calWake * 1000 * 60 * 5;
                calWake = ppWakeUp.getTimeInMillis() - calWake;

                final String wakeUpString = DateFormat.format("HH:mm:ss", new Date(calWake)).toString();

                SessionControl.wakeUp = wakeUpString;
                SessionControl.sleep = sleepString;

                alertBuilder
                        .setTitle("알림")
                        .setMessage("적정 수면 시간 :" + sleepString +  "     적정 기상 시간 : " + wakeUpString)
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
