package com.example.nawoo.secretaryproject;

/**
 * Created by dbsal on 2017-11-30.
 */


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private boolean isFragmentSchedule = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ActionBar에 타이틀 변경
        getSupportActionBar().setTitle("조윤민");
        // ActionBar의 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF891D32));

        // shedule/friends 프래그먼트 변경
        android.app.FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.main_fragment_view, new Fragment_Schedule());
        fragmentTransaction.commit();

        // 프래그먼트 변경 버튼
        Button buttonFragSwitch = (Button)findViewById(R.id.switch_button);
        buttonFragSwitch.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                switchFragment();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // ActionBar에 + 버튼을 클릭했을 때의 동작
        return true;
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