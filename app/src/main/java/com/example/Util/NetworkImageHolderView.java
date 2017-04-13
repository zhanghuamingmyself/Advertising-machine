package com.example.Util;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.example.Activity.R;
import com.facebook.drawee.view.SimpleDraweeView;


/**
 * Created by roocky on 03/09.
 *
 * 图片轮播控件ConvenientBanner
 *
 */
public class NetworkImageHolderView implements Holder<BannerItem> {
    private View view;

    @Override
    public View createView(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.banner_item, null, false);
        return view;
    }

    @Override
    public void UpdateUI(Context context, int position, BannerItem data) {
        ((TextView)view.findViewById(R.id.tv_title)).setText(data.getTitle());
        ((SimpleDraweeView)view.findViewById(R.id.sdv_background)).setImageURI(Uri.parse(data.getImage()));
    }
}