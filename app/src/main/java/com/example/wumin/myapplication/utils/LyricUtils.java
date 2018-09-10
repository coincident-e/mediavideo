package com.example.wumin.myapplication.utils;

import com.example.wumin.myapplication.view.Lyric;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by wumin on 2018/8/10.
 */
public class LyricUtils {
    //得到解析好的歌词集合
    public  ArrayList<Lyric> getLyrics() {
        return lyrics;
    }

    public boolean isExists() {
        return isExists;
    }

    private  boolean isExists=false;
    private ArrayList<Lyric> lyrics;

    public void readLyricFile(File file) {
        if (file == null || !file.exists()) {
            //歌词不存在
            isExists=false;
            lyrics = null;
        } else {
            lyrics = new ArrayList<>();
            isExists=true;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),getCharset(file)));
                String line = "";
                while ((line = reader.readLine()) != "") {
                    //解析每一行内容
                    line = parseLineLyric(line);
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
            //sort
            Collections.sort(lyrics, new Comparator<Lyric>() {
                @Override
                public int compare(Lyric o1, Lyric o2) {
                    return o1.getTime()==o2.getTime()?0:(o1.getTime()>o2.getTime()?-1:1);
                }
            });
            //设置高亮时间
            for (int i = 0; i < lyrics.size(); i++) {
                Lyric onelyric = lyrics.get(i);
                if (i+1<lyrics.size()){
                    Lyric twolyric = lyrics.get(i + 1);
                    onelyric.setHighLightTime(twolyric.getTime()-onelyric.getTime());
                }
            }
        }
    }

    private String parseLineLyric(String content) {

        //第一个时间
        int pos1 = content.indexOf("[");
        int pos2 = content.indexOf("]");
        if (pos1 == 0 && pos2 != -1) {
            //一行歌词有对应几个时间戳
            long[] times = new long[getCount(content)];
            times[0]=strTime2LongTime(content.substring(pos1+1,pos2));//[][][]xxxxxxxx
            String line=content;
            int i=1;
            while (pos1 == 0 && pos2 != -1){
                line=line.substring(pos2+1);//[][]xxxxxxx//last time xxxxxxxx
                pos1 = content.indexOf("[");
                pos2 = content.indexOf("]");
                if (pos2!=-1){
                    times[i]=strTime2LongTime(line.substring(pos1+1,pos2));
                    if (times[i]==-1){
                        return "";
                    }
                    i++;
                }
            }
            //把时间戳数组和歌词关联
            for (int j=0;j<times.length;j++){
              if (times[j]!=0){//表示有时间戳
                  Lyric lyric=new Lyric();
                  lyric.setTime(times[i]);
                  lyric.setContent(line);
                  lyrics.add(lyric);
              }
            }
            return line;
        }
        return "";
    }
//字符串时间转化成long类型的时间
    private long strTime2LongTime(String time) {
        long result=0;
       try {
           String[] one=time.split(":");
           String[] two=one[1].split(".");
           long min=Long.parseLong(one[0]);
           long sec=Long.parseLong(two[0]);
           long mil=Long.parseLong(two[1]);
           result=min*60*1000+sec*1000+mil*10;
       }catch (Exception e){
           result=-1;
       }
        return result;
    }

    /**
     * 判断文件编码
     * @param file 文件
     * @return 编码：GBK,UTF-8,UTF-16LE
     */
    public String getCharset(File file) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(file));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1)
                return charset;
            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8";
                checked = true;
            }
            bis.reset();
            if (!checked) {
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF)
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF)
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return charset;
    }

    private int getCount(String content) {
        int result = 0;
        String[] left = content.split("\\[");
        String[] right = content.split("\\]");
        if (left.length == 0 || right.length == 0) {
            result = 1;
        } else {
            result = Math.max(left.length, right.length);
        }
        return result;
    }


}
