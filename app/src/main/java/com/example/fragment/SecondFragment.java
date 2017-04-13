package com.example.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Activity.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class SecondFragment extends Fragment {

    private final String TAG = "SecondFragment";
    private TextView tv;
    private ImageView imageView;
    private String Path;//图片的扫描路径


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.secondlayout,
                container, false);
        tv = (TextView) view.findViewById(R.id.textViewSec);
        imageView = (ImageView) view.findViewById(R.id.imageViewsec);
        getImagePathFromSD(Path);
        readWorldFile(Path);
        return view;
    }

    public void SetPath(String p) {
        Path = p;
        Log.e(TAG, p);
    }


    public void readWorldFile(String P) {
        BufferedReader bre = null;
        try {
            bre = new BufferedReader(new FileReader(P + "/world.txt"));//此时获取到的bre就是整个文件的缓存流
            Log.e(TAG, "i find a txt file names:" + P + "/world.txt");
            String str = null;
            while ((str = bre.readLine()) != null) // 判断最后一行不存在，为空结束循环
            {
                Log.e(TAG, str);
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
            String mess = new String();
            for (int i = 1; i < w.length; i++) {
                mess += ' ' + w[i];
            }
            tv.setText(mess);
        } else {
            Log.e(TAG, "mess is worry to show!!!");
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
                } else {
                    if (checkIsImageFile(file.getPath())) {
                        Log.e(TAG,"find a picture:"+file.getPath());
                        Bitmap bitmap = decodeSampledBitmapFromResource(file.getPath(),600,1000);
                        imageView.setImageBitmap(bitmap);//不会变形
                    }
                }

            }
        }catch (Exception e)
        {
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
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            //计算缩放比例
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
    public static Bitmap decodeSampledBitmapFromResource(String p
            ,int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(p,options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(p, options);
    }
}
