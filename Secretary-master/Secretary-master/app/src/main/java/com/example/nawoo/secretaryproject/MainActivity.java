package com.example.nawoo.secretaryproject;

/**
 * Created by dbsal on 2017-11-30.
 */


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private boolean isFragmentSchedule = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.app.FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.main_fragment_view, new Fragment_Schedule());
        fragmentTransaction.commit();

        Button buttonFragSwitch = (Button)findViewById(R.id.switch_button);
        buttonFragSwitch.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                switchFragment();
            }
        });

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
}