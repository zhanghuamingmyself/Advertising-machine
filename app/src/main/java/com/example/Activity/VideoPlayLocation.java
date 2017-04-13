package com.example.Activity;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
视频轮播
 */


public class VideoPlayLocation extends Activity implements SurfaceHolder.Callback {
    private SurfaceView surfaceView = null;//视频显示控件
    private SurfaceHolder surfaceHolder = null;
    private MediaPlayer mp = null;

    private List<String> VideoPathList;
    private int videocurrentltem = 0;//视频个数
    private String videoPath = Environment.getExternalStorageDirectory().toString() + "/myapp/video";//视频的扫描路径

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        super.onCreate(savedInstanceState);
        try {
            File videoDir = new File(videoPath);
            VideoPathList = new ArrayList<String>();
            if (!videoDir.exists()) {
                videoDir.mkdirs();
                setResult(0X22);
                finish();
            } else if (videoDir.isDirectory()) {
                getVideoPathFromSD(videoPath);
            }

            mp = new MediaPlayer();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    surfaceView.setBackgroundResource(R.drawable.bg_finish);    //改变SurfaceView的背景图片
                    if (videocurrentltem < VideoPathList.size() - 1) {
                        videocurrentltem++;
                        playVideo(VideoPathList.get(videocurrentltem));
                    } else {
                        videocurrentltem = 0;
                        setResult(0X22);
                        finish();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        surfaceView = new SurfaceView(this);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);
        setContentView(surfaceView);
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mp.isPlaying()) {
                        mp.stop();
                    }
                    mp.reset();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setResult(0X22);
                finish();
            }
        });


    }

    public void playVideo(String path) {

        surfaceView = new SurfaceView(this);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);
        setContentView(surfaceView);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        mp.reset();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setDisplay(surfaceHolder);
        try {
            mp.setDataSource(VideoPathList.get(videocurrentltem));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            mp.prepare();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mp.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }

    /**
     * 从SD卡中获取视频的路径
     */
    private void getVideoPathFromSD(String p) {

        File mFile = new File(p);
        File[] files = mFile.listFiles();


        /* 将所有文件存入ArrayList中 */
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                getVideoPathFromSD(file.getPath());
            }
            if (checkIsVideoFile(file.getPath()))
                VideoPathList.add(file.getPath());
        }
        //   Toast.makeText(videoPlayLocation.this, "扫描了"+VideoPathList.size(), Toast.LENGTH_SHORT).show();
    }

    /**
     * 判断是否相应的视频格式
     */
    private boolean checkIsVideoFile(String fName) {
        boolean isVideoFormat;

        /* 取得扩展名 */
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length())
                .toLowerCase();

        /* 按扩展名的类型决定MimeType */
        if (end.equals("mp4") || end.equals("3gp")) {
            isVideoFormat = true;
        } else {
            isVideoFormat = false;
        }
        return isVideoFormat;
    }


}