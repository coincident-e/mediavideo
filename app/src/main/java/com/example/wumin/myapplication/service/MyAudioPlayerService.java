package com.example.wumin.myapplication.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.wumin.myapplication.IMyAudioPlayerService;
import com.example.wumin.myapplication.R;
import com.example.wumin.myapplication.activity.AudioPlayerActivity;
import com.example.wumin.myapplication.bean.MediaItem;
import com.example.wumin.myapplication.utils.CacheUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;

public class MyAudioPlayerService extends Service {
    public static final String AUDIOMEDIA = "com.android.audio.mediaplayer";
    private ArrayList<MediaItem> list = new ArrayList<>();
    private int position;
    //用于播放音乐
    private MediaPlayer mediaPlayer;
    private MediaItem mediaItem;
    private NotificationManager manager;

private  int playmode;
    //顺序播放
    public static final int REPEAT_NORMAL=1;
    //单曲循环
    public static final int REPEAT_SINGLE=2;
    //循环全部
    public static final int REPEAT_ALL=3;

    public MyAudioPlayerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.playmode=CacheUtils.getPlayMode(this,"mode");
        getAudioDataFromLoad();
    }

    private void getAudioDataFromLoad() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                ContentResolver contentResolver = getContentResolver();

                String[] projection = {MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ARTIST
                };
                Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        MediaItem media = new MediaItem();
                        String name = cursor.getString(0);
                        media.setName(name);
                        long duration = cursor.getLong(1);
                        media.setDuration(duration);
                        long size = cursor.getLong(2);
                        media.setSize(size);
                        String data = cursor.getString(3);
                        media.setData(data);
                        String artist = cursor.getString(4);
                        media.setArtist(artist);
                        list.add(media);
                    }
                }
                cursor.close();
            }
        }.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyIBinder();
    }

    class MyIBinder extends IMyAudioPlayerService.Stub {
        MyAudioPlayerService service = MyAudioPlayerService.this;

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
        }

        @Override
        public void openAutio(int position) throws RemoteException {
            service.openAutio(position);
        }

        @Override
        public void start() throws RemoteException {
            service.start();
        }

        @Override
        public void pause() throws RemoteException {
            service.pause();
        }

        @Override
        public void stop() throws RemoteException {
            service.stop();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return service.getCurrentPosition();
        }

        @Override
        public int getDuration() throws RemoteException {
            return service.getDuration();
        }

        @Override
        public String getArtist() throws RemoteException {
            return service.getArtist();
        }

        @Override
        public String getName() throws RemoteException {
            return service.getName();
        }

        @Override
        public String getAudioPath() throws RemoteException {
            return service.getAudioPath();
        }

        @Override
        public void next() throws RemoteException {
            service.next();
        }

        @Override
        public void pre() throws RemoteException {
            service.pre();
        }

        @Override
        public void setPlayMode(int playMode) throws RemoteException {
            service.setPlayMode(playMode);
        }

        @Override
        public int getPlayMode() throws RemoteException {
            return service.getPlayMode();
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return service.isPlaying();
        }

        @Override
        public void seekTo(int position) throws RemoteException {
            mediaPlayer.seekTo(position);
        }

        @Override
        public int getAudioSessionId() throws RemoteException {
            return mediaPlayer.getAudioSessionId();
        }
    }

    //根据pos打开音频文件
    private void openAutio(int position) {
        this.position = position;
        if (list != null && list.size() > 0) {
            mediaItem = list.get(position);
            if (mediaPlayer != null) {
                mediaPlayer.reset();
            }
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
                mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());
                mediaPlayer.setOnErrorListener(new MyOnErrorListener());
                mediaPlayer.setDataSource(mediaItem.getData());
                mediaPlayer.prepareAsync();
                if (playmode==MyAudioPlayerService.REPEAT_SINGLE){
                    mediaPlayer.setLooping(true);
                }else{
                    mediaPlayer.setLooping(false);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(MyAudioPlayerService.this, "没有数据", Toast.LENGTH_SHORT).show();
        }

    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            //准备好之后发广播通知activity获取歌曲信息
//            notifyChange(AUDIOMEDIA);
            EventBus.getDefault().post(mediaItem);
            start();
        }
    }

    private void notifyChange(String action) {
        Intent intent=new Intent(action);
        sendBroadcast(intent);
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            next();
        }
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            next();
            return true;
        }
    }

    //播放
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void start() {
        //发送通知 显示歌曲正在播放 点击通知进入activity
         manager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent=new Intent(this, AudioPlayerActivity.class);
        //标记是从通知进入activity的
        intent.putExtra("notification",true);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification=new Notification.Builder(this).setSmallIcon(R.drawable.notification_music_playing)
                .setContentTitle("321音乐")
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setOngoing(false)
                .setContentText(mediaItem.getName()).build();
        manager.notify(1,notification);
        mediaPlayer.start();
    }

    //暂停
    private void pause() {
mediaPlayer.pause();
        manager.cancel(1);
    }

    //停止
    private void stop() {
mediaPlayer.stop();
    }

    //播放进度
    private int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    //歌曲duration
    private int getDuration() {
        return mediaPlayer.getDuration();
    }

    //得到singer
    private String getArtist() {
        if (TextUtils.isEmpty( mediaItem.getArtist())){
            return "未知艺术家";
        }
        return mediaItem.getArtist() ;
    }

    //得到歌名
    private String getName() {
        return mediaItem.getName();
    }

    //得到歌曲播放路径
    private String getAudioPath() {
        return mediaItem.getData();
    }

    //下一首
    private void next() {

        changeNextPosition();
        openNextAudio();
    }

    private void openNextAudio() {
        int playMode = getPlayMode();
        if (playMode == MyAudioPlayerService.REPEAT_NORMAL) {
          if (position<list.size()){
              openAutio(position);
          }else{
              position=list.size()-1;
          }

        } else if (playMode == MyAudioPlayerService.REPEAT_SINGLE) {
            openAutio(position);
        } else if (playMode == MyAudioPlayerService.REPEAT_ALL) {
            openAutio(position);
        } else {
            if (position<list.size()){
                openAutio(position);
            }else{
                position=list.size()-1;
            }
        }
    }

    private void changeNextPosition() {
      int  playMode=getPlayMode();
        if (playMode==MyAudioPlayerService.REPEAT_NORMAL){
          position++;
        }else if (playMode==MyAudioPlayerService.REPEAT_SINGLE){
           position++;
            if (position>=list.size()){
                position=0;
            }
        }else if (playMode==MyAudioPlayerService.REPEAT_ALL){
            position++;
            if (position>=list.size()){
                position=0;
            }
        }else{
            position++;
        }

    }

    //上一首
    private void pre() {
setPrePosition();
        openPreAudio();
    }

    private void openPreAudio() {
        int playMode = getPlayMode();
        if (playMode == MyAudioPlayerService.REPEAT_NORMAL) {
            if (position<0){
                position=0;
            }else{
               openAutio(position);
            }

        } else if (playMode == MyAudioPlayerService.REPEAT_SINGLE) {
            openAutio(position);
        } else if (playMode == MyAudioPlayerService.REPEAT_ALL) {
            openAutio(position);
        } else {
            if (position<0){
                position=0;
            }else{
                openAutio(position);
            }
        }
    }

    private void setPrePosition() {
        int  playMode=getPlayMode();
        if (playMode==MyAudioPlayerService.REPEAT_NORMAL){
            position--;
        }else if (playMode==MyAudioPlayerService.REPEAT_SINGLE){
            position--;
            if (position<0){
                position=list.size()-1;
            }
        }else if (playMode==MyAudioPlayerService.REPEAT_ALL){
            position--;
            if (position<0){
                position=list.size()-1;
            }
        }else{
            position--;
        }
    }

    //设置播放模式
    private void setPlayMode(int playMode) {
this.playmode=playMode;
        CacheUtils.cachePlayMode(this,"mode",playMode);
        if (playmode==MyAudioPlayerService.REPEAT_SINGLE){
            mediaPlayer.setLooping(true);
        }else{
            mediaPlayer.setLooping(false);

        }
    }

    //得到播放模式
    private int getPlayMode() {
        return this.playmode;
    }
    //是否在播放
    private boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }
}
