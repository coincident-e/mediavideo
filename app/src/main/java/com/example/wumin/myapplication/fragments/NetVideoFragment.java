package com.example.wumin.myapplication.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wumin.myapplication.R;
import com.example.wumin.myapplication.activity.SystemVideoActivity;
import com.example.wumin.myapplication.adapter.LvNetVideoAdapter;
import com.example.wumin.myapplication.bean.MediaItem;
import com.example.wumin.myapplication.utils.CacheUtils;
import com.example.wumin.myapplication.utils.Constants;
import com.example.wumin.myapplication.view.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class NetVideoFragment extends Fragment {
    private XListView listview_netvideo;
    private TextView tv_nonet;
    private ProgressBar pb;
    private ArrayList<MediaItem> mediaItems = new ArrayList<>();
    private LvNetVideoAdapter adapter;
    private Context context;
    private boolean moreDataFromNet;

    public NetVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_net_video, container, false);
        listview_netvideo = (XListView) view.findViewById(R.id.listview_netvideo);
        tv_nonet = (TextView) view.findViewById(R.id.tv_nonetvideo);
        pb = (ProgressBar) view.findViewById(R.id.pb_netvideo);
        listview_netvideo.setPullLoadEnable(true);
        listview_netvideo.setXListViewListener(new MyIXListViewListener());
        adapter = new LvNetVideoAdapter(context, mediaItems);
        listview_netvideo.setAdapter(adapter);
        initdata();
        setListener();
        return view;
    }

    public void getMoreDataFromNet() {
        isLoadMore = true;
        initdata();
    }

    class MyIXListViewListener implements XListView.IXListViewListener {

        @Override
        public void onRefresh() {
            initdata();
            onload();
        }

        @Override
        public void onLoadMore() {
            getMoreDataFromNet();
        }
    }

    private void onload() {
        listview_netvideo.stopRefresh();
        listview_netvideo.stopLoadMore();
        listview_netvideo.setRefreshTime("更新时间：" + getSystemTime());

    }

    private String getSystemTime() {
        SimpleDateFormat format =  new SimpleDateFormat("hh:mm:ss");
        return format.format(new Date());
    }

    private void setListener() {
        listview_netvideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, SystemVideoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("medialist", mediaItems);
                intent.putExtras(bundle);
                intent.putExtra("position", position-1);
                context.startActivity(intent);
            }
        });
    }

    private void setData() {
        if (mediaItems != null && mediaItems.size() > 0) {
            tv_nonet.setVisibility(View.GONE);
            //设置适配器
            adapter=new LvNetVideoAdapter(context,mediaItems);
            listview_netvideo.setAdapter(adapter);

        } else {
            tv_nonet.setVisibility(View.VISIBLE);
        }
        pb.setVisibility(View.GONE);
    }

    private boolean isLoadMore = false;

    private void initdata() {
        final RequestParams params = new RequestParams(Constants.NET_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CacheUtils.cacheData(context,Constants.NET_URL,result);
                if (!isLoadMore) {
                    mediaItems = parseJson(result);
                    System.out.println("------"+mediaItems.size());
                    setData();
                } else {
                    mediaItems.addAll(parseJson(result));
                    adapter.notifyDataSetChanged();
                    isLoadMore=false;
                    onload();
                }

            }


            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
isLoadMore=false;
               String data= CacheUtils.getData(context,Constants.NET_URL);
                if (!TextUtils.isEmpty(data)){
                    mediaItems=parseJson(data);
                    setData();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
isLoadMore=false;
            }

            @Override
            public void onFinished() {
isLoadMore=false;
            }
        });
    }


    private ArrayList<MediaItem> parseJson(String result) {
        ArrayList<MediaItem> mediaItems = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.optJSONArray("trailers");
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.optJSONObject(i);
                    MediaItem item = new MediaItem();
                    String name = object.getString("movieName");
                    item.setName(name);
                    String desc = object.getString("videoTitle");
                    item.setDesc(desc);
                    String data = object.getString("url");
                    item.setData(data);
                    String cover = object.getString("coverImg");
                    item.setCover(cover);
                    mediaItems.add(item);
                }
//                System.out.println("------------" + mediaItems);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mediaItems;
    }

}
