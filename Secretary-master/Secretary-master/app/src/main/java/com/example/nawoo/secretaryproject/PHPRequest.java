package com.example.nawoo.secretaryproject;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by dbsal on 2017-12-15.
 */

public class PHPRequest {
    private URL url;

    public PHPRequest(String url) throws MalformedURLException{this.url = new URL(url);}

    private String readStream(InputStream in) throws IOException {
        StringBuilder jsonHtml = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = null;

        while((line = reader.readLine()) != null)
            jsonHtml.append(line);

        reader.close();
        return jsonHtml.toString();
    }

    // 유저스케줄 데이터베이스에 저장
    public String AddSchedule(final String userID, final String title, final String memo, final String date, final String time){
        try{
            String postData = "USERID=" + userID + "&" + "SCHEDULENAME=" + title + "&" + "SCHEDULEMEMO=" + memo + "&" + "DATE=" + date + "&" + "TIME=" + time;
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(postData.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            String result = readStream(conn.getInputStream());
            conn.disconnect();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("PHPRequest", "request was failed.");
            return null;
        }
    }

    // 스케줄 타입도 저장
    public String AddScheduleType(final String title, final Integer type){
        try{
            String postData = "SCHEDULENAME=" + title + "&" + "SCHEDULETYPE=" + type;
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(postData.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            String result = readStream(conn.getInputStream());
            conn.disconnect();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("PHPRequest", "request was failed.");
            return null;
        }
    }


    // 스케줄 삭제
    public String DeleteSchedule(final String userID, final String title){
        try{
            String postData = "USERID=" + userID + "&" + "SCHEDULENAME=" + title;
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(postData.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            String result = readStream(conn.getInputStream());
            conn.disconnect();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("PHPRequest", "request was failed.");
            return null;
        }
    }
}
