package com.example.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.ref.WeakReference;


/**
 * Created by zhang on 2017/6/10.
 */

public class CalendarActivity extends Activity {

    private FloatingActionButton back;
        private static boolean isTime = true;//定时器是否允许
        private static com.example.Activity.CalendarActivity.MyHandler mHandler ;
        private static class MyHandler extends Handler {
            private WeakReference<Context> reference;
            public MyHandler(Context context) {
                reference = new WeakReference<>(context);
            }
            @Override
            public void handleMessage(Message msg) {
                com.example.Activity.CalendarActivity activity = (com.example.Activity.CalendarActivity) reference.get();
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
        private static Runnable mRunnable = new Runnable() {

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
            overridePendingTransition(R.anim.in_from_down, R.anim.out_to_up); //设置切换动画，从下进入，上退出
            finish();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
            super.onCreate(savedInstanceState);
            this.setContentView(R.layout.calendar_activity);

            this.back = (FloatingActionButton)findViewById(R.id.fab);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    close(0X22);
                }
            });





            mHandler = new com.example.Activity.CalendarActivity.MyHandler(this);
            new Thread(mRunnable).start(); //启动新的线程

        }

}
