package com.example.wumin.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.wumin.myapplication.utils.DensityUtil;

import java.util.ArrayList;

/**
 * Created by wumin on 2018/8/10.
 */
public class ShowLyricView extends TextView {
    private ArrayList<Lyric> lyrics;
    private int width;
    private int height;
    private Paint paint;
    private Paint whitepaint;
    private int lineheight;
    private int index = 0;
    private int currentposition;
    private float time;
    private float highLightTime;

    public ShowLyricView(Context context) {
        this(context, null);


    }

    public ShowLyricView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowLyricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
public void setLyrics(ArrayList<Lyric> lyrics){
    this.lyrics=lyrics;
}
    private void initView(Context context) {
        lineheight= DensityUtil.dip2px(context,20);
        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.GREEN);
        paint.setAntiAlias(true);
        paint.setTextSize(DensityUtil.dip2px(context,20));
        whitepaint = new Paint();
        whitepaint.setTextAlign(Paint.Align.CENTER);
        whitepaint.setColor(Color.WHITE);
        whitepaint.setAntiAlias(true);
        whitepaint.setTextSize(DensityUtil.dip2px(context,20));
        lyrics = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Lyric lyric = new Lyric();
            lyric.setContent(i + "qqqqqqqqqqqqqq" + i);
            lyric.setTime(1000 * i);
            lyric.setHighLightTime(1500 + i);
            lyrics.add(lyric);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (lyrics != null && lyrics.size() > 0) {
            //歌词向上移动的效果
            float flush=0;
            if (highLightTime==0){
                flush=0;
            }else{
                float delta=(currentposition-time)/highLightTime*lineheight;
                flush= lineheight+delta;
            canvas.translate(0,-flush);
            }





            //绘制歌词  前面部分  高亮部分  后面部分
            //高亮
            canvas.drawText(lyrics.get(index).getContent(), width / 2, height / 2, paint);
            //前面歌词
            int temp = height / 2;
            for (int i = index - 1; i >= 0; i--) {
                String precontent = lyrics.get(i).getContent();
                temp = temp - lineheight;
                if (temp < 0) {
                    break;
                }
                canvas.drawText(precontent, width / 2, temp, whitepaint);
            }
            //后面部分
            temp = height / 2;
            for (int i = index + 1; i <lyrics.size(); i++) {
                String nextcontent = lyrics.get(i).getContent();
                temp = temp + lineheight;
                if (temp >height) {
                    break;
                }
                canvas.drawText(nextcontent, width / 2, temp, whitepaint);
            }
        }


        else {
            canvas.drawText("没有歌词", width / 2, height / 2, paint);
        }
    }

    public void setShowNextLyric(int currentPosition) {
        this.currentposition = currentPosition;
        if (lyrics == null || lyrics.size() <= 0)
            return;
        for (int i = 1; i < lyrics.size(); i++) {
            if (currentposition < lyrics.get(i).getTime()) {
                int tempindex = i - 1;
                if (currentposition >= lyrics.get(tempindex).getTime()) {
                    index = tempindex;
                    time = lyrics.get(index).getTime();
                    highLightTime = lyrics.get(index).getHighLightTime();
                }
            }
        }
        //重新绘制
        invalidate();

    }
}
