package com.example.Activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.Util.Open;
import com.example.adapter.OpenAdapter;
import com.squareup.leakcanary.RefWatcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/15.
 */

public class OpenListActivity extends Activity {
    private final String TAG = "openListActivity";
    //定义数据
    private List<Open> mData;
    private List<String> path;
    //定义ListView对象
    private ListView mListViewArray;
    private static boolean isTime = true;//定时器是否允许
    private static OpenListActivity.MyHandler mHandler;
    private static class MyHandler extends Handler {
        private WeakReference<Context> reference;
        public MyHandler(Context context) {
            reference = new WeakReference<>(context);
        }
        @Override
        public void handleMessage(Message msg) {
            OpenListActivity activity = (OpenListActivity) reference.get();
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
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_open_list);
        //为ListView对象赋值
        mListViewArray = (ListView) findViewById(R.id.open_list);
        LayoutInflater inflater = getLayoutInflater();
        //初始化数据
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close(0X22);
            }
        });
        initData();
        //创建自定义Adapter的对象
        OpenAdapter adapter = new OpenAdapter(inflater, mData);
        //将布局添加到ListView中
        mListViewArray.setAdapter(adapter);
        mListViewArray.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(OpenListActivity.this, OpenActivity.class);
                intent.putExtra("path", path.get(position));
                startActivityForResult(intent,0X11);
                overridePendingTransition(R.anim.in_from_down, R.anim.out_to_up); //设置切换动画，从下进入，上退出
            }
        });
        mHandler = new OpenListActivity.MyHandler(this);
        new Thread(mRunnable).start(); //启动新的线程
    }

    /*
    初始化数据
    */
    private void initData() {
        path = new ArrayList<String>();
        mData = new ArrayList<Open>();
        imagePathList = new ArrayList<String>();
        Name = new ArrayList<String>();
        Date = new ArrayList<String>();
        getOpenItemPathFromSD();

    }

    /**
     * 从SD卡中获取资源图片的路径
     */
    private String openItemPath = Environment.getExternalStorageDirectory().toString() + "/myapp/open/";//扫描路径
    private List<String> imagePathList;//图片列表
    private List<String> Date;
    private List<String> Name;

    private void getOpenItemPathFromSD() {
        File mFile = new File(openItemPath);
        File[] files = mFile.listFiles();
        /* 将所有文件存入ArrayList中 */
        if(files.length !=0) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isDirectory()) {
                    path.add(file.getPath());
                    getImagePathFromSD(file);
                    mData.add(new Open(Name.get(i), Date.get(i), imagePathList.get(i)));
                }
            }
        }
    }

    private void getImagePathFromSD(File f) {

        File mFile = f;
        File[] files = mFile.listFiles();
        readWorldFile(mFile);
        /* 将所有文件存入ArrayList中 */
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (checkIsImageFile(file.getPath())) {
                imagePathList.add(file.getPath());
                break;
            }
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



    /*
        获取world文件
     */
    public void readWorldFile(File f) {
        BufferedReader bre = null;
        try {
            bre = new BufferedReader(new FileReader(f.getPath()+"/world.txt"));//此时获取到的bre就是整个文件的缓存流
            String str = null;
            while ((str = bre.readLine()) != null) // 判断最后一行不存在，为空结束循环
            {
                getWorld(str);//解析设置文件
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
        if (w[0].equals("date") ) {
            Date.add(w[1]);
        } else if (w[0].equals("name")) {
            Name.add(w[1]);
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0X22) {
            close(0X22);
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
