package com.example.wumin.myapplication.view;

/**
 * Created by wumin on 2018/8/10.
 */
public class Lyric {
    private  String content;
    private  long time;
    private  long highLightTime;

    @Override
    public String toString() {
        return "Lyric{" +
                "content='" + content + '\'' +
                ", time=" + time +
                ", highLightTime=" + highLightTime +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getHighLightTime() {
        return highLightTime;
    }

    public void setHighLightTime(long highLightTime) {
        this.highLightTime = highLightTime;
    }
}
