package com.example.nawoo.secretaryproject;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by dbsal on 2017-12-18.
 */

public class AlarmReceiver extends AppCompatActivity{

    // 필요한 것: 스케줄 제목과 내용, 타입
    private List<Integer> selectedFunctions = new ArrayList<>();
    int typenum;
    AudioManager mAudioManager;
    String title;
    String memo;
    String date;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // 오디오 매니저 설정
        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        memo = intent.getStringExtra("memo");
        date = intent.getStringExtra("date");
        typenum = intent.getIntExtra("typenum", 0);
        for(int a = 0; a<typenum; a++){
            selectedFunctions.add(intent.getIntExtra("type" + String.valueOf(a+1), 0));
        }

        showNotification(this, R.drawable.ic_launcher, title, memo);

        AlertDialog.Builder dig = new AlertDialog.Builder(this);
        dig.setTitle(title);
        dig.setMessage(memo);
        dig.setIcon(R.drawable.ic_launcher);

        dig.setPositiveButton("일정 수행", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialogInterface, int i){
                for(int a = 0; a<typenum; a++){
                    if(selectedFunctions.get(a) == 1){
                        // 알람 울려주기/ default
                    }
                    else if(selectedFunctions.get(a) == 2){
                        // 날씨 보여주기
                        CurrentSchedule.Date = date;
                        GotoWeatherActivity();
                        finish();
                    }
                    else if(selectedFunctions.get(a) == 3){
                        // 교통상황 보여주기
                    }
                    else if(selectedFunctions.get(a) == 4){
                        // 무음모드로 변경하기
                        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        Toast.makeText(getApplicationContext(), "무음모드로 설정되었습니다!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        dig.show();


        finish();
    }

    private boolean GotoWeatherActivity(){
        Intent intent = new Intent(this, SetWeatherActivity.class);
        startActivity(intent);
        return true;
    }

    private void showNotification(Context context, int statusBarIconID, String statusBarTextID, String detailedTextID) {
        Intent contentIntent = new Intent(context, MainActivity.class);
        PendingIntent theappIntent = PendingIntent.getActivity(context, 0, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notif = new Notification(statusBarIconID, null, System.currentTimeMillis());
        notif.sound = Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "6"); // ringURI
        notif.flags = Notification.FLAG_INSISTENT;
        //notif.setLatestEventInfo(context, from, message, theappIntent);
        notif.ledARGB = Color.GREEN;

        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context)
                .setContentIntent(theappIntent)
                .setSmallIcon(statusBarIconID)
                .setContentTitle(statusBarTextID)
                .setContentText(detailedTextID)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        notif = builder.build();
        nm.notify(1234, notif);
    }
}
