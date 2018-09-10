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
import android.widget.Toast;

import com.example.wumin.myapplication.R;
import com.example.wumin.myapplication.activity.SystemVideoActivity;
import com.example.wumin.myapplication.adapter.LvVideoAdapter;
import com.example.wumin.myapplication.bean.MediaItem;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment implements AdapterView.OnItemClickListener {

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
                    listView.setAdapter(new LvVideoAdapter(context,list,false));
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

    public VideoFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        while (!isGrantExternalRW((Activity) context)){
//            isGrantExternalRW((Activity) context);
//        };
        View view=inflater.inflate(R.layout.fragment_video, container, false);
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

                    String[] projection = {MediaStore.Video.Media.DISPLAY_NAME,
                            MediaStore.Video.Media.DURATION,
                            MediaStore.Video.Media.SIZE,
                            MediaStore.Video.Media.DATA,
                            MediaStore.Video.Media.ARTIST
                    };
                    Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
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
                    System.out.println("-------list size"+list.size());
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
        Toast.makeText(context, item.toString(), Toast.LENGTH_SHORT).show();
//        Intent intent=new Intent();
//        intent.setDataAndType(Uri.parse(list.get(position).getData()),"video/*");
//        context.startActivity(intent);
//        Intent intent=new Intent(context, SystemVideoActivity.class);
//        intent.setDataAndType(Uri.parse(item.getData()),"video/*");
//        context.startActivity(intent);

        //传递数据列表
        Intent intent=new Intent(context, SystemVideoActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("medialist",list);
        intent.putExtras(bundle);
        intent.putExtra("position",position);
        context.startActivity(intent);
    }
}
