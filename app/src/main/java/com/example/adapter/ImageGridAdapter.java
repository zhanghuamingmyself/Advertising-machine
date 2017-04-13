package com.example.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;

import com.example.Activity.R;
import com.example.Util.ScaleImageView;
import com.jcodecraeer.imageloader.ImageLoader;

import java.util.ArrayList;

public class ImageGridAdapter extends BaseAdapter{
    private static final String TAG = "ImageGridAdapter";
    private static final boolean DEBUG = true;
	private ImageLoader mLoader;
	private ArrayList<String> mImageList;
	private LayoutInflater mLayoutInflater;
	public ImageGridAdapter(Context context,   
			ArrayList<String> list) {
		mLoader = new ImageLoader(context);
		mLoader.setIsUseMediaStoreThumbnails(false);
		mImageList = list;
	    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	    int width = wm.getDefaultDisplay().getWidth();//��Ļ���
	    mLoader.setRequiredSize(width / 3); //3��ʾ����
		mLayoutInflater = LayoutInflater.from(context); 
	 
 
	}
	
	public int getCount() {
		return mImageList.size();
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
        if (DEBUG)
            Log.i(TAG, "position = " + position); 
		ViewHolder holder = null;
		if (convertView == null) { 
			convertView = mLayoutInflater.inflate(R.layout.item_image,
					null);
			holder = new ViewHolder();
			holder.imageView = (ScaleImageView) convertView .findViewById(R.id.imageView);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();

		mLoader.DisplayImage(mImageList.get(position), holder.imageView);
		return convertView;
	}

 
	static class ViewHolder {
		ScaleImageView imageView;
	}
}
