package com.example.wumin.myapplication.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wumin.myapplication.R;
import com.example.wumin.myapplication.adapter.LvNetVideoAdapter;
import com.example.wumin.myapplication.bean.MediaItem;
import com.example.wumin.myapplication.utils.Constants;
import com.example.wumin.myapplication.utils.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class SearchActivity extends AppCompatActivity {
    private EditText etInput;
    private ImageView ivSpeechinput;
    private TextView tvSearch;
    private ListView lvSearchActivity;
    private ProgressBar pbSearchActivity;
    private TextView tvNoresult;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private LvNetVideoAdapter adapter;
    private ArrayList<MediaItem> mediaItems=new ArrayList<>();
//    private List<SearchBean.TrailersBean> trailers=new ArrayList<>();


    private void findViews() {
        setContentView(R.layout.activity_search);
        etInput = (EditText) findViewById(R.id.et_input);
        ivSpeechinput = (ImageView) findViewById(R.id.iv_speechinput);
        tvSearch = (TextView) findViewById(R.id.tv_search);
        lvSearchActivity = (ListView) findViewById(R.id.lv_search_activity);
        pbSearchActivity = (ProgressBar) findViewById(R.id.pb_search_activity);
        tvNoresult = (TextView) findViewById(R.id.tv_noresult);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        findViews();
        setListener();
    }

    private void setListener() {
        ivSpeechinput.setOnClickListener(new MyOnClickListener());
        tvSearch.setOnClickListener(new MyOnClickListener());
    }

    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_speechinput:
                    showDialog();
                    Toast.makeText(SearchActivity.this, "语音输入", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_search:
                    search();
//                    Toast.makeText(SearchActivity.this, "s", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    //搜索
    private void search() {
        String text = etInput.getText().toString().trim();
        if (!TextUtils.isEmpty(text)) {
            try {
                if (mediaItems!=null&&mediaItems.size()>0){
                    mediaItems.clear();
                }
                text = URLEncoder.encode(text, "UTF-8");
                String path = Constants.SEARCH_URL + text;
                getDataFromNet(path);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }

    private void getDataFromNet(String path) {
        pbSearchActivity.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams(Constants.NET_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {
pbSearchActivity.setVisibility(View.VISIBLE);
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                pbSearchActivity.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(CancelledException cex) {
                pbSearchActivity.setVisibility(View.GONE);
            }
            @Override
            public void onFinished() {
                pbSearchActivity.setVisibility(View.GONE);
            }
        });
    }

    private void setAdapter() {
        if (mediaItems!=null&&mediaItems.size()>0){
           tvNoresult.setVisibility(View.GONE);
            adapter=new LvNetVideoAdapter(this,mediaItems);
            lvSearchActivity.setAdapter(adapter);
        }else{
            tvNoresult.setVisibility(View.VISIBLE);
        }
        pbSearchActivity.setVisibility(View.GONE);
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mediaItems;
    }
    private void processData(String result) {
        //设置适配器
        mediaItems=parseJson(result);
        if (mediaItems!=null&&mediaItems.size()>0) {
            adapter = new LvNetVideoAdapter(this,mediaItems);
            lvSearchActivity.setAdapter(adapter);
            tvNoresult.setVisibility(View.GONE);
        }else{
            tvNoresult.setVisibility(View.VISIBLE);
        }
        pbSearchActivity.setVisibility(View.GONE);
    }


    private void showDialog() {
        RecognizerDialog dialog = new RecognizerDialog(this, new MyInitListener());
        dialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        dialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        dialog.setListener(new MyRecognizerDialogListener());
        dialog.show();
    }

    class MyRecognizerDialogListener implements RecognizerDialogListener {

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            printResult(recognizerResult);
        }

        @Override
        public void onError(SpeechError speechError) {
            Toast.makeText(SearchActivity.this, "识别失败", Toast.LENGTH_SHORT).show();
        }
    }

    class MyInitListener implements InitListener {

        @Override
        public void onInit(int i) {
            if (i != ErrorCode.SUCCESS) {
                Toast.makeText(SearchActivity.this, "初始化失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        etInput.setText(resultBuffer.toString());
        etInput.setSelection(etInput.length());
    }


}
