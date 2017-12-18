package com.example.nawoo.secretaryproject;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dbsal on 2017-12-18.
 */

public class AlarmReceiver extends AppCompatActivity{

    // 필요한 것: 스케줄 제목과 내용, 타입
    private List<Integer> selectedFunctions = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String memo = intent.getStringExtra("memo");
        int typenum = intent.getIntExtra("typenum", 0);
        for(int a = 0; a<typenum; a++){
            selectedFunctions.add(intent.getIntExtra("type" + String.valueOf(a+1), 0));
        }

        AlertDialog.Builder dig = new AlertDialog.Builder(this);
        dig.setTitle(title);
        dig.setMessage(memo);
        dig.setIcon(R.drawable.ic_launcher);
        dig.setPositiveButton("일정 수행", null);
        dig.show();

        showNotification(this, R.drawable.ic_launcher, title, memo);

        finish();
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
