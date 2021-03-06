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
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by dbsal on 2017-12-03.
 */

public class Fragment_Friends extends Fragment {
    public Fragment_Friends(){

    }

    private static final String TAG_JSON="webnautes";
    private static final String TAG_USER1="user1";
    private static final String TAG_USER2="user2";
    private static final String TAG_STATUS="status";
    private static final String TAG_USER1_NAME = "u1_name";
    private static final String TAG_USER2_NAME = "u2_name";

    private TextView textView;
    ArrayList<FriendsItem> fArrayList;

    ListView flistView = null;
    String fJsonString;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){


        fArrayList = new ArrayList<>();
        View v = inflater.inflate(R.layout.fragment_friends, container, false);

        textView = (TextView) v.findViewById(R.id.txt);
        flistView = (ListView) v.findViewById(R.id.friends_list_view);

        getFriend gfd = new getFriend();
        gfd.execute();

        flistView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                Intent intent = new Intent(getActivity(), FriendsClicked.class);

                if(fArrayList.get(position).getStatus().equals("apply"))
                {
                    return;
                }

                intent.putExtra("Requester", fArrayList.get(position).getRequester());
                intent.putExtra("Receiver", fArrayList.get(position).getID());
                intent.putExtra("RequesterName", fArrayList.get(position).getU1Name());
                intent.putExtra("ReceiverName", fArrayList.get(position).getU2Name());
                intent.putExtra("Status", fArrayList.get(position).getStatus());

                String chk_user = fArrayList.get(position).getRequester();

                if(chk_user.equals(SessionControl.loginID))
                {
                    CurrentFriend.friendID = fArrayList.get(position).getID();
                    CurrentFriend.friendName = fArrayList.get(position).getU2Name();
                }
                else
                {
                    CurrentFriend.friendID = fArrayList.get(position).getRequester();
                    CurrentFriend.friendName = fArrayList.get(position).getU1Name();
                }

                startActivity(intent);
            }
        });

        return v;
    }

    public class getFriend extends AsyncTask<Void, Integer, String> {

        HttpURLConnection conn = null;

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getActivity(),
                    "Please Wait", null, true, true);
        }
        @Override
        protected String doInBackground(Void... unuesd) {
/* 인풋 파라메터값 생성 */
            String result = "";
            String param = "USERID=" + SessionControl.loginID;
            Log.e("POST", param);
            try {


                URL url = new URL(
                        "http://211.214.113.144:8888/Dproject/getFriend.php");
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
                result = buff.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;

        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //textView.setText(result);
            Log.d(TAG, "response  - " + result);

            if (result == null){

                textView.setText(errorString);
            }
            else {

                fJsonString = result;
                showResult();
            }
        }
    }
    private void showResult() {
        try {

            JSONObject jsonObject = new JSONObject(fJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);



            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String receiver;
                String requester;

                String user1 = item.getString(TAG_USER1);
                String user2 = item.getString(TAG_USER2);
                String status = item.getString(TAG_STATUS);
                String u1_name = item.getString(TAG_USER1_NAME);
                String u2_name = item.getString(TAG_USER2_NAME);

                requester = user1;
                receiver = user2;


                FriendsItem friendsItem = new FriendsItem(requester, receiver, u1_name, u2_name ,status);

                fArrayList.add(friendsItem);
            }
            FriendsAdapter friendsAdapter = new FriendsAdapter(this.getContext(), fArrayList);

            flistView.setAdapter(friendsAdapter);

        }catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }
}
