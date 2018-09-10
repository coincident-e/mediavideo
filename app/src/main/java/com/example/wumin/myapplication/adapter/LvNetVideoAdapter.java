package com.example.wumin.myapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wumin.myapplication.R;
import com.example.wumin.myapplication.bean.MediaItem;

import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by wumin on 2018/8/6.
 */
public class LvNetVideoAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MediaItem> data;

    public LvNetVideoAdapter(Context context, ArrayList<MediaItem> data) {
        this.context = context;
        this.data = data;
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
        convertView = View.inflate(context, R.layout.listview_net_video_layout, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);

        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        MediaItem mediaItem = data.get(position);
        x.image().bind(holder.iv_image,mediaItem.getCover());
        holder.tv_title.setText(mediaItem.getName());
        holder.tv_desc.setText(mediaItem.getDesc());
        return convertView;
    }

    static class ViewHolder {
        ImageView iv_image;
        TextView tv_title, tv_desc;

        ViewHolder(View view) {
            iv_image = (ImageView) view.findViewById(R.id.cover_netvideo_item);
            tv_title = (TextView) view.findViewById(R.id.tv_title_netvideo_item);
            tv_desc = (TextView) view.findViewById(R.id.tv_desc_netvideo_item);
        }
    }
}
