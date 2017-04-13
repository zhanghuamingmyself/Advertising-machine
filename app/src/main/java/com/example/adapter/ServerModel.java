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
    public String near;
    public String open;
    public String play;
    public String works;



    @Override
    public String toString() {
        return "near "+near+"/"+
                "open "+open+"/"+
                "play "+play+"/"+
                "works "+works;
    }
}
