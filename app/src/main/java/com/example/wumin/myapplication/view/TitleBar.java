package com.example.wumin.myapplication.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wumin.myapplication.R;
import com.example.wumin.myapplication.activity.SearchActivity;

/**
 * Created by wumin on 2018/8/3.
 */
public class TitleBar extends LinearLayout implements View.OnClickListener {
private  Context context;
private View tv_search;
    private  View rl_game;
    private  View iv_record;
    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TitleBar(Context context) {
        this(context,null);
    }

    @Override
    protected void onFinishInflate() {

        super.onFinishInflate();
        tv_search=getChildAt(1);
        rl_game=getChildAt(2);
        iv_record=getChildAt(3);
        tv_search.setOnClickListener(this);
        rl_game.setOnClickListener(this);
        iv_record.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_search_titlebar:
                Intent intent=new Intent(context, SearchActivity.class);
                context.startActivity(intent);
                break;
            case R.id.rl_game:
                Toast.makeText(context, "game", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_record:
                Toast.makeText(context, "record", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
