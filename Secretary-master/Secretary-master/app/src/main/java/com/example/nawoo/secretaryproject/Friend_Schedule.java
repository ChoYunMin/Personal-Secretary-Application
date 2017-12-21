package com.example.nawoo.secretaryproject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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

import static com.example.nawoo.secretaryproject.R.id.f_btn_fri;
import static com.example.nawoo.secretaryproject.R.id.f_btn_mon;
import static com.example.nawoo.secretaryproject.R.id.f_btn_sat;
import static com.example.nawoo.secretaryproject.R.id.f_btn_sun;
import static com.example.nawoo.secretaryproject.R.id.f_btn_thu;
import static com.example.nawoo.secretaryproject.R.id.f_btn_tue;
import static com.example.nawoo.secretaryproject.R.id.f_btn_wed;

/**
 * Created by nawoo on 2017-12-17.
 */

public class Friend_Schedule extends Fragment {
    public Friend_Schedule(){

    }

    private static String TAG = "getFriendSchedule";
    private static final String TAG_JSON="webnautes";
    private static final String TAG_TITLE="title";
    private static final String TAG_MEMO="memo";
    private static final String TAG_DATE="date";
    private static final String TAG_TIME="time";

    private TextView mTextViewResult;
    ArrayList<HashMap<String, String>> mArrayList;
    ListView mlistView;
    String mJsonString;

    TextView friendsSchedule;

    getFriendSchedule task;
    TextView txtView;

    int selDay;

    Button sun,mon,tue,wed,thu,fri,sat;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.friend_schedule, container, false);

        friendsSchedule = (TextView)v.findViewById(R.id.FriendScheduleListText);

        String friends = CurrentFriend.friendName + "'s Schedule";

        friendsSchedule.setText(friends);

        sun = (Button)v.findViewById(f_btn_sun);
        sun.setOnClickListener(mClickListener);
        mon = (Button)v.findViewById(f_btn_mon);
        mon.setOnClickListener(mClickListener);
        tue = (Button)v.findViewById(f_btn_tue);
        tue.setOnClickListener(mClickListener);
        wed = (Button)v.findViewById(f_btn_wed);
        wed.setOnClickListener(mClickListener);
        thu = (Button)v.findViewById(f_btn_thu);
        thu.setOnClickListener(mClickListener);
        fri = (Button)v.findViewById(f_btn_fri);
        fri.setOnClickListener(mClickListener);
        sat = (Button)v.findViewById(f_btn_sat);
        sat.setOnClickListener(mClickListener);

        v.findViewById(f_btn_sun).setSelected(true);
        selDay = 1;

        mlistView = (ListView)v.findViewById(R.id.friends_listView_list);
        mArrayList = new ArrayList<>();

        task = new getFriendSchedule();
        txtView = (TextView)v.findViewById(R.id.errorTextView);
        task.execute("http://211.214.113.144:8888/Dproject/userSchedule.php");

        return v;
    }

    Button.OnClickListener mClickListener = new View.OnClickListener(){
        public void onClick(View v){
            initSelected();


            switch(v.getId()){
                case f_btn_sun:
                    sun.setSelected(true);
                    selDay = 1;
                    break;
                case f_btn_mon:
                    mon.setSelected(true);
                    selDay = 2;
                    break;
                case f_btn_tue:
                    tue.setSelected(true);
                    selDay = 3;
                    break;
                case f_btn_wed:
                    wed.setSelected(true);
                    selDay = 4;
                    break;
                case f_btn_thu:
                    thu.setSelected(true);
                    selDay = 5;
                    break;
                case f_btn_fri:
                    fri.setSelected(true);
                    selDay = 6;
                    break;
                case f_btn_sat:
                    sat.setSelected(true);
                    selDay = 7;
                    break;
            }


            getFriendSchedule exe = new getFriendSchedule();
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

    private class getFriendSchedule extends AsyncTask<String, Void, String> {
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

                mTextViewResult.setText(errorString);
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


                String postData = "USERID=" + CurrentFriend.friendID;
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
                    getActivity(), mArrayList, R.layout.friend_schedule_list,
                    new String[]{TAG_TIME, TAG_TITLE},
                    new int[]{R.id.friend_textView_list_time, R.id.friend_textView_list_title}
            );

            mlistView.setAdapter(adapter);

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        } catch (ParseException e){
            Log.d(TAG, "showResult : ", e);
        }

    }

}

