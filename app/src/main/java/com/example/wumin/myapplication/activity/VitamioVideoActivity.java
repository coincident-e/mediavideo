package com.example.wumin.myapplication.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
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
import android.view.WindowManager;
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
import com.example.wumin.myapplication.view.VitamioVideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;

public class VitamioVideoActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, View.OnClickListener {

    private  static  final  int SHOW_SPEED=3;
    //播放之前盖住整个屏幕的布局
    private  LinearLayout ll_loading;
    private TextView tv_loading;

    //是否使用系统封装判断网络卡顿
    private  boolean isuseSystem;
    //当前播放进度
    private  int currentposition;
    //上一次播放进度
    private  int precurrentposition;
    private AudioManager am;
    private int currentVoice;
    private int maxVoice;
    private static final int MEDIA_CONTROLLER = 2;
    private static final int DEFAULT = 1;
    private static final int FULL = 2;
    private GestureDetector detector;
    private static final int PROGRESS = 1;
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
    private Uri uri;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-08-06 23:06:27 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private RelativeLayout mediacontroller;
    private MyReceiver receiver;
    private int videoHeight;
    private int videoWidth;
    private boolean isnetUri;
private  LinearLayout ll_buffer;
    private  TextView tv_buffer;
    private void findViews() {
        tv_loading= (TextView) findViewById(R.id.tv_loading);
        ll_loading= (LinearLayout) findViewById(R.id.ll_loading);
        ll_buffer= (LinearLayout) findViewById(R.id.ll_buffer);
        tv_buffer= (TextView) findViewById(R.id.tv_buffer);
        mediacontroller = (RelativeLayout) findViewById(R.id.mediacontroller);
        videoView = (VitamioVideoView) findViewById(R.id.videoview_system);
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
        //开始更新网速
        handler.sendEmptyMessage(SHOW_SPEED);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2018-08-06 23:06:27 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private boolean isMute = false;

    @Override
    public void onClick(View v) {
        if (v == btnVoice) {
            // Handle clicks for btnVoice
            isMute = !isMute;
            updateVoice(currentVoice, isMute);

        } else if (v == btnSwitch) {
            // Handle clicks for btnSwitch
            //切换到系统播放器
            showSwitchPlayerDialog();
        } else if (v == btnExit) {
            finish();
            // Handle clicks for btnExit
        } else if (v == btnVideoPre) {
            // Handle clicks for btnVideoPre
            playPreVideo();

        } else if (v == btnVideoStartPause) {
            startAndPause();

            // Handle clicks for btnVideoStartPause
        } else if (v == btnVideoNext) {//
            //播放下一个
            playNextVideo();
            // Handle clicks for btnVideoNext
        } else if (v == btnVideoSwitchScreen) {
            // Handle clicks for btnVideoSwitchScreen
            setFullScreenAndDefault();
        }
        handler.removeMessages(MEDIA_CONTROLLER);
        handler.sendEmptyMessageDelayed(MEDIA_CONTROLLER, 4000);
    }

    private void showSwitchPlayerDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(VitamioVideoActivity.this);
        builder.setTitle("Vitamio提示").setMessage("播放视频花屏时，请尝试切换系统播放器").setIcon(android.R.drawable.stat_notify_error).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switctSystemPlayer();
            }
        }).setNegativeButton("取消",null).show();
    }

    private void switctSystemPlayer() {
        Intent intent=new Intent(this,SystemVideoActivity.class);
        if (mediaItems!=null&&mediaItems.size()>0){
            Bundle bundle=new Bundle();
            bundle.putSerializable("medialist",mediaItems);
            intent.putExtras(bundle);
            intent.putExtra("position",position);
        }else if(uri!=null){
            intent.setData(uri);
        }
        startActivity(intent);
        finish();
    }

    private void startAndPause() {
        if (videoView.isPlaying()) {
            videoView.pause();
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_start_selector);
        } else {
            videoView.start();
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector);
        }
    }

    private void playPreVideo() {
        if (mediaItems != null && mediaItems.size() > 0) {
            position--;
            if (position >= 0) {
                MediaItem mediaItem = mediaItems.get(position);
                tvName.setText(mediaItem.getName());
                isnetUri = utils.isNetUri(mediaItem.getData());
                ll_loading.setVisibility(View.GONE);
                videoView.setVideoPath(mediaItem.getData());
                setBtnStatus();
            }
        } else if (uri != null) {
            setBtnStatus();
        }

    }

    private void playNextVideo() {
        if (mediaItems != null && mediaItems.size() > 0) {
            position++;
            if (position < mediaItems.size()) {
                MediaItem mediaItem = mediaItems.get(position);
                tvName.setText(mediaItem.getName());
                isnetUri = utils.isNetUri(mediaItem.getData());
                ll_loading.setVisibility(View.GONE);
                videoView.setVideoPath(mediaItem.getData());
                setBtnStatus();
            }
        } else if (uri != null) {
            setBtnStatus();
        }
    }

    private void setBtnStatus() {
        if (mediaItems != null && mediaItems.size() > 0) {
            if (mediaItems.size() == 1) {
                setEnable(false);
            } else if (mediaItems.size() == 2) {
                if (position == 0) {
                    btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
                    btnVideoPre.setEnabled(false);
                    btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
                    btnVideoNext.setEnabled(true);
                } else if (position == 1) {
                    btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
                    btnVideoNext.setEnabled(false);
                    btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
                    btnVideoPre.setEnabled(true);
                }
            } else {
                if (position == 0) {
                    btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
                    btnVideoPre.setEnabled(false);
                } else if (position == mediaItems.size() - 1) {
                    btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
                    btnVideoNext.setEnabled(false);
                } else {
                    setEnable(true);
                }
            }
        } else if (uri != null) {
            setEnable(false);
        }

    }

    private void setEnable(boolean is) {
        if (is) {
            btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
            btnVideoNext.setEnabled(true);
            btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
            btnVideoPre.setEnabled(true);
        } else {
            btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
            btnVideoNext.setEnabled(false);
            btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
            btnVideoPre.setEnabled(false);
        }
    }


    private VitamioVideoView videoView;
    Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vitamio.isInitialized(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_vitamio_video);
        registerBroad();
        findViews();
        initData();
        getData();
        setData();
        setListener();

//        videoView.setMediaController(new MediaController(this));
    }

    private void registerBroad() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        receiver = new MyReceiver();
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
            } else {
                ivBattery.setImageResource(R.drawable.ic_battery_100);
            }
        }
    }

    private void getData() {
        uri = getIntent().getData();
        position = getIntent().getIntExtra("position", 0);
        mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("medialist");
    }

    private ArrayList<MediaItem> mediaItems;
    private int position;

    private void setData() {

        if (mediaItems != null && mediaItems.size() > 0) {
            MediaItem item = mediaItems.get(position);
            isnetUri = utils.isNetUri(item.getData());
            videoView.setVideoPath(item.getData());
            tvName.setText(item.getName());
        } else if (uri != null) {
            tvName.setText(uri.toString());
            isnetUri = utils.isNetUri(uri.toString());
            videoView.setVideoURI(uri);

        } else {
            Toast.makeText(VitamioVideoActivity.this, "没有传递数据", Toast.LENGTH_SHORT).show();
        }
        setBtnStatus();

    }

    private int screenWidth;
    private int screenHeight;

    private void initData() {
        utils = new Utils();
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                //暂停和播放
                startAndPause();
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                //隐藏和显示控制面板
                if (isshowMediaController) {
                    hideMediaController();
                    handler.removeMessages(MEDIA_CONTROLLER);
                } else {
                    showMediaController();
                    handler.sendEmptyMessageDelayed(MEDIA_CONTROLLER, 4000);
                }
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                setFullScreenAndDefault();
                return super.onDoubleTap(e);
            }
        });
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        maxVoice = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVoice = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        //设置音量
        seekbarVoice.setMax(maxVoice);
        seekbarVoice.setProgress(currentVoice);
    }

    private void setFullScreenAndDefault() {
        if (isFullScreen) {
            setVideoType(DEFAULT);
        } else {
            setVideoType(FULL);
        }
    }

    private void setVideoType(int type) {
        switch (type) {
            case FULL:
                videoView.setVideoSize(screenWidth, screenHeight);
                btnVideoSwitchScreen.setBackgroundResource(R.drawable.btn_video_switch_default_screen_selector);
                isFullScreen = true;
                break;
            case DEFAULT:
                int mVideoWidth = videoWidth;
                int mVideoHeight = videoHeight;
                int width = screenWidth;
                int height = screenHeight;
                if (mVideoWidth * height < width * mVideoHeight) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                } else if (mVideoWidth * height > width * mVideoHeight) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                }
                videoView.setVideoSize(width, height);
                btnVideoSwitchScreen.setBackgroundResource(R.drawable.btn_video_switch_full_screen_selector);
                isFullScreen = false;
                break;
        }
    }

    private float startY;
    private int touchRange;
    //当前音量
    private int mVol;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //屏幕事件交由detector分析
        detector.onTouchEvent(event);
        //上下滑动屏幕有改变音量
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = event.getY();
                mVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                touchRange = Math.min(screenHeight, screenWidth);
                handler.removeMessages(MEDIA_CONTROLLER);
                break;
            case MotionEvent.ACTION_MOVE:
                float endY = event.getY();
                float distance = startY - endY;
                float delta = distance / touchRange * maxVoice;
                int voice = (int) Math.min(Math.max(mVol + delta, 0), maxVoice);
                if (delta != 0) {
                    isMute = false;
                    updateVoice(voice, isMute);
                }
                break;
            case MotionEvent.ACTION_UP:
                handler.sendEmptyMessageDelayed(MEDIA_CONTROLLER, 4000);
                break;
        }
        return true;
    }

    //监听物理按键改变音量
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                currentVoice--;
                updateVoice(currentVoice, false);
                handler.removeMessages(MEDIA_CONTROLLER);
                handler.sendEmptyMessageDelayed(MEDIA_CONTROLLER, 4000);
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                currentVoice++;
                updateVoice(currentVoice, false);
                handler.removeMessages(MEDIA_CONTROLLER);
                handler.sendEmptyMessageDelayed(MEDIA_CONTROLLER, 4000);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setListener() {
        videoView.setOnPreparedListener(this);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNextVideo();
            }
        });
        seekbarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(MEDIA_CONTROLLER);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.sendEmptyMessageDelayed(MEDIA_CONTROLLER, 4000);
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                //播放出错
                /*1.视频格式不支持--》跳转到万能播放器播放
                2.网络有问题--》1网络断了，提示用户  2 网络断断续续 重新播放
                * 3.视频文件有问题
                * */
                AlertDialog.Builder builder=new AlertDialog.Builder(VitamioVideoActivity.this);
                builder.setTitle("提示").setMessage("播放出错").setIcon(android.R.drawable.stat_notify_error).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
              return  true;
            }
        });

        seekbarVoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (progress < 0) {
                        isMute = true;
                    } else {
                        isMute = false;
                    }
                    updateVoice(progress, isMute);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(MEDIA_CONTROLLER);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.sendEmptyMessageDelayed(MEDIA_CONTROLLER, 4000);

            }
        });
        //监听网络视频播放时是否卡顿
        if (isuseSystem){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    switch (what) {
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                            //开始卡顿  网速慢，拖动卡
                            ll_buffer.setVisibility(View.VISIBLE);
                            break;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                            //卡顿结束  网速慢，拖动卡结束
                            ll_buffer.setVisibility(View.GONE);
                            break;
                    }
                    return true;
                }
            });
        }
        }
    }


    private void updateVoice(int progress, boolean isMute) {
        if (isMute) {
            am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            seekbarVoice.setProgress(0);
        } else {
            am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            seekbarVoice.setProgress(progress);
            currentVoice = progress;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_SPEED:
                    String netspeed=utils.getNetSpeed(VitamioVideoActivity.this);
                    tv_loading.setText("视频加载中..."+netspeed);
                    tv_buffer.setText("缓冲中..."+netspeed);
                    handler.removeMessages(SHOW_SPEED);
                    handler.sendEmptyMessageDelayed(SHOW_SPEED,2000);
                    break;
                case MEDIA_CONTROLLER:
                    hideMediaController();
                    break;
                case PROGRESS:
                    int current = (int) videoView.getCurrentPosition();
                    seekbarVideo.setProgress(current);
                    tvCurrenttime.setText(utils.stringForTime(current));
                    //显示系统时间
                    tvSystemtime.setText(getSystemTime());
                    //网络视频显示缓冲效果
                    if (isnetUri) {
                        int bufferPercentage = videoView.getBufferPercentage();
                        int totalBuffer = bufferPercentage * seekbarVideo.getMax();
                        int secondProgress = totalBuffer / 100;
                        seekbarVideo.setSecondaryProgress(secondProgress);
                    } else {
                        seekbarVideo.setSecondaryProgress(0);
                    }
                    //自己定义方法判断网络视频是否卡顿
                    if (!isuseSystem&&videoView.isPlaying()){
                        if (videoView.isPlaying()) {
                            int buffer = current - precurrentposition;
                            if (buffer < 500) {
                                ll_buffer.setVisibility(View.VISIBLE);
                            } else {
                                ll_buffer.setVisibility(View.GONE);
                            }
                        }else{
                            ll_buffer.setVisibility(View.GONE);
                        }
                    }
                    precurrentposition=current;
                    handler.removeMessages(PROGRESS);
                    handler.sendEmptyMessageDelayed(PROGRESS, 1000);
                    break;
            }
        }
    };

    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
        return format.format(new Date());
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        videoView.start();
        videoHeight = mp.getVideoHeight();
        videoWidth = mp.getVideoWidth();
        //更新进度
        int duration = (int) videoView.getDuration();
        seekbarVideo.setMax(duration);
        tvTotaltime.setText(utils.stringForTime(duration));
        hideMediaController();
        handler.sendEmptyMessage(PROGRESS);
        setVideoType(DEFAULT);
        ll_loading.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        if (videoView != null) {
            videoView = null;
        }
        unregisterReceiver(receiver);
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private boolean isshowMediaController = false;

    private void showMediaController() {
        mediacontroller.setVisibility(View.VISIBLE);
        isshowMediaController = true;
    }

    private void hideMediaController() {
        mediacontroller.setVisibility(View.GONE);
        isshowMediaController = false;
    }

    private boolean isFullScreen = false;

}
