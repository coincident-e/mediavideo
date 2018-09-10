package com.example.wumin.myapplication.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wumin.myapplication.R;
import com.example.wumin.myapplication.adapter.LvNetAudioAdapter;
import com.example.wumin.myapplication.bean.NetAudioBean;
import com.example.wumin.myapplication.utils.CacheUtils;
import com.example.wumin.myapplication.utils.Constants;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


public class NetAutioFragment extends Fragment {
    private ListView listView;
    private ProgressBar pb_nonetaudio;
    private TextView tv_nonetaudio;
    private Context context;
    private List<NetAudioBean.ListBean> datas=new ArrayList<>();
    private LvNetAudioAdapter adapter;

    public NetAutioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_net_autio, container, false);
        initView(view);
        getData();
        return view;
    }

    private void getData() {
        String result= CacheUtils.getData(context, Constants.ALL_RES_URL);
        if (!TextUtils.isEmpty(result)){
            //没有网络直接显示缓存的数据
           datas= parseJson(result);
        }
        getDataFromNet();


    }

    private void setAdapter() {
        if (datas!=null&&datas.size()>0){
            tv_nonetaudio.setVisibility(View.GONE);
            //设置适配器  tvnoaudio隐藏
            adapter = new LvNetAudioAdapter(context,datas);
            listView.setAdapter(adapter);
        }else{
            tv_nonetaudio.setVisibility(View.VISIBLE);
        }
        pb_nonetaudio.setVisibility(View.GONE);
    }

    private void getDataFromNet() {
        pb_nonetaudio.setVisibility(View.VISIBLE);
        x.http().get(new RequestParams(Constants.ALL_RES_URL), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
              datas=  parseJson(result);
                setAdapter();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
pb_nonetaudio.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private List<NetAudioBean.ListBean>  parseJson(String result) {
        Gson gson=new Gson();
        NetAudioBean netAudioBean = gson.fromJson(result, NetAudioBean.class);
        List<NetAudioBean.ListBean> list = netAudioBean.getList();
        System.out.println("---net audio parse json 后的数据"+datas.size());
        return list;
    }

    private void initView(View view) {
        listView= (ListView) view.findViewById(R.id.listview_netaudio);
        pb_nonetaudio= (ProgressBar) view.findViewById(R.id.pb_netaudio);
        tv_nonetaudio= (TextView) view.findViewById(R.id.tv_nonetaudio);
    }


}
