package com.example.nawoo.secretaryproject;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

public class FriendsClicked extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_clicked);

        android.app.FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.friend_fragment_view, new Friend_Schedule());
        fragmentTransaction.commit();


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        if(event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if(keyCode == KeyEvent.KEYCODE_BACK)
            {
                CurrentFriend.friendID = "";
                CurrentFriend.friendName = "";
            }
        }

        return super.onKeyDown(keyCode,event);
    }
}
