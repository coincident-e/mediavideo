package com.example.wumin.myapplication.activity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wumin.myapplication.IMyAudioPlayerService;
import com.example.wumin.myapplication.R;
import com.example.wumin.myapplication.bean.MediaItem;
import com.example.wumin.myapplication.service.MyAudioPlayerService;
import com.example.wumin.myapplication.utils.Utils;
import com.example.wumin.myapplication.view.BaseVisualizerView;
import com.example.wumin.myapplication.view.ShowLyricView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AudioPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    private BaseVisualizerView baseVisualizerView;
    private ImageView imageAudio;
    private TextView tvSingerAudio;
    private TextView tvSongnameAudio;
    private TextView tvTimeAudio;
    private SeekBar seekbarAudio;
    private Button btnAudioPlaymode;
    private Button btnAudioPre;
    private Button btnAudioStartPause;
    private Button btnAudioNext;
    private Button btnAudioLyric;
    private int position;
    private IMyAudioPlayerService service;
    private Utils utils;
    //标记是否是从通知栏进入的activity
    private  boolean notification=false;
    //更新歌词控件
    private  static final int SHOW_LYRIC=2;
    private  static final int   UPDATE=1;
    private ShowLyricView showLyricView;
    //handler 更新时间和进度条


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case  SHOW_LYRIC:
                    try {
                        int currentPosition1 = service.getCurrentPosition();
                        showLyricView.setShowNextLyric(currentPosition1);
                        handler.removeMessages(SHOW_LYRIC);
                        handler.sendEmptyMessage(SHOW_LYRIC);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case UPDATE:
                    try {
                        int currentPosition = service.getCurrentPosition();
                        tvTimeAudio.setText(utils.stringForTime(currentPosition) +"/"+utils.stringForTime(service.getDuration()));
                        seekbarAudio.setProgress(currentPosition);
                        handler.removeMessages(UPDATE);
                        handler.sendEmptyMessageDelayed(UPDATE,1000);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
private ServiceConnection conn=new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder iBinder) {
        service=IMyAudioPlayerService.Stub.asInterface(iBinder);
        if (service != null) {
            try {
                if (!notification) {
                    service.openAutio(position);
                }else{
                    showData(null);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
};
    private BroadcastReceiver receiver;
    private int playMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBroad();
       findViews();
        getData();
        bindAudioService();
    }

    private void registerBroad() {
        utils=new Utils();
//        receiver=new MyReceiver();
//        IntentFilter fileter=new IntentFilter();
//        fileter.addAction(MyAudioPlayerService.AUDIOMEDIA);
//        registerReceiver(receiver,fileter);
        EventBus.getDefault().register(this);
    }
    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            showData(null);
        }
    }
@Subscribe(threadMode = ThreadMode.MAIN,sticky = false,priority = 0)
    public void showData(MediaItem item) {
    //播放器准备好之后发消息更新界面
    //更新歌词控件
    showLyric();
    showViewData();
    checkPlayMode();
    setupVisualizerFxAndUi();
    }

    private void showLyric() {
//        LyricUtils utils=new LyricUtils();
//        try {
//            String path=service.getAudioPath();
//            path=path.substring(0,path.lastIndexOf("."));
//            File file=new File(path+".lrc");
//            if (!file.exists()){
//                file=new File(path+".txt");
//            }
//            utils.readLyricFile(file);
//          showLyricView.setLyrics( utils.getLyrics());
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        }
//        if (utils.isExists()){
            handler.sendEmptyMessage(SHOW_LYRIC);
//        }
    }

    private void showViewData() {
        try {
            tvSingerAudio.setText(service.getArtist());
            tvSongnameAudio.setText(service.getName());
            seekbarAudio.setMax(service.getDuration());
            handler.sendEmptyMessage(UPDATE);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void bindAudioService() {
        Intent intent=new Intent(this, MyAudioPlayerService.class);
        intent.setAction("android.service.audioplayer");
        bindService(intent,conn, Service.BIND_AUTO_CREATE);
        startService(intent);
    }

    public void getData() {
        notification=getIntent().getBooleanExtra("notification",false);
        if (!notification) {
            position = getIntent().getIntExtra("position", 0);
        }

    }


    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-08-09 21:16:39 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.activity_audio_player);
        baseVisualizerView= (BaseVisualizerView) findViewById(R.id.basevisualizerview);
        showLyricView= (ShowLyricView) findViewById(R.id.showlyricview);
        imageAudio = (ImageView)findViewById( R.id.image_audio );
        //开启动画
        imageAudio.setBackgroundResource(R.drawable.animation_list);
        AnimationDrawable background = (AnimationDrawable) imageAudio.getBackground();
        background.start();
        tvSingerAudio = (TextView)findViewById( R.id.tv_singer_audio );
        tvSongnameAudio = (TextView)findViewById( R.id.tv_songname_audio );
        tvTimeAudio = (TextView)findViewById( R.id.tv_time_audio );
        seekbarAudio = (SeekBar)findViewById( R.id.seekbar_audio );
        btnAudioPlaymode = (Button)findViewById( R.id.btn_audio_playmode );
        btnAudioPre = (Button)findViewById( R.id.btn_audio_pre );
        btnAudioStartPause = (Button)findViewById( R.id.btn_audio_start_pause );
        btnAudioNext = (Button)findViewById( R.id.btn_audio_next );
        btnAudioLyric = (Button)findViewById( R.id.btn_audio_lyric );

        btnAudioPlaymode.setOnClickListener( this );
        btnAudioPre.setOnClickListener( this );
        btnAudioStartPause.setOnClickListener( this );
        btnAudioNext.setOnClickListener( this );
        btnAudioLyric.setOnClickListener( this );
        seekbarAudio.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
    }
    class MyOnSeekBarChangeListener implements  SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser){
                try {
                    service.seekTo(progress);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2018-08-09 21:16:39 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == btnAudioPlaymode ) {
            // Handle clicks for btnAudioPlaymode
            //切换播放模式
           changePlayMode();
        } else if ( v == btnAudioPre ) {
            try {
                service.pre();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            // Handle clicks for btnAudioPre
        } else if ( v == btnAudioStartPause ) {
            // Handle clicks for btnAudioStartPause
            if (service != null) {
                try {
                    if (service.isPlaying()){
                        //改为暂停  按钮状态改变
                        service.pause();
                        btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_play_selector);
                    }else{
                        service.start();
                        btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else if ( v == btnAudioNext ) {
            // Handle clicks for btnAudioNext
            try {
                service.next();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if ( v == btnAudioLyric ) {
            // Handle clicks for btnAudioLyric
        }
    }

    private void changePlayMode() {
        try {
            playMode=service.getPlayMode();
            if (playMode==MyAudioPlayerService.REPEAT_NORMAL){
                service.setPlayMode(MyAudioPlayerService.REPEAT_SINGLE);
            }else if (playMode==MyAudioPlayerService.REPEAT_SINGLE){
                service.setPlayMode(MyAudioPlayerService.REPEAT_ALL);
            }else if (playMode==MyAudioPlayerService.REPEAT_ALL){
                service.setPlayMode(MyAudioPlayerService.REPEAT_NORMAL);
            }else{
                service.setPlayMode(MyAudioPlayerService.REPEAT_NORMAL);
            }
            showPlayMode();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void showPlayMode() {
        try {
            playMode=service.getPlayMode();
            if (playMode==MyAudioPlayerService.REPEAT_NORMAL){
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_order_selector);
                Toast.makeText(AudioPlayerActivity.this, "顺序播放", Toast.LENGTH_SHORT).show();

            }else if (playMode==MyAudioPlayerService.REPEAT_SINGLE){
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_single_selector);
                Toast.makeText(AudioPlayerActivity.this, "单曲循环", Toast.LENGTH_SHORT).show();
            }else if (playMode==MyAudioPlayerService.REPEAT_ALL){
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_playall_selector);
                Toast.makeText(AudioPlayerActivity.this, "循环列表", Toast.LENGTH_SHORT).show();
            }else{
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_order_selector);
                Toast.makeText(AudioPlayerActivity.this, "顺序播放", Toast.LENGTH_SHORT).show();
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    private  Visualizer mVisualizer;
    /**
     * 生成一个VisualizerView对象，使音频频谱的波段能够反映到 VisualizerView上
     */
    private void setupVisualizerFxAndUi()
    {

        try {
            int audioSessionid = service.getAudioSessionId();
            System.out.println("audioSessionid=="+audioSessionid);
            mVisualizer = new Visualizer(audioSessionid);
            // 参数内必须是2的位数
            mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
            // 设置允许波形表示，并且捕获它
            baseVisualizerView.setVisualizer(mVisualizer);
            mVisualizer.setEnabled(true);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onPause() {
        if (mVisualizer!=null){
            mVisualizer.release();
        }
        super.onPause();
    }

    private  void checkPlayMode(){
        try {
            playMode=service.getPlayMode();
            if (playMode==MyAudioPlayerService.REPEAT_NORMAL){
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_order_selector);

            }else if (playMode==MyAudioPlayerService.REPEAT_SINGLE){
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_single_selector);
            }else if (playMode==MyAudioPlayerService.REPEAT_ALL){
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_playall_selector);
            }else{
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_order_selector);
            }
//校验播放按钮
            if (service.isPlaying()) {
                btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_play_selector);
            }else{
                btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(conn);
        handler.removeCallbacksAndMessages(null);
//        if (receiver!=null){
//            unregisterReceiver(receiver);
//        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
