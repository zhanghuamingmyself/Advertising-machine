package com.example.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.adapter.PagerAdapter;
import com.example.fragment.NearItemFragment;
import com.example.indicator.ZoomOutPageTransformerimplements;
import com.squareup.leakcanary.RefWatcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/12/24.
 */

public class NearActivty extends FragmentActivity {
    public final String TAG = "NearActivity";
    private List<Fragment> fragments;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private NearItemFragment nearItemFragment;
    private FloatingActionButton back;

    private List<String> imagePathList;//图片列表
    private String imagePath = Environment.getExternalStorageDirectory().toString() + "/myapp/near/";//图片的扫描路径

    private TextView tv;
    private boolean isTime = true;//定时器是否允许
    private NearActivty.MyHandler mHandler = new NearActivty.MyHandler(this);
    private static class MyHandler extends Handler {
        private WeakReference<Context> reference;
        public MyHandler(Context context) {
            reference = new WeakReference<>(context);
        }
        @Override
        public void handleMessage(Message msg) {
            NearActivty activity = (NearActivty) reference.get();
            if(activity != null){
                switch (msg.what) {
                    case 0X10:
                        activity.close(0X22);
                        break;
                    default:
                        break;

                }
            }
        }
    }
    private Boolean T = true;
    private Runnable mRunnable = new Runnable() {

        public void run() {
            while (T) {
                while (isTime) {
                    try {
                        Thread.sleep(50000);
                        Message msg = mHandler.obtainMessage();
                        msg.what = 0X10;  //消息(一个整型值)
                        mHandler.sendMessage(msg);// 每隔4秒发送一个msg给mHandler
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    };
    public void close(int code) {
        T=false;
        isTime=false;
        setResult(code);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.activity_near_activty);


        Intent intent1 = this.getIntent();
        imagePath = intent1.getStringExtra("path");
        back = (FloatingActionButton) findViewById(R.id.fab);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close(0X22);
            }
        });
        tv = (TextView) findViewById(R.id.textnear);
        readWorldFile(imagePath);
        imagePathList = new ArrayList<String>();
        fragments = new ArrayList<Fragment>();
        getImagePathFromSD(imagePath);
        for (int i = 0; i < imagePathList.size(); i++) {
            fragments.add(new NearItemFragment(imagePathList.get(i)));
        }

        mViewPager = (ViewPager) findViewById(R.id.viewpagerfornear);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformerimplements());
        mPagerAdapter = new PagerAdapter(
                getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mPagerAdapter);
        new Thread(mRunnable).start(); //启动新的线程

    }


    /**
     * 从SD卡中获取资源图片的路径
     */
    private void getImagePathFromSD(String p) {

        File mFile = new File(p);
        File[] files = mFile.listFiles();

        /* 将所有文件存入ArrayList中 */
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                getImagePathFromSD(file.getPath());
            }
            if (checkIsImageFile(file.getPath()))
                imagePathList.add(file.getPath());
        }
    }

    /**
     * 判断是否相应的图片格式
     */
    private boolean checkIsImageFile(String fName) {
        boolean isImageFormat;

        /* 取得扩展名 */
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length())
                .toLowerCase();

        /* 按扩展名的类型决定MimeType */
        if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp")) {
            isImageFormat = true;
        } else {
            isImageFormat = false;
        }
        return isImageFormat;
    }


    public void readWorldFile(String P) {
        BufferedReader bre = null;
        try {
            bre = new BufferedReader(new FileReader(P + "/world.txt"));//此时获取到的bre就是整个文件的缓存流
            String str = null;
            while ((str = bre.readLine()) != null) // 判断最后一行不存在，为空结束循环
            {
                getWorld(str);
            }
            bre.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
    解析world文件
     */
    public void getWorld(String s) {
        String[] w = s.split(" ");
        if (w[0].equals("mess")) {
            String mess=new String();
            for(int i=1;i<w.length;i++)
            {
                mess+=' '+w[i];
            }
            tv.setText(mess);
        }
        else
        {
            Log.e(TAG,"mess is worry to show!!!");
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        RefWatcher refWatcher = Application.getInstance().getRefWatcher(this);
        refWatcher.watch(this);
        System.gc();
    }
}
