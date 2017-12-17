package com.example.nawoo.secretaryproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by nawoo on 2017-12-17.
 */

public class FriendsAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<FriendsItem> data;
    private Context mContext = null;


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
        userID.setText(getItem(position).getID());

        TextView status = (TextView)v.findViewById(R.id.friend_set_name);
        status.setText(getItem(position).getStatus());

        Button acp_button = (Button)v.findViewById(R.id.btn_accept);
        acp_button.setEnabled(true);
        acp_button.setTag(position);
        acp_button.setOnClickListener(buttonClickListener);

        String getStatus = getItem(position).getStatus();

        if(!getStatus.equals("apply"))
        {
            acp_button.setVisibility(v.INVISIBLE);
            acp_button.setEnabled(false);
        }

        return v;
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch(v.getId()){
                case R.id.btn_accept:
                    Toast.makeText(mContext,"버튼 Tag = " + v.getTag(),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

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
