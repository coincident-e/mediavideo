package com.example.wumin.myapplication.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wumin.myapplication.R;
import com.example.wumin.myapplication.activity.AudioPlayerActivity;
import com.example.wumin.myapplication.adapter.LvVideoAdapter;
import com.example.wumin.myapplication.bean.MediaItem;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AudioFragment extends Fragment implements AdapterView.OnItemClickListener {


    private Context context;
    private LvVideoAdapter lvAdapter;
    private ListView listView;
    private TextView tv;
    private ProgressBar pb;
    private ArrayList<MediaItem> list;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==10){
                if (list!=null&&list.size()>0){
                    tv.setVisibility(View.GONE);
                    //TODO 设置适配器
                    listView.setAdapter(new LvVideoAdapter(context,list,true));
                }else{
                    tv.setVisibility(View.VISIBLE);
                }
                pb.setVisibility(View.GONE);
            }
        }
    };
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_audio, container, false);
        listView= (ListView) view.findViewById(R.id.listview_video);
        tv= (TextView) view.findViewById(R.id.tv_video);
        pb= (ProgressBar) view.findViewById(R.id.pb_video);
        list=new ArrayList<>();
        getVideoData();
        listView.setOnItemClickListener(this);
        return view;
    }

    private void getVideoData() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                isGrantExternalRW((Activity) context);
                ContentResolver contentResolver = context.getContentResolver();

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
                        String data=cursor.getString(3);
                        media.setData(data);
                        String artist = cursor.getString(4);
                        media.setArtist(artist);
                        list.add(media);
                    }
                }
                cursor.close();
//                System.out.println("-------list size"+list.size());
                handler.sendEmptyMessage(10);
            }
//            }
        }.start();
    }

    /**
     * 解决安卓6.0以上版本不能读取外部存储权限的问题
     * @param activity
     * @return
     */
    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.WRITE_SETTINGS
            }, 1);

            return false;
        }

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MediaItem item=list.get(position);
        //传递数据列表
        Intent intent=new Intent(context, AudioPlayerActivity.class);
//        Bundle bundle=new Bundle();
//        bundle.putSerializable("medialist",list);
//        intent.putExtras(bundle);
        intent.putExtra("position",position);
        context.startActivity(intent);
    }

    public AudioFragment() {
        // Required empty public constructor
    }




}
