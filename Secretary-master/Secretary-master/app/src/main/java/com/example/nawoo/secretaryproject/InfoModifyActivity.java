package com.example.nawoo.secretaryproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InfoModifyActivity extends AppCompatActivity implements TimePicker.OnTimeChangedListener{

    final Context context = this;

    // 설정 일시
    private GregorianCalendar wCalendar;
    private GregorianCalendar sCalendar;

    String wtimestr;
    String stimestr;

    private TimePicker wTime;
    private TimePicker sTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_modify);

        wCalendar = new GregorianCalendar();
        sCalendar = new GregorianCalendar();

        wTime = (TimePicker)findViewById(R.id.WakeUpTime);
        wTime.setHour(wCalendar.get(Calendar.HOUR_OF_DAY));
        wTime.setMinute(wCalendar.get(Calendar.MINUTE));
        wTime.setOnTimeChangedListener(this);

        sTime = (TimePicker)findViewById(R.id.SleepTime);
        sTime.setHour(sCalendar.get(Calendar.HOUR_OF_DAY));
        sTime.setMinute(sCalendar.get(Calendar.MINUTE));
        sTime.setOnTimeChangedListener(this);



    }

    public void bt_Modify_ok(View view)
    {

        wtimestr = Integer.toString(wTime.getHour()) + ":" + Integer.toString(wTime.getMinute()) + ":" + "00";
        stimestr = Integer.toString(sTime.getHour()) + ":" + Integer.toString(sTime.getMinute()) + ":" + "00";

        SessionControl.wakeUp = wtimestr;
        SessionControl.sleep = stimestr;

        modifyDB rdb = new modifyDB();
        rdb.execute();
    }

    // 시각 설정 클래스의 상태변화 리스너
    @Override
    public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
    }

    public class modifyDB extends AsyncTask<Void, Integer, Void> {
        String data = "";

        @Override
        protected Void doInBackground(Void... unused) {

/* 인풋 파라메터값 생성 */
            String param = "u_id=" + SessionControl.loginID + "&w_time=" + wtimestr + "&s_time=" + stimestr + "";
            try {
/* 서버연결 */
                URL url = new URL(
                        "http://211.214.113.144:8888/Dproject/InfoModify.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

/* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

/* 서버 -> 안드로이드 파라메터값 전달 */
                InputStream is = null;
                BufferedReader in = null;

                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();
                Log.e("RECV DATA", data);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        /* 서버에서 응답 */
            Log.e("RECV DATA", data);

            if (data.equals("succeed")) {
                Log.e("RESULT", "성공적으로 처리되었습니다!");
                alertBuilder
                        .setTitle("알림")
                        .setMessage("성공적으로 수정되었습니다!")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            } else {
                Log.e("RESULT", "에러 발생! ERRCODE = " + data);
                alertBuilder
                        .setTitle("알림")
                        .setMessage(data)
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
        }
    }
}
