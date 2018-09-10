package com.example.wumin.myapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wumin.myapplication.R;
import com.example.wumin.myapplication.bean.SearchBean;

import org.xutils.x;

import java.util.List;

/**
 * Created by wumin on 2018/8/6.
 */
public class SearchAdapter extends BaseAdapter {
    private Context context;
    private List<SearchBean.TrailersBean> data;

    public SearchAdapter(Context context,  List<SearchBean.TrailersBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size() != 0 ? data.size() : 0;
    }

    @Override
    public SearchBean.TrailersBean getItem(int position) {
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
        SearchBean.TrailersBean trailersBean = data.get(position);
        x.image().bind(holder.iv_image,trailersBean.getCoverImg());
        holder.tv_title.setText(trailersBean.getMovieName());
        holder.tv_desc.setText(trailersBean.getSummary());
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
