package com.example.nawoo.secretaryproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/* 이것은 로그인 */

public class SigninActivity extends AppCompatActivity {

    final Context context = this;

    EditText et_id, et_pw;
    String sId, sPw;
    CheckBox autologin;

    SharedPreferences setting;
    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        if(NetworkConnection() == false){
            NotConnected_showAlert();
        }

        et_id = (EditText) findViewById(R.id.siid);
        et_pw = (EditText) findViewById(R.id.sipw);
        autologin = (CheckBox)findViewById(R.id.autoLoginChk);

        setting = getSharedPreferences("setting", 0);
        editor = setting.edit();

        if(setting.getBoolean("autologin", false)){
            et_id.setText(setting.getString("ID", ""));
            et_pw.setText(setting.getString("PW",""));
            autologin.setChecked(true);
        }

        Button b_join = (Button)findViewById(R.id.register_button);
        b_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        JoinActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });
    }

    private NetworkInfo getNetworkInfo() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    private boolean NetworkConnection(){
        NetworkInfo mNetworkState = getNetworkInfo();

        if(mNetworkState != null && mNetworkState.isConnected()){
            if(mNetworkState.getType() == ConnectivityManager.TYPE_WIFI || mNetworkState.getType() == ConnectivityManager.TYPE_MOBILE){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    private void NotConnected_showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SigninActivity.this);
        builder.setTitle("네트워크 연결 오류");
        builder.setMessage("사용 가능한 무선네트워크가 없습니다.\n" + "먼저 무선네트워크 연결상태를 확인해 주세요.")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void bt_Sign_In(View v)
    {
        if(autologin.isChecked()){
            Toast.makeText(this, "로그인", Toast.LENGTH_SHORT).show();
            String ID = et_id.getText().toString();
            String PW = et_pw.getText().toString();
            editor.putString("ID", ID);
            editor.putString("PW", PW);
            editor.putBoolean("autologin", true);
            editor.commit();
        }else{
            editor.clear();
            editor.commit();
        }
        try{
            sId = et_id.getText().toString().trim();
            sPw = et_pw.getText().toString().trim();

        }catch (NullPointerException e)
        {
            Log.e("err",e.getMessage());
        }

        loginDB lDB = new loginDB();
        lDB.execute();

    }

    public class loginDB extends AsyncTask<Void, Integer, Void> {
        String data = "";
        HttpURLConnection conn = null;
        @Override
        protected Void doInBackground(Void... unused) {

/* 인풋 파라메터값 생성 */
            String param = "u_id=" + sId  + "&u_pw=" + sPw +"";
            Log.e("POST", param);
            try {
/* 서버연결 */
                /*UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList, "UTF-8");
                httpPost.setEntity(urlEncodedFormEntity);
                HttpResponse httpResponse = httpclient.execute(httpPost);*/


                URL url = new URL(
                        "http://172.30.1.9:8888/Secretary/SignIn.php");
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

            if(data.equals("1"))
            {
                Log.e("RESULT","성공적으로 처리되었습니다!");

                SessionControl.loginID = sId;
                alertBuilder
                        .setTitle("알림")
                        .setMessage("로그인 성공!")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
            else if(data.equals("0"))
            {
                Log.e("RESULT","비밀번호가 일치하지 않습니다.");
                alertBuilder
                        .setTitle("알림")
                        .setMessage("비밀번호가 일치하지 않습니다.")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
            else
            {
                Log.e("RESULT","에러 발생! ERRCODE = " + data);
                alertBuilder
                        .setTitle("알림")
                        .setMessage("등록중 에러가 발생했습니다! errcode : "+ data)
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
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
