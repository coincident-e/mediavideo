package com.example.wumin.myapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wumin.myapplication.R;
import com.example.wumin.myapplication.bean.MediaItem;
import com.example.wumin.myapplication.utils.Utils;

import java.util.ArrayList;

/**
 * Created by wumin on 2018/8/6.
 */
public class LvVideoAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MediaItem> data;
    private  boolean isAudio;
private  Utils utils;
    public LvVideoAdapter(Context context, ArrayList<MediaItem> data,boolean isAudio) {
        this.context = context;
        this.data = data;
        utils=new Utils();
        this.isAudio=isAudio;
    }

    @Override
    public int getCount() {
        return data.size() != 0 ? data.size() : 0;
    }

    @Override
    public MediaItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
        convertView = View.inflate(context, R.layout.listview_video_layout, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);

        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        MediaItem mediaItem = data.get(position);
        if (isAudio) {
            holder.iv_image.setImageResource(R.drawable.music_default_bg);
        }
        holder.tv_name.setText(mediaItem.getName());
        holder.tv_size.setText(android.text.format.Formatter.formatFileSize(context,mediaItem.getSize()));
        holder.tv_duration.setText(utils.stringForTime((int) mediaItem.getDuration()));
        return convertView;
    }

    static class ViewHolder {
        ImageView iv_image;
        TextView tv_name, tv_duration, tv_size;

        ViewHolder(View view) {
            iv_image = (ImageView) view.findViewById(R.id.iv_image_item);
            tv_name = (TextView) view.findViewById(R.id.tv_name_item);
            tv_size = (TextView) view.findViewById(R.id.tv_size_item);
            tv_duration = (TextView) view.findViewById(R.id.tv_duration_item);
        }
    }
}
