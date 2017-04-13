package com.example.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Activity.R;
import com.example.Util.Open;

import java.util.List;

/**
 * Created by Administrator on 2017/1/15.
 */

public class OpenAdapter extends BaseAdapter {
    private List<Open> mData;//定义数据。
    private LayoutInflater mInflater;//定义Inflater,加载我们自定义的布局。
    private ViewHolder viewHolder;
    /*
    定义构造器，在Activity创建对象Adapter的时候将数据data和Inflater传入自定义的Adapter中进行处理。
    */
    public OpenAdapter(LayoutInflater inflater, List<Open> data){
        mInflater = inflater;
        mData = data;
    }
    @Override
    public int getCount() {
        return mData.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertview, ViewGroup viewGroup) {
//获得ListView中的view

        if(convertview == null)
        {
            convertview =  mInflater.inflate(R.layout.open_item_layout,null);
            //获得自定义布局中每一个控件的对象。
            ImageView imagePhoto = (ImageView) convertview.findViewById(R.id.image_photo);
            TextView name = (TextView) convertview.findViewById(R.id.textview_name);
            TextView date = (TextView) convertview.findViewById(R.id.textview_date);
            viewHolder = new ViewHolder();
            viewHolder.setImagePhoto(imagePhoto);
            viewHolder.setDate(date);
            viewHolder.setName(name);
            convertview.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertview.getTag();
        }

//获得对象
        Open item = mData.get(position);

//将数据一一添加到自定义的布局中。
        Bitmap bitmap = decodeSampledBitmapFromResource(item.getImag(),600,1000);
        viewHolder.getImagePhoto().setImageBitmap(bitmap);
        viewHolder.getName().setText(item.getName());
        viewHolder.getDate().setText(item.getDate());
        return convertview ;
    }
    public static  class ViewHolder
    {
        public void setName(TextView name) {
            this.name = name;
        }

        public void setImagePhoto(ImageView imagePhoto) {
            this.imagePhoto = imagePhoto;
        }

        public void setDate(TextView date) {
            this.date = date;
        }

        public ImageView getImagePhoto() {
            return imagePhoto;
        }

        public TextView getName() {
            return name;
        }

        public TextView getDate() {
            return date;
        }

        ImageView imagePhoto;
        TextView name;
        TextView date;
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
            , int reqWidth, int reqHeight) {
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