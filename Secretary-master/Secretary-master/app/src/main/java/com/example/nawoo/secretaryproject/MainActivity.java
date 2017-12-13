package com.example.nawoo.secretaryproject;

/**
 * Created by dbsal on 2017-11-30.
 */


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private boolean isFragmentSchedule = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getData gdt = new getData();
        gdt.execute();


        // ActionBar에 타이틀 변경
        getSupportActionBar().setTitle("조윤민");
        // ActionBar의 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF891D32));

        // shedule/friends 프래그먼트 변경
        android.app.FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.main_fragment_view, new Fragment_Schedule());
        fragmentTransaction.commit();

        // 프래그먼트 변경 버튼
        Button buttonFragSwitch = (Button)findViewById(R.id.switch_button);
        buttonFragSwitch.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                switchFragment();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // ActionBar에 + 버튼을 클릭했을 때의 동작
        switch(item.getItemId()){
            case R.id.first :
                Toast.makeText(this, "사용자 정보",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        UserInfoActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다
                break;
            case R.id.second :
                Toast.makeText(this, "기능1",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    public void switchFragment(){
        Fragment fr;

        if(isFragmentSchedule){
            fr = new Fragment_Schedule();
        } else {
            fr = new Fragment_Friends();
        }

        isFragmentSchedule = (isFragmentSchedule) ? false : true;

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment_view, fr);
        fragmentTransaction.commit();
    }
    public class getData extends AsyncTask<Void, Integer, Void> {

        HttpURLConnection conn = null;
        @Override
        protected Void doInBackground(Void... unuesd) {
/* 인풋 파라메터값 생성 */
            String param = "u_id=" + SessionControl.loginID  + "&" + "";
            Log.e("POST", param);
            try {
/* 서버연결 */
                /*UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList, "UTF-8");
                httpPost.setEntity(urlEncodedFormEntity);
                HttpResponse httpResponse = httpclient.execute(httpPost);*/


                URL url = new URL(
                        "http://172.30.1.9:80/Secretary/getData.php");
                conn = (HttpURLConnection) url.openConnection();
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
                SessionControl.loginName = buff.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Quit").setMessage("종료 하시겠습니까?").setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    moveTaskToBack(true);
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }).setNegativeButton("No",null).show();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}