package com.example.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.Activity.R;

import java.io.File;

/**
 * Created by Administrator on 2017/2/13.
 */

public class NearItemFragment extends Fragment {
    private ImageView imageView;
    private String imagePath;//图片的路径

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.near_fragmeant_item,
                container, false);
        imageView = (ImageView) view.findViewById(R.id.nearitem);
        try {
            File file = new File(imagePath);
            String p = imagePath;
            Bitmap bitmap = decodeSampledBitmapFromResource(p,600,1000);
            imageView.setImageBitmap(bitmap);//不会变形
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return view;
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
    public NearItemFragment(String s) {
        super();
        imagePath=s;
    }
    public NearItemFragment() {
        super();
    }
}
