package com.example.wumin.myapplication.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wumin.myapplication.R;
import com.example.wumin.myapplication.bean.MediaItem;
import com.example.wumin.myapplication.utils.Utils;
import com.example.wumin.myapplication.view.VideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by wumin on 2018/8/14.
 */

public class DemoSystem extends AppCompatActivity implements View.OnClickListener {
    private static final int UPDATE_UI = 1;
    private static final int NETSPEED = 2;
    private static final int MEDIACONTROLLOER =3 ;
    private TextView tv_loading;
    private LinearLayout ll_loading;
    private LinearLayout ll_buffer;
    private TextView tv_buffer;
    private RelativeLayout mediacontroller;
    private VideoView videoView;
    private LinearLayout llTop;
    private TextView tvName;
    private ImageView ivBattery;
    private TextView tvSystemtime;
    private Button btnVoice;
    private SeekBar seekbarVoice;
    private Button btnSwitch;
    private LinearLayout llBottom;
    private TextView tvCurrenttime;
    private SeekBar seekbarVideo;
    private TextView tvTotaltime;
    private Button btnExit;
    private Button btnVideoPre;
    private Button btnVideoStartPause;
    private Button btnVideoNext;
    private Button btnVideoSwitchScreen;


    //数据
    //本地视频列表
    private ArrayList<MediaItem> mediaItems = new ArrayList<>();
    //网络视频地址
    private String uri;
    private int position;
    private boolean isnetUri;
    private boolean isUseSystem;
    private MediaItem item;

    //videoview 当前播放位置
    private int currentPosition;
    //videoview 上一秒播放位置
    private int precurrentposition;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MEDIACONTROLLOER:
                    hideMediaController();
                    handler.removeMessages(MEDIACONTROLLOER);
                case NETSPEED:
                    String netSpeed = utils.getNetSpeed(DemoSystem.this);
                    tv_buffer.setText("正在缓冲中..." + netSpeed);
                    tv_loading.setText("拼命加载中..." + netSpeed);
                    handler.removeMessages(NETSPEED);
                    handler.sendEmptyMessageDelayed(NETSPEED, 2000);
                    break;
                case UPDATE_UI:
                    //更新 系统时间 seekbarvideo进度  tvcurrent 当前显示的播放进度
                    tvSystemtime.setText(getSystemTime());
                    currentPosition = videoView.getCurrentPosition();
                    seekbarVideo.setProgress(currentPosition);
                    tvCurrenttime.setText(utils.stringForTime(currentPosition));
                    //如果是网络资源 加载第二进度条
                    if (isnetUri) {
                        int bufferPercentage = videoView.getBufferPercentage();
                        seekbarVideo.setSecondaryProgress(bufferPercentage * seekbarVideo.getMax() / 100);
                    } else {
                        seekbarVideo.setSecondaryProgress(0);
                    }
                    //自己的方法判断当前网络是否卡顿
                    if (isUseSystem && videoView.isPlaying()) {
                        if (videoView.isPlaying()) {
                            int delta = currentPosition - precurrentposition;
                            if (delta < 500) {
                                ll_buffer.setVisibility(View.VISIBLE);

//                                tv_buffer.setText("缓冲中" + utils.getNetSpeed(DemoSystem.this));
                            } else {
                                ll_buffer.setVisibility(View.GONE);
                            }

                        } else {
                            ll_buffer.setVisibility(View.GONE);
                        }
                        precurrentposition = currentPosition;
                    }
                    handler.removeMessages(UPDATE_UI);
                    handler.sendEmptyMessageDelayed(UPDATE_UI, 1000);
                    break;
            }
        }
    };
    private GestureDetector gestureDetector;
    private int maxVolume;

    private String getSystemTime() {
        return new SimpleDateFormat().format(new Date());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_video);
        findView();
        registerBatteryBroad();
        getIntentData();
        initData();
        initGestureDetector();
        setData();
        setListener();
    }

    private void initGestureDetector() {
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override//mediacontroller
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (isshowMediacontroller){
                    hideMediaController();
                    handler.removeMessages(MEDIACONTROLLOER);
                }else{
                    showMediaController();
                    handler.sendEmptyMessageDelayed(MEDIACONTROLLOER,4000);
                }
                return super.onSingleTapConfirmed(e);
            }

            @Override //start and pause
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                startAndPause();
            }

            @Override //isfullscreen
            public boolean onDoubleTap(MotionEvent e) {
                return super.onDoubleTap(e);
            }
        });
    }

    //videoView  seekbar监听
    private void setListener() {
        videoView.setOnPreparedListener(new MyOnPreparedListener());
        videoView.setOnCompletionListener(new MyOnCompletionListener());
        videoView.setOnErrorListener(new MyOnErrorListener());
        if (!isUseSystem) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                videoView.setOnInfoListener(new MyOnInfoListener());
            }
        }
        seekbarVoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
if (fromUser){
    if (progress<=0){
        updateVolume(0,true);
    }else{
        updateVolume(progress,false);
    }

}
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
handler.removeMessages(MEDIACONTROLLOER);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
handler.sendEmptyMessageDelayed(MEDIACONTROLLOER,4000);
            }
        });
        seekbarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {

        private int videoWidth;
        private int videoHeight;

        @Override
        public void onPrepared(MediaPlayer mp) {
            //准备好开始播放
            videoView.start();
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
            long duration = item.getDuration();
            tvTotaltime.setText(utils.stringForTime((int) duration));
            seekbarVideo.setMax((int) duration);
            videoWidth = mp.getVideoWidth();
            videoHeight = mp.getVideoHeight();
            //loading页面不可见
            ll_loading.setVisibility(View.GONE);
            //         //隐藏控制面板
            hideMediaController();
            // todo        //videoview 设置成默认播放 非全屏
            //todo        //setBtnStates() 检查按钮状态 在setdata 之后就判断

            handler.sendEmptyMessage(UPDATE_UI);
        }
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {

        }
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return false;
        }
    }

    class MyOnInfoListener implements MediaPlayer.OnInfoListener {

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {

            } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {

            }
            return false;
        }
    }


    //videoView 设置播放地址
    private void setData() {
        if (mediaItems != null && mediaItems.size() > 0) {
            item = mediaItems.get(position);
            String data = item.getData();
            isnetUri = utils.isNetUri(data);
            tvName.setText(item.getName());
            videoView.setVideoPath(data);
        } else if (uri != null) {
            isnetUri = utils.isNetUri(uri);
            tvName.setText(uri.toString());
            videoView.setVideoPath(uri);
        } else {
            Toast.makeText(DemoSystem.this, "没有传递数据", Toast.LENGTH_SHORT).show();
        }
        //videoview 设置播放源之后就初始化界面按钮
        //TODO setbtnState()
    }

    private int screenWidth;
    private int screenHeight;
    private int currentVolume;
    private Utils utils;
    private AudioManager audioManager;

    //初始化界面数据  得到屏幕宽高  得到系统音量
    private void initData() {
        utils = new Utils();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekbarVoice.setMax(maxVolume);
        seekbarVoice.setProgress(currentVolume);
    }

    private void getIntentData() {
        //接收intent传递来的数据
        mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("mediaitem");
        position = getIntent().getIntExtra("position", 0);
        uri = getIntent().getStringExtra("uri");
    }

    //监听电池电量广播   手机开锁屏 只能动态注册才能收到广播
    private MyReceiver receiver;

    private void registerBatteryBroad() {
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);
            if (level <= 0) {
                ivBattery.setImageResource(R.drawable.ic_battery_0);
            } else if (level <= 10) {
                ivBattery.setImageResource(R.drawable.ic_battery_10);
            } else if (level <= 20) {
                ivBattery.setImageResource(R.drawable.ic_battery_20);
            } else if (level <= 40) {
                ivBattery.setImageResource(R.drawable.ic_battery_40);
            } else if (level <= 60) {
                ivBattery.setImageResource(R.drawable.ic_battery_60);
            } else if (level <= 80) {
                ivBattery.setImageResource(R.drawable.ic_battery_80);
            } else if (level <= 100) {
                ivBattery.setImageResource(R.drawable.ic_battery_100);
            }
        }
    }

    private void findView() {
        tv_loading = (TextView) findViewById(R.id.tv_loading);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        ll_buffer = (LinearLayout) findViewById(R.id.ll_buffer);
        tv_buffer = (TextView) findViewById(R.id.tv_buffer);
        mediacontroller = (RelativeLayout) findViewById(R.id.mediacontroller);
        videoView = (VideoView) findViewById(R.id.videoview_system);
        llTop = (LinearLayout) findViewById(R.id.ll_top);
        tvName = (TextView) findViewById(R.id.tv_name);
        ivBattery = (ImageView) findViewById(R.id.iv_battery);
        tvSystemtime = (TextView) findViewById(R.id.tv_systemtime);
        btnVoice = (Button) findViewById(R.id.btn_voice);
        seekbarVoice = (SeekBar) findViewById(R.id.seekbar_voice);
        btnSwitch = (Button) findViewById(R.id.btn_switch);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        tvCurrenttime = (TextView) findViewById(R.id.tv_currenttime);
        seekbarVideo = (SeekBar) findViewById(R.id.seekbar_video);
        tvTotaltime = (TextView) findViewById(R.id.tv_totaltime);
        btnExit = (Button) findViewById(R.id.btn_exit);
        btnVideoPre = (Button) findViewById(R.id.btn_video_pre);
        btnVideoStartPause = (Button) findViewById(R.id.btn_video_start_pause);
        btnVideoNext = (Button) findViewById(R.id.btn_video_next);
        btnVideoSwitchScreen = (Button) findViewById(R.id.btn_video_switch_screen);

        btnVoice.setOnClickListener(this);
        btnSwitch.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnVideoPre.setOnClickListener(this);
        btnVideoStartPause.setOnClickListener(this);
        btnVideoNext.setOnClickListener(this);
        btnVideoSwitchScreen.setOnClickListener(this);
        //界面初始化好之后就开始更新网速
        handler.sendEmptyMessage(NETSPEED);
    }

    //是否静音
    private boolean isMute;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_voice: //是否静音
                isMute = !isMute;
                updateVolume(currentVolume, isMute);
                break;
            case R.id.btn_switch://切换播放器
                showSwitchDialog();
                break;
            case R.id.btn_exit:
                finish();
                break;
            case R.id.btn_video_pre:
                break;
            case R.id.btn_video_start_pause:
                startAndPause();
                break;
            case R.id.btn_video_next:
                playnextVideo();
                break;
            case R.id.btn_video_switch_screen:
                break;
        }
        handler.removeMessages(MEDIACONTROLLOER);
        handler.sendEmptyMessageDelayed(MEDIACONTROLLOER,4000);
    }

    private void playnextVideo() {
       if (mediaItems!=null&&mediaItems.size()>0){
        position++;
           if (position<mediaItems.size()){
               item=mediaItems.get(position);
               tvName.setText(item.getName());
               isnetUri=utils.isNetUri(item.getData());
               videoView.setVideoPath(item.getData());
               setbtnState();
           }

       }else if(uri!=null){
           setbtnState();
       }
    }

    private void setbtnState() {
        if (mediaItems!=null&&mediaItems.size()>0){
            if (mediaItems.size()==1){
                //setenable(false);
            }else if (mediaItems.size()==2){
                if (position==0){
                    // pre  no   next yes
                }else if(position==1){
                    //next  no  pre yes
                }
            }else{
                if (position==0){
                    //pre  no
                }else if (position==mediaItems.size()-1){
                    //next no
                }else{
                    //setenable true
                }
            }

        }else if (uri!=null){
            //setEnable(false);
        }
    }

    private void startAndPause() {
        if (videoView.isPlaying()){
            videoView.pause();
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_start_selector);
        }else{
            videoView.start();
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector);
        }
    }

    private void showSwitchDialog() {
        AlertDialog builder=new AlertDialog.Builder(this).setTitle("系统提示").setMessage("是否切换到万能播放器").setPositiveButton("是的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //startactivity()
                //intent 传data
            }
        }).setNegativeButton("取消",null).show();
    }

    private boolean isshowMediacontroller;

    private  void showMediaController(){
        isshowMediacontroller=true;
        llTop.setVisibility(View.VISIBLE);
        llBottom.setVisibility(View.VISIBLE);
    }
    private  void hideMediaController(){
        isshowMediacontroller=false;
        llTop.setVisibility(View.GONE);
        llBottom.setVisibility(View.GONE);
    }
    private void updateVolume(int progress, boolean isMute) {
        if (isMute) {//静音
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            seekbarVoice.setProgress(0);
        } else {//非静音
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            seekbarVoice.setProgress(progress);
            currentVolume = progress;
        }
    }

    //监听手机物理按键  改变音量
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
            currentVolume--;
            updateVolume(currentVolume,false);
            handler.removeMessages(MEDIACONTROLLOER);
            handler.sendEmptyMessageDelayed(MEDIACONTROLLOER,4000);
        }else if (keyCode==KeyEvent.KEYCODE_VOLUME_UP){
            currentVolume++;
           updateVolume(currentVolume,false);
            handler.removeMessages(MEDIACONTROLLOER);
            handler.sendEmptyMessageDelayed(MEDIACONTROLLOER,4000);
        }
        return super.onKeyDown(keyCode, event);
    }

    //重写触摸屏幕事件  返回  实现滑动改变音量 和亮度
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                handler.removeMessages(MEDIACONTROLLOER);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case  MotionEvent.ACTION_UP:
                handler.sendEmptyMessageDelayed(MEDIACONTROLLOER,4000);
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        //反注册广播
        if (videoView != null) {
            videoView = null;
        }
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}
