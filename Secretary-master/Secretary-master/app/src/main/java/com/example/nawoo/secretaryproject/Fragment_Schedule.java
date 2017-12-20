package com.example.nawoo.secretaryproject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static com.example.nawoo.secretaryproject.R.id.btn_fri;
import static com.example.nawoo.secretaryproject.R.id.btn_mon;
import static com.example.nawoo.secretaryproject.R.id.btn_sat;
import static com.example.nawoo.secretaryproject.R.id.btn_sun;
import static com.example.nawoo.secretaryproject.R.id.btn_thu;
import static com.example.nawoo.secretaryproject.R.id.btn_tue;
import static com.example.nawoo.secretaryproject.R.id.btn_wed;


/**
 * Created by dbsal on 2017-12-03.
 */

public class Fragment_Schedule extends Fragment {
    public Fragment_Schedule() {

    }

    private static String TAG = "phptest";
    private static final String TAG_JSON="webnautes";
    private static final String TAG_TITLE="title";
    private static final String TAG_MEMO="memo";
    private static final String TAG_DATE="date";
    private static final String TAG_TIME="time";

    ArrayList<HashMap<String, String>> mArrayList;
    ListView mlistView;
    String mJsonString;

    phpDown task;

    int selDay;

    Button sun,mon,tue,wed,thu,fri,sat;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_schedule, container, false);

        sun = (Button)v.findViewById(btn_sun);
        sun.setOnClickListener(mClickListener);
        mon = (Button)v.findViewById(btn_mon);
        mon.setOnClickListener(mClickListener);
        tue = (Button)v.findViewById(btn_tue);
        tue.setOnClickListener(mClickListener);
        wed = (Button)v.findViewById(btn_wed);
        wed.setOnClickListener(mClickListener);
        thu = (Button)v.findViewById(btn_thu);
        thu.setOnClickListener(mClickListener);
        fri = (Button)v.findViewById(btn_fri);
        fri.setOnClickListener(mClickListener);
        sat = (Button)v.findViewById(btn_sat);
        sat.setOnClickListener(mClickListener);

        v.findViewById(R.id.btn_sun).setSelected(true);
        selDay = 1;

        mlistView = (ListView)v.findViewById(R.id.lsitView_list);
        mArrayList = new ArrayList<>();

        task = new phpDown();
        task.execute("http://211.214.113.144:8888/Dproject/userSchedule.php");

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                //스케줄 눌렀을 때 activity_detail_schedule로 넘어가기
                Intent intent = new Intent(getActivity(), DetailScheduleActivity.class);
                intent.putExtra("schedule", mlistView.getItemAtPosition(position).toString());
                startActivity(intent);

            }
        });

        return v;
    }
    Button.OnClickListener mClickListener = new View.OnClickListener(){
        public void onClick(View v){

            initSelected();


            switch(v.getId()){
                case btn_sun:
                    sun.setSelected(true);
                    selDay = 1;
                    break;
                case btn_mon:
                    mon.setSelected(true);
                    selDay = 2;
                    break;
                case btn_tue:
                    tue.setSelected(true);
                    selDay = 3;
                    break;
                case btn_wed:
                    wed.setSelected(true);
                    selDay = 4;
                    break;
                case btn_thu:
                    thu.setSelected(true);
                    selDay = 5;
                    break;
                case btn_fri:
                    fri.setSelected(true);
                    selDay = 6;
                    break;
                case btn_sat:
                    sat.setSelected(true);
                    selDay = 7;
                    break;
            }


            phpDown exe = new phpDown();
            exe.execute("http://211.214.113.144:8888/Dproject/userSchedule.php");
        }
    };

    private void initSelected(){
        sun.setSelected(false);
        mon.setSelected(false);
        tue.setSelected(false);
        wed.setSelected(false);
        thu.setSelected(false);
        fri.setSelected(false);
        sat.setSelected(false);
    }

    private class phpDown extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getActivity(),
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);

            if (result == null){

               // mTextViewResult.setText(errorString);
            }
            else {

                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];


            try {
                URL url = new URL(serverURL);
                // userID 보내는 경로


                String postData = "USERID=" + SessionControl.loginID;
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(5000);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(postData.getBytes("UTF-8"));
                outputStream.flush();
                //outputStream.close();
                conn.disconnect();


                // 받는 경로
                //HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.connect();


                int responseStatusCode = conn.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = conn.getInputStream();
                }
                else{
                    inputStream = conn.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showResult(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            mArrayList = new ArrayList<>();

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String title = item.getString(TAG_TITLE);
                String memo = item.getString(TAG_MEMO);
                String date = item.getString(TAG_DATE);
                String time = item.getString(TAG_TIME);

                HashMap<String,String> hashMap = new HashMap<>();

                hashMap.put(TAG_TITLE, title);
                hashMap.put(TAG_MEMO, memo);
                hashMap.put(TAG_DATE, date);
                hashMap.put(TAG_TIME, time);

                SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd");
                Date dt = dateFormat.parse(date);

                Calendar cal = Calendar.getInstance();
                cal.setTime(dt);

                int dayNum = cal.get(Calendar.DAY_OF_WEEK);

                if(dayNum == selDay){
                    mArrayList.add(hashMap);
                }
            }

            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), mArrayList, R.layout.item_list,
                    new String[]{TAG_TIME, TAG_TITLE},
                    new int[]{R.id.textView_list_time, R.id.textView_list_title}
            );

            mlistView.setAdapter(adapter);

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        } catch (ParseException e){
            Log.d(TAG, "showResult : ", e);
        }

    }


    /*
    private class back extends AsyncTask<String, Integer, Bitmap>{

        protected Bitmap doInBackground(String... urls) {
            // TODO Auto-generated method stub
            try{
                URL myFileUrl = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection)myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                //String json = DownloadHtml("http://서버주소/appdata.php");
                InputStream is = conn.getInputStream();

                bmImg = BitmapFactory.decodeStream(is);


            }catch(IOException e){
                e.printStackTrace();
            }
            return bmImg;
        }

        protected void onPostExecute(Bitmap img){
            imView.setImageBitmap(bmImg);
        }

    }

    private class phpDown extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... urls){
            StringBuilder jsonHtml = new StringBuilder();
            try{
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if(conn != null){
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);

                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for(;;){
                            String line = br.readLine();
                            if(line == null) break;
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonHtml.toString();
        }

        protected void onPostExecute(String str){
            txtView.setText(str);
        }
    }
    */
}
