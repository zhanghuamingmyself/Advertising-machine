package com.example.adapter;

import java.io.Serializable;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）
 * 版    本：1.0
 * 创建日期：2016/4/7
 * 描    述：我的Github地址  https://github.com/jeasonlzy0216
 * 修订历史：
 * ================================================
 */
public class ServerModel implements Serializable {
    private static final long serialVersionUID = -823022761336296999L;
    public String music;
    public String mynewapp;
    public String myskin;
    public String near;
    public String open;
    public String picture;
    public String ppt;
    public String setting;
    public String video;
    public String word;
    public String works;


    @Override//注意有空格的
    public String toString() {
        return "near " + near + "/" +
                "open " + open + "/" +
                "video " + video + "/" +
                "works " + works + "/" +
                "music " + music + "/" +
                "mynewapp " + mynewapp + "/" +
                "myskin " + myskin + "/" +
                "picture " + picture + "/" +
                "ppt " + ppt + "/" +
                "word " + word + "/" +
                "setting " + setting;
    }
}
