package com.example.nawoo.secretaryproject;

import android.annotation.SuppressLint;
import android.os.StrictMode;

/**
 * Created by dbsal on 2017-12-15.
 */

public class NetworkUtil {
    @SuppressLint("NewApi")
    static public void setNetworkPolicy(){
        if(android.os.Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
}
