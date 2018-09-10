// IMyAudioPlayerService.aidl
package com.example.wumin.myapplication;

// Declare any non-default types here with import statements

interface IMyAudioPlayerService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
            
             //根据pos打开音频文件
                 void openAutio(int position);
                //播放
                  void start();
                //暂停
                   void pause();
                //停止
                 void stop();
                //播放进度
                  int getCurrentPosition();
                //歌曲duration
                  int getDuration();
                //得到singer
                  String getArtist();
                //得到歌名
                 String getName();
                //得到歌曲播放路径
                  String getAudioPath();
                //下一首
                  void next();
                //上一首
                  void pre();
                //设置播放模式
                  void setPlayMode(int playMode);
                //得到播放模式
                  int getPlayMode();
                  //isplaying
                  boolean isPlaying();
                  void seekTo(int position);
                  int getAudioSessionId();

}
