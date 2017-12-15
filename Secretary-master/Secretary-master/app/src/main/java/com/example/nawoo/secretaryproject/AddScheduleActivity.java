package com.example.nawoo.secretaryproject;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

/**
 * Created by dbsal on 2017-12-12.
 */

public class AddScheduleActivity extends AppCompatActivity implements DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener{
    ///// 새로운 스케줄 등록

    // 알람 매니저
    private AlarmManager mManager;
    // 설정 일시
    private GregorianCalendar mCalendar;
    // 일자 설정 클래스
    private DatePicker mDate;
    // 시작 설정 클래스
    private TimePicker mTime;

    /*
    통지 관련 맴버 변수
    */
    private NotificationManager mNotification;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        // 통지 매니저를 취득
        mNotification = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // 알람 매니저를 취득
        mManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        // 현재 시각을 취득
        mCalendar = new GregorianCalendar();
        Log.i("HelloAlarmActivity", mCalendar.getTime().toString());
        // 셋 버튼, 취소버튼 리스너 등록
        Button b = (Button)findViewById(R.id.btn_set_schedule);
        b.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setAlarm();

            }
        });

        b = (Button)findViewById(R.id.btn_cancel_add_schedule);
        b.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                cancelAlarm();
            }
        });

        // 일시 설정 클래스로 현재 시각을 설정
        mDate = (DatePicker)findViewById(R.id.schedule_datePicker);
        mDate.init(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), this);
        mTime = (TimePicker)findViewById(R.id.schedule_timePicker);
        mTime.setCurrentHour(mCalendar.get(Calendar.HOUR_OF_DAY));
        mTime.setCurrentMinute(mCalendar.get(Calendar.MINUTE));
        mTime.setOnTimeChangedListener(this);

    }

    // 알람의 설정
    private void setAlarm(){
        // AlarmManager 호출
        //mManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        mManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent());

        Log.i("AlarmActivity!", mCalendar.getTime().toString());

        finish();
    }

    // 알람의 해제
    private void cancelAlarm(){
        mManager.cancel(pendingIntent());
        finish();
    }

    //알람 설정 시간에 발생하는 인텐트 작성
    private PendingIntent pendingIntent(){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
        return pi;
    }

    // 일자 설정 클래스의 상태변화 리스너
    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set (year, monthOfYear, dayOfMonth, mTime.getCurrentHour(), mTime.getCurrentMinute());
        Log.i("HelloAlarmActivity", mCalendar.getTime().toString());
    }

    // 시각 설정 클래스의 상태변화 리스너
    @Override
    public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
        mCalendar.set(mDate.getYear(), mDate.getMonth(), mDate.getDayOfMonth(), hourOfDay, minute);
        Log.i("HelloAlarmActivity", mCalendar.getTime().toString());
    }
}
