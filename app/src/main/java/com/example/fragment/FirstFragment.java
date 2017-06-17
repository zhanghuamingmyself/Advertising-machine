package com.example.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.Activity.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FirstFragment extends Fragment {
	private ImageView [] imageView;
	private List<String> imagePathList;//图片列表
	private String[] imglist;//当前图片的绝对路径吧
	private String imagePath = Environment.getExternalStorageDirectory().toString() + "/myapp/picture";//图片的扫描路径

	public void setPath(String p)
	{
		imagePath=p;
	}




	/**
	 * 从SD卡中获取资源图片的路径
	 */
	private void getImagePathFromSD() {

		try {
			File mFile = new File(imagePath);
			File[] files = mFile.listFiles();

        /* 将所有文件存入ArrayList中 */
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isDirectory()) {
					getImagePathFromSD();
				}
				if (checkIsImageFile(file.getPath()))
					imagePathList.add(file.getPath());
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



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		File fileDir;


		View view = inflater.inflate(R.layout.firstlayout,
				container, false);
		imageView = new ImageView[10];
		imageView[0]=(ImageView)view.findViewById(R.id.IVfir1);
		imageView[1]=(ImageView)view.findViewById(R.id.IVfir2);
		imageView[2]=(ImageView)view.findViewById(R.id.IVfir3);
		imageView[3]=(ImageView)view.findViewById(R.id.IVfir4);
		imageView[4]=(ImageView)view.findViewById(R.id.IVfir5);
		imageView[5]=(ImageView)view.findViewById(R.id.IVfir6);
		imageView[6]=(ImageView)view.findViewById(R.id.IVfir7);
		imageView[7]=(ImageView)view.findViewById(R.id.IVfir8);
		imageView[8]=(ImageView)view.findViewById(R.id.IVfir9);
		imageView[9]=(ImageView)view.findViewById(R.id.IVfir10);
		imagePathList = new ArrayList<String>();
		try {
			fileDir=new File(imagePath);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			} else if (fileDir.isDirectory()) {
				getImagePathFromSD();
				imglist = imagePathList.toArray(new String[imagePathList.size()]);
				int j;
				if(imagePathList.size()>10)
				{
					j=10;
				}
				else
				{
					j=imagePathList.size();
				}
				for(int i=0;i<j;i++)
				{
					String p=imagePathList.get(i);
					Bitmap bitmap = decodeSampledBitmapFromResource(p,600,1000);
					imageView[i].setImageBitmap(bitmap);//不会变形
				}
			}
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


}
