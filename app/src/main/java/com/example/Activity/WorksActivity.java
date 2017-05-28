package com.example.Activity;

import android.content.Context;
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

import com.example.adapter.PagerAdapter;
import com.example.fragment.WorksFragment;
import com.example.indicator.ZoomOutPageTransformerimplements;
import com.squareup.leakcanary.RefWatcher;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/12/24.
 */

public class WorksActivity extends FragmentActivity {
    public final String TAG="WorksActivity";
    private List<Fragment> fragments;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private FloatingActionButton back;
    private String Path = Environment.getExternalStorageDirectory().toString() + "/myapp/works";//图片的扫描路径


    private static boolean isTime = true;//定时器是否允许
    private static WorksActivity.MyHandler mHandler;
    private static class MyHandler extends Handler {
        private WeakReference<Context> reference;
        public MyHandler(Context context) {

            reference = new WeakReference<>(context);
        }
        @Override
        public void handleMessage(Message msg) {
            WorksActivity activity = (WorksActivity) reference.get();
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
    private static Boolean T = true;
    private  static Runnable mRunnable = new Runnable() {
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
        setResult(code);
        T=false;
        isTime=false;
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.activity_works);

        back = (FloatingActionButton) findViewById(R.id.fab);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close(0X22);
            }
        });
        fragments = new ArrayList<Fragment>();
        File f=new File(Path);
        File[] item=f.listFiles();
        f =null;
        for (int i = 0; i < item.length; i++) {
            WorksFragment worksFragment=new WorksFragment();
            worksFragment.SetPath(item[i].getPath());
            Log.e(TAG,"fragments add :"+item[i].getPath());
            fragments.add(worksFragment);
        }

        mViewPager = (ViewPager) findViewById(R.id.viewpagerforworks);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformerimplements());
        mPagerAdapter = new PagerAdapter(
                getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mPagerAdapter);
        mHandler = new WorksActivity.MyHandler(this);
        new Thread(mRunnable).start(); //启动新的线程

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        RefWatcher refWatcher = Application.getInstance().getRefWatcher(this);
        refWatcher.watch(this);
    }
}
