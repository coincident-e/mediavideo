package com.example.wumin.myapplication;

import android.app.Application;

import com.iflytek.cloud.SpeechUtility;

import org.xutils.x;

/**
 * Created by wumin on 2018/8/3.
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SpeechUtility.createUtility(MyApp.this, "appid=" + "5b6ea1f1");
        x.Ext.init(this);
    }
}
