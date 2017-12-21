package com.example.nawoo.secretaryproject;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

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

    // 스케줄 제목과 내용
    private EditText title, memo;

    // 반복 횟수
    private String repeatnum;

    // 원하는 기능 type
    private List<Integer> selectedFunctions = new ArrayList<>();

    /*
    통지 관련 맴버 변수
    */
    private NotificationManager mNotification;

    @RequiresApi(api = Build.VERSION_CODES.N)
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

        // 일시 설정 클래스로 현재 시각을 설정
        mDate = (DatePicker)findViewById(R.id.schedule_datePicker);
        mDate.init(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), this);
        mTime = (TimePicker)findViewById(R.id.schedule_timePicker);
        mTime.setHour(mCalendar.get(Calendar.HOUR_OF_DAY));
        mTime.setMinute(mCalendar.get(Calendar.MINUTE));
        mTime.setOnTimeChangedListener(this);

        // 스케줄 제목과 메모 저장
        title = (EditText)findViewById(R.id.schedule_title);
        memo = (EditText)findViewById(R.id.schedule_memo);

        // 셋 버튼, 취소버튼 리스너 등록
        Button b = (Button)findViewById(R.id.btn_set_schedule);
        b.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                new Thread(){
                    public void run(){
                        try {
                            setAlarm();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        b = (Button)findViewById(R.id.btn_cancel_add_schedule);
        b.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                cancelAlarm();
            }
        });

        // 반복횟수 지정 라디오 다이얼로그
        b = (Button)findViewById(R.id.btn_set_repeat);
        b.setOnClickListener(new View.OnClickListener(){
           public void onClick(View v){
               showSelectRepeatDialog();
           }
        });

        // 원하는 기능 설정
        b = (Button)findViewById(R.id.btn_choose_function);
        b.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                showSelectFunctionDialog();
            }
        });

    }

    // 알람의 설정
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setAlarm() throws MalformedURLException {
        // AlarmManager 호출
        //mManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        if(repeatnum == "반복 없음"){
            mManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent());
        }
        else if(repeatnum == "매일"){ // 매일반복
            mManager.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), 86400000, pendingIntent());
        }
        else if(repeatnum == "매주"){ // 매주반복
            mManager.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), 604800000, pendingIntent());
        }


        Log.i("AlarmActivity!", mCalendar.getTime().toString());

        // user schedule 저장
        // 디비에 저장할 형식에 맞춰서 변환
        String date = Integer.toString(mDate.getYear()) + Integer.toString(mDate.getMonth() + 1) + Integer.toString(mDate.getDayOfMonth());
        String time = Integer.toString(mTime.getHour()) + ":" + Integer.toString(mTime.getMinute()) + ":" + "00";

        try{
            PHPRequest request = new PHPRequest("http://211.214.113.144:8888/Dproject/add_schedule.php");
            String result = request.AddSchedule(SessionControl.loginID, title.getText().toString(), memo.getText().toString(), date, time);
            if(result.equals("1")){
                //Toast.makeText(getApplication(), "들어감", Toast.LENGTH_SHORT).show();
            }
            else{
                //Toast.makeText(getApplication(), "안들어감", Toast.LENGTH_SHORT).show();
            }
        }
        catch(MalformedURLException e){
            e.printStackTrace();
        }


        // schedule type에도 저장
        for(int a = 0; a<selectedFunctions.size(); a++){
            try{
                PHPRequest request2 = new PHPRequest("http://211.214.113.144:8888/Dproject/add_schedule_type.php");
                String result = request2.AddScheduleType(title.getText().toString(), selectedFunctions.get(a));
                if(result.equals("1")){
                    //Toast.makeText(getApplication(), "들어감", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Toast.makeText(getApplication(), "안들어감", Toast.LENGTH_SHORT).show();
                }
            }
            catch(MalformedURLException e){
                e.printStackTrace();
            }
        }
        finish();
    }

    // 알람의 해제
    private void cancelAlarm(){
        mManager.cancel(pendingIntent());
        finish();
    }

    //알람 설정 시간에 발생하는 인텐트 작성
    private PendingIntent pendingIntent(){
        Intent i = new Intent(getApplicationContext(), AlarmReceiver.class);
        i.putExtra("title", title.getText().toString());
        i.putExtra("memo", memo.getText().toString());
        i.putExtra("date", Integer.toString(mDate.getYear()) + "-" + Integer.toString(mDate.getMonth() + 1) + "-" + Integer.toString(mDate.getDayOfMonth()));
        i.putExtra("typenum", selectedFunctions.size());
        for(int a = 0; a < selectedFunctions.size(); a++){
            i.putExtra("type" + String.valueOf(a+1), selectedFunctions.get(a));
        }
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
        return pi;
    }

    // 일자 설정 클래스의 상태변화 리스너
    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set (year, monthOfYear, dayOfMonth, mTime.getHour(), mTime.getMinute());
        Log.i("HelloAlarmActivity", mCalendar.getTime().toString());
    }

    // 시각 설정 클래스의 상태변화 리스너
    @Override
    public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
        mCalendar.set(mDate.getYear(), mDate.getMonth(), mDate.getDayOfMonth(), hourOfDay, minute);
        Log.i("HelloAlarmActivity", mCalendar.getTime().toString());
    }

    // 반복 횟수 지정하는 라디오 다이얼로그
    private void showSelectRepeatDialog() {
        final List<String> ListItems = new ArrayList<>();
        ListItems.add("반복 없음");
        ListItems.add("매일");
        ListItems.add("매주");
        final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

        // 선택된 아이템 (기본값 0)
        final List SelectedItems = new ArrayList();
        int defaultItem = 0;
        SelectedItems.add(defaultItem);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("반복 횟수 선택");
        builder.setSingleChoiceItems(items, defaultItem, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                SelectedItems.clear();
                SelectedItems.add(which);
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which){
                    repeatnum="반복 없음";
                    if(!SelectedItems.isEmpty()){
                        int index = (int)SelectedItems.get(0);
                        repeatnum = ListItems.get(index);
                    }

                }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
           public void onClick(DialogInterface dialog, int which){

           }
        });
        builder.show();
    }

    // 원하는 기능 선택하는 체크박스 다이얼로그
    private void showSelectFunctionDialog(){
        final boolean [] chkList = {false, false, false, false};
        final String [] chkStrList = {"알람 울려주기", "날씨 보여주기", "교통상황 보여주기", "무음모드 전환"};

        new AlertDialog.Builder(this)
                .setTitle("원하는 기능 선택")
                .setCancelable(false)
                .setMultiChoiceItems(chkStrList, chkList, new DialogInterface.OnMultiChoiceClickListener(){
                    public void onClick(DialogInterface dialogInterface, int i, boolean b){
                        chkList[i] = b;
                    }
                }
        ).setPositiveButton("확인", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialogInterface, int i){
                for(int k = 1; k <= chkList.length; k++){
                    if(chkList[k-1]){
                        // type을 저장하는 list에 선택된 타입들 저장
                        selectedFunctions.add(k);
                        addBtn(k);
                    }
                }
            }
        }).show();



    }

    // LinearLayout params 정의(동적 버튼 생성)
    private void addBtn(final int index){
        /*if(index == 2){ // 날씨정보
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout btnLayout = (LinearLayout)findViewById(R.id.linearlayout_set_function);
            Button btnSetFunction = new Button(this);
            btnSetFunction.setText("날씨 정보 보기");
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(0, 0, 0, 20);
            btnSetFunction.setBackgroundResource(R.color.pastel);
            btnSetFunction.setTextColor(getResources().getColor(R.color.white));
            btnSetFunction.setTextSize(20);
            // 버튼이 눌리면
            btnSetFunction.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    newIntent(index);
                }
            });
            btnSetFunction.setLayoutParams(params);
            btnLayout.addView(btnSetFunction);
        }*/

        /*if(index == 3){ // 교통정보
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout btnLayout = (LinearLayout)findViewById(R.id.linearlayout_set_function);
            Button btnSetFunction2 = new Button(this);
            btnSetFunction2.setText("교통상황 보기 정보 설정하기");
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(0, 0, 0, 20);
            btnSetFunction2.setBackgroundResource(R.color.pastel);
            btnSetFunction2.setTextColor(getResources().getColor(R.color.white));
            btnSetFunction2.setTextSize(20);
            // 버튼이 눌리면
            btnSetFunction2.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    newIntent(index);
                }
            });
            btnSetFunction2.setLayoutParams(params);
            btnLayout.addView(btnSetFunction2);
        }*/
    }

    private void newIntent(int index){
        if(index == 2){
            Intent setFunctionIntent = new Intent(this, SetWeatherActivity.class);
            startActivity(setFunctionIntent);
        }
        else if(index == 3){
            Intent setFunctionIntent = new Intent(this, SetTrafficActivity.class);
            startActivity(setFunctionIntent);
        }

    }
}
