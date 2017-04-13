package com.example.Util;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.example.Activity.R;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

public class pictureplayActivity extends Activity{
    private ConvenientBanner convenientBanner;
    private List<BannerItem> bannerItems = new ArrayList<>();
    private List<String> webPathList=new ArrayList<>();//每张图片的绝对地址
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.activity_pictureplay);

        convenientBanner = (ConvenientBanner)findViewById(R.id.convenientBanner);
        //生成所需的数据
        webPathList.add("http://pic3.zhimg.com/da1fcaf6a02d1223d130d5b106e828b9.jpg");
        webPathList.add( "http://p1.zhimg.com/dd/f1/ddf10a04227ea50fd59746dbcd13c728.jpg");
        webPathList.add("http://p3.zhimg.com/64/5c/645cde143c9a371005f3f749366cffad.jpg");

        for (int i = 0; i < webPathList.size(); i ++) {
            bannerItems.add(new BannerItem("第" + i + "张", webPathList.get(i)));
        }

        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, bannerItems)
         .startTurning(5000)     //设置自动切换（同时设置了切换时间间隔）
         .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT) //设置指示器位置（左、中、右）
         .setPageIndicator(new int[] {R.drawable.dot_unselected, R.drawable.dot_selected})
         //.setManualPageable(true)  //设置手动影响（设置了该项无法手动切换）
         .setOnItemClickListener(new OnItemClickListener() {
             @Override
             public void onItemClick(int position) {
                 finish();
             }
         }); //设置点击监听事件
    }

}
