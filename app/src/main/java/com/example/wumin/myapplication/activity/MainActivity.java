package com.example.wumin.myapplication.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.wumin.myapplication.R;
import com.example.wumin.myapplication.fragments.AudioFragment;
import com.example.wumin.myapplication.fragments.NetAutioFragment;
import com.example.wumin.myapplication.fragments.NetVideoFragment;
import com.example.wumin.myapplication.fragments.VideoFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    private RadioGroup rg;
    private ArrayList<Fragment> data;
    private  int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout= (FrameLayout) findViewById(R.id.framelayout_main);
        rg= (RadioGroup) findViewById(R.id.rg_main);
        initData();
        rg.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        rg.check(R.id.rb_video);

    }

    private void initData() {
        FragmentManager mananger=getSupportFragmentManager();
        FragmentTransaction transaction = mananger.beginTransaction();
        data=new ArrayList<>();
    VideoFragment    videoFragment = new VideoFragment();
        data.add(videoFragment);
        AudioFragment audioFragment=new AudioFragment();
        data.add(audioFragment);
        NetVideoFragment netVideoFragment=new NetVideoFragment();
        data.add(netVideoFragment);
        NetAutioFragment netAutioFragment=new NetAutioFragment();
        data.add(netAutioFragment);
        transaction.add(R.id.framelayout_main,videoFragment);
        transaction.add(R.id.framelayout_main,audioFragment);
        transaction.add(R.id.framelayout_main,netVideoFragment);
        transaction.add(R.id.framelayout_main,netAutioFragment);
        transaction.commit();

    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                default:
                    position=0;
                    break;
                case R.id.rb_audio:
                    position=1;
                    break;
                case R.id.rb_netvideo:
                    position=2;
                    break;
                case R.id.rb_netaudio:
                    position=3;
                    break;
            }
           showFragment(position);
        }
    }

    private void showFragment(int position) {
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        for (int i = 0; i < data.size(); i++) {
            transaction.hide(data.get(i));
        }
        transaction.show(data.get(position));
        transaction.commit();
    }

    private  boolean isexit=false;
    private Handler handler=new Handler();
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (position!=0){
                position=0;
                rg.check(R.id.rb_video);
                return true;
            }else if(!isexit){
                isexit=true;
                Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isexit=false;
                    }
                },2000);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
