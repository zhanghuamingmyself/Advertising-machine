package com.example.Activity;
/*
本地图片轮播
在myapp/picture下一定要有图片
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.squareup.leakcanary.RefWatcher;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class PicturePlayLocation extends Activity {
    private String TAG = "PicturePlayLocation";
    private ImageView img;
    private List<String> imagePathList;//图片列表
    private List<String> musicPathList;//播放音乐列表
    private int currentltem = 0;//当前播放音乐的索引
    private int imgindex = 0;
    private String[] imglist;//当前图片的绝对路径吧
    private Application mApplication;
    private String imagePath = Environment.getExternalStorageDirectory().toString() + "/myapp/picture/";//图片的扫描路径
    private String musicPath = Environment.getExternalStorageDirectory().toString() + "/myapp/music/";//音乐的扫描路径
    private MediaPlayer mp;//音乐的播放器
    private int anim_index = 0;
    private boolean isTime = true;//定时器是否允许
    //在主线程里面处理消息并更新UI界面
    private Random random = new Random();
    private static PicturePlayLocation.MyHandler mHandler ;

    private static class MyHandler extends Handler {
        private WeakReference<Context> reference;
        public MyHandler(Context context) {
            reference = new WeakReference<>(context);
        }
        @Override
        public void handleMessage(Message msg) {
            PicturePlayLocation activity = (PicturePlayLocation) reference.get();
            if(activity != null){
                switch (msg.what) {
                    case 0X10:
                        activity.anim_index = activity.random.nextInt(activity.anims.length);
                        activity.weateranim(activity.anim_index);
                        activity.playpicture();
                        break;
                    default:
                        break;

                }
            }
        }
    }

    private static PicturePlayLocation.TimeThread timeThread ;
    private static Boolean T = true;

    class TimeThread extends Thread {
        @Override
        public void run() {
            while (T) {
                while (isTime) {
                    try {
                        Thread.sleep(6000);
                        Message msg = new Message();
                        msg.what = 0X10;  //消息(一个整型值)
                        mHandler.sendMessage(msg);// 每隔4秒发送一个msg给mHandler
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        T=false;
        try {
            if (mp.isPlaying()) {
                mp.stop();
            }
            isTime = false;
            mp.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        RefWatcher refWatcher = Application.getInstance().getRefWatcher(this);
        refWatcher.watch(this);
        System.gc();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_pictureplaylocation);
        mHandler = new PicturePlayLocation.MyHandler(this);
        timeThread = new PicturePlayLocation.TimeThread();

        init();
        if (musicPathList.size() > 0) {
            playMusic(musicPathList.get(currentltem));
        }
    }

    public void init() {
        T=true;
        isTime=true;
        img = (ImageView) findViewById(R.id.imageView1);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close(0X22);
            }
        });
        File pictureDir = new File(imagePath);
        File musicDir = new File(musicPath);
        imagePathList = new ArrayList<String>();
        musicPathList = new ArrayList<String>();
        if (!pictureDir.exists()) {
            playVideo();
        } else if (pictureDir.isDirectory()) {
            getImagePathFromSD(imagePath);
            if (imagePathList.size() != 0) {
                imglist = imagePathList.toArray(new String[imagePathList.size()]);
            }
        }
        if (!musicDir.exists()) {
            musicDir.mkdirs();
        } else if (musicDir.isDirectory()) {
            getMusicPathFromSD(musicPath);
        }
        if(imglist.length>0) {
            playpicture();
            timeThread.start(); //启动新的线程
        }else {
            playVideo();

        }

        if (0 != musicPathList.size()) {
            mp = new MediaPlayer();        //实例化MediaPlayer对象
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (currentltem < musicPathList.size() - 1) {
                        playMusic(musicPathList.get(currentltem));
                        currentltem++;
                    } else {
                        currentltem = 0;
                        isTime = false;
                        playVideo();
                    }
                }
            });

        }
    }

    public void playpicture() {

        img.setImageURI(Uri.parse(imglist[imgindex]));
        if (++imgindex >= imglist.length) {
            imgindex = 0;
            playVideo();
        }
    }

    /*
    启动视频轮播
     */
    public void playVideo() {
        try {
            if (mp.isPlaying()) {
                mp.stop();
            }
            mp.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
        T=false;
        isTime = false;
        Intent i = new Intent(PicturePlayLocation.this, VideoPlayLocation.class);
        startActivityForResult(i, 0X20);
        overridePendingTransition(R.anim.out_to_up,R.anim.in_from_down); //设置切换动画，从下进入，上退出

    }

    public void playMusic(String path) {
        try {
            if (mp.isPlaying()) {
                mp.stop();
            }
            mp.reset();
            mp.setDataSource(path);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从SD卡中获取资源图片的路径
     */
    private void getImagePathFromSD(String p) {

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
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

    /**
     * 从SD卡中获取资源音乐的路径
     */
    private void getMusicPathFromSD(String p) {

        try {
            File mFile = new File(p);
            File[] files = mFile.listFiles();

        /* 将所有文件存入ArrayList中 */
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isDirectory()) {
                    getMusicPathFromSD(file.getPath());
                }
                if (checkIsMusicFile(file.getPath()))
                    musicPathList.add(file.getPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否相应的音乐格式
     */
    private boolean checkIsMusicFile(String fName) {
        boolean isMusicFormat;

        /* 取得扩展名 */
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length())
                .toLowerCase();

        /* 按扩展名的类型决定MimeType */
        if (end.equals("mp3") || end.equals("wav") || end.equals("3gp")) {
            isMusicFormat = true;
        } else {
            isMusicFormat = false;
        }
        return isMusicFormat;
    }


    private int[] anims = {R.anim.activity_close, R.anim.alpha_out, R.anim.base_slide_remain, R.anim.base_slide_right_in
            , R.anim.base_slide_right_out, R.anim.fade_in, R.anim.fade_out, R.anim.hyperspace_in, R.anim.hyperspace_out
            , R.anim.pop_menu_enter_anim, R.anim.pop_menu_out_anim, R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_out, R.anim.push_up_in
            , R.anim.push_up_out, R.anim.scale_in, R.anim.scale_rotate_in, R.anim.scale_translate, R.anim.scale_translate_rotate, R.anim.slide_down_out, R.anim.slide_left
            , R.anim.slide_right, R.anim.slide_up_in, R.anim.view_pull_refresh_slide_in_from_bottom, R.anim.view_pull_refresh_slide_in_from_top, R.anim.view_pull_refresh_slide_out_to_bottom
            , R.anim.view_pull_refresh_slide_out_to_bottom, R.anim.view_pull_refresh_slide_out_to_top, R.anim.wave_scale, R.anim.zoom_enter, R.anim.zoom_exit,R.anim.in_from_down,R.anim.out_to_up,
            R.anim.dync_in_from_right,R.anim.dync_out_to_left};


    void weateranim(int i) {
        Animation anim = AnimationUtils.loadAnimation(PicturePlayLocation.this,
                anims[i]);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                img.setVisibility(View.VISIBLE);


            }
        });
        img.startAnimation(anim);
    }

    public void close(int code) {
        mHandler.removeCallbacksAndMessages(null);
        T=false;
        isTime=false;
        setResult(code);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        close(0X22);

    }
}
