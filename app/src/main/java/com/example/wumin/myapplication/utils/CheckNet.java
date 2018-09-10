package com.example.wumin.myapplication.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by wumin on 2018/8/13.
 */
public class CheckNet {
    public static boolean isWifiState(Context context){
        boolean isWifi=false;
        ConnectivityManager manager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        int type = activeNetworkInfo.getType();
        if (type==ConnectivityManager.TYPE_WIFI){
            isWifi=true;
        }
        return  isWifi;
    }
}
