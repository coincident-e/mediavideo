package com.example.wumin.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wumin on 2018/8/9.
 */
public class CacheUtils {
    public static void cacheData(Context context,String key,String data){
        SharedPreferences sharedPreferences=context.getSharedPreferences("phone",Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key,data).commit();
    }
    public static   String getData(Context context,String key){
        SharedPreferences sharedPreferences=context.getSharedPreferences("phone",Context.MODE_PRIVATE);
       return sharedPreferences.getString(key,"");
    }
    public static void cachePlayMode(Context context,String key,int mode){
        SharedPreferences sharedPreferences=context.getSharedPreferences("mode",Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(key,mode).commit();
    }
    public static   int getPlayMode(Context context,String key){
        SharedPreferences sharedPreferences=context.getSharedPreferences("mode",Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key,0);
    }
}
