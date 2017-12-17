package com.example.nawoo.secretaryproject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.nawoo.secretaryproject.R.id.btn_accept;

/**
 * Created by nawoo on 2017-12-17.
 */

public class FriendsAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<FriendsItem> data;
    private Context mContext = null;
    private int curPosition;


    public FriendsAdapter(Context context, ArrayList<FriendsItem> data){
        this.mContext = context;
        this.data = data;
    }

    @Override
    public int getCount(){
        return data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if(v == null){
            this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.friends_list,parent,false);
        }

        TextView userID = (TextView)v.findViewById(R.id.friend_set_id);
        userID.setTag(position);

        TextView userName = (TextView)v.findViewById(R.id.friend_set_name);
        userName.setTag(position);

        Button acp_button = (Button)v.findViewById(btn_accept);
        acp_button.setEnabled(false);
        acp_button.setTag(position);
        acp_button.setOnClickListener(buttonClickListener);

        String chk_user = getItem(position).getRequester();

        if(chk_user.equals(SessionControl.loginID))
        {
            userID.setText(getItem(position).getID());
            userName.setText(getItem(position).getU2Name());
        }
        else
        {
            userID.setText(getItem(position).getRequester());
            userName.setText(getItem(position).getU1Name());

            String getStatus = getItem(position).getStatus();
            if(getStatus.equals("apply"))
            {
                acp_button.setVisibility(v.VISIBLE);
                acp_button.setEnabled(true);
            }
        }

        return v;
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){

            switch(v.getId()){
                case btn_accept:
                    modifyFriendDB mfd = new modifyFriendDB();
                    curPosition = (Integer)v.getTag();
                    mfd.execute();
                    break;
            }
        }
    };

    public class modifyFriendDB extends AsyncTask<Void, Integer, Void> {
        String data = "";
        @Override
        protected Void doInBackground(Void... unused) {

/* 인풋 파라메터값 생성 */
            String param = "u_id1=" + getItem(curPosition).getRequester()  + "&u_id2=" + getItem(curPosition).getID() +"";
            try {
/* 서버연결 */
                URL url = new URL(
                        "http://211.214.113.144:8888/Dproject/accept_friend.php");
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
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public FriendsItem getItem(int position){

        return data.get(position);
    }

    @Override
    protected  void finalize() throws Throwable{
        free();
        super.finalize();
    }
    private void free(){
        inflater =  null;
        data = null;
        mContext = null;
    }
}
