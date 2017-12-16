package com.example.nawoo.secretaryproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.nawoo.secretaryproject.R.id.friend_id;
import static com.example.nawoo.secretaryproject.R.id.friend_name;

/**
 * Created by nawoo on 2017-12-17.
 */

public class FriendsAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<FriendsItem> data;
    private int layout;

    public FriendsAdapter(Context context, int layout, ArrayList<FriendsItem> data){
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layout = layout;
    }

    @Override
    public int getCount(){
        return data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = inflater.inflate(layout,parent,false);
        }
        FriendsItem friendsItem = data.get(position);

        TextView userID = (TextView)convertView.findViewById(friend_id);
        userID.setText(friendsItem.getID());

        TextView status = (TextView)convertView.findViewById(friend_name);
        status.setText(friendsItem.getStatus());

        return convertView;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public String getItem(int position){
        return data.get(position).getID();
    }
}
