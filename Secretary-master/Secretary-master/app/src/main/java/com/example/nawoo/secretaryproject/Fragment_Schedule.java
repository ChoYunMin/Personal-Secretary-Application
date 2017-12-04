package com.example.nawoo.secretaryproject;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Created by dbsal on 2017-12-03.
 */

public class Fragment_Schedule extends Fragment {
    public Fragment_Schedule() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_schedule, container, false);

        v.findViewById(R.id.btn_sun).setOnClickListener(mClickListener);
        v.findViewById(R.id.btn_mon).setOnClickListener(mClickListener);
        v.findViewById(R.id.btn_tue).setOnClickListener(mClickListener);
        v.findViewById(R.id.btn_wed).setOnClickListener(mClickListener);
        v.findViewById(R.id.btn_thu).setOnClickListener(mClickListener);
        v.findViewById(R.id.btn_fri).setOnClickListener(mClickListener);
        v.findViewById(R.id.btn_sat).setOnClickListener(mClickListener);

        return v;
    }

    Button.OnClickListener mClickListener = new View.OnClickListener(){
        public void onClick(View v){

        }
    };
}
