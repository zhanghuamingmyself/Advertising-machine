package com.example.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.adapter.PagerAdapter;
import com.example.fragment.MyFirstFragment;
import com.example.fragment.MySecondFragment;
import com.example.indicator.ZoomOutPageTransformerimplements;
import com.squareup.leakcanary.RefWatcher;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/24.
 */

public class OpenActivity extends FragmentActivity {
    private final String TAG = "OpenActivity";
    private List<Fragment> fragments;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private MyFirstFragment firstFragment;
    private MySecondFragment secondFragment;
    private FloatingActionButton back;

    private boolean isTime = true;//定时器是否允许
    private OpenActivity.MyHandler mHandler = new OpenActivity.MyHandler(this);
    private static class MyHandler extends Handler {
        private WeakReference<Context> reference;
        public MyHandler(Context context) {
            reference = new WeakReference<>(context);
        }
        @Override
        public void handleMessage(Message msg) {
            OpenActivity activity = (OpenActivity) reference.get();
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
        setContentView(R.layout.activity_open);


        back=(FloatingActionButton)findViewById(R.id.fab);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(0X22);
                finish();
            }
        });

        String path;
        Intent intent1 = this.getIntent();
        path = intent1.getStringExtra("path");

        fragments = new ArrayList<Fragment>();
        firstFragment = new MyFirstFragment();
        firstFragment.setPath(path);
        secondFragment = new MySecondFragment();
        secondFragment.SetPath(path);
        fragments.add(firstFragment);
        fragments.add(secondFragment);
        mViewPager = (ViewPager) findViewById(R.id.viewpagerForOpen);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformerimplements());
        mPagerAdapter = new PagerAdapter(
                getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mPagerAdapter);
        new Thread(mRunnable).start(); //启动新的线程

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
