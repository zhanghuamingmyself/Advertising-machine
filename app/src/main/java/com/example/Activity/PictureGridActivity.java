package com.example.Activity;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.Util.MultiColumnListView;
import com.example.adapter.ImageGridAdapter;
import com.example.adapter.PLA_AdapterView;
import com.squareup.leakcanary.RefWatcher;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;

public class PictureGridActivity extends Activity {
	private final String TAG="PictureGridActivity";
	private MultiColumnListView mAdapterView = null;
	private ArrayList<String> imageUrls;
	private ImageGridAdapter adapter;
	private FloatingActionButton back;
	private ImageView imageView;
	private int timeNum=0;
	private boolean isTime = true;//定时器是否允许
	private PictureGridActivity.MyHandler mHandler = new PictureGridActivity.MyHandler(this);
	private static class MyHandler extends Handler {
		private WeakReference<Context> reference;
		public MyHandler(Context context) {
			reference = new WeakReference<>(context);
		}
		@Override
		public void handleMessage(Message msg) {
			PictureGridActivity activity = (PictureGridActivity) reference.get();
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
	private Boolean T = true;
	private Runnable mRunnable = new Runnable() {

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
		setContentView(R.layout.activity_picture_grid);
		//mAdapterView = (PLA_AdapterView<Adapter>) findViewById(R.id.list);
		imageView = (ImageView)findViewById(R.id.show);
		imageView.setVisibility(View.INVISIBLE);
		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				imageView.setVisibility(View.INVISIBLE);
			}
		});
		back=(FloatingActionButton)findViewById(R.id.fab);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				close(0X22);
			}
		});
		mAdapterView = (MultiColumnListView) findViewById(R.id.list);
		mAdapterView.setOnItemClickListener(new PLA_AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view, int position, long id) {
				Uri uri = Uri.parse(imageUrls.get(position));
				String p =getRealFilePath(PictureGridActivity.this,uri);
				Log.i(TAG,p);
				Bitmap b = BitmapFactory.decodeFile(p);
				imageView.setBackground(new BitmapDrawable(b));
				imageView.setVisibility(View.VISIBLE);
			}
		});
		imageUrls = new ArrayList<String>();
		adapter = new ImageGridAdapter(this, imageUrls);
		mAdapterView.setAdapter(adapter);
		queryMediaImages();
		new Thread(mRunnable).start(); //启动新的线程
	}

	public String getRealPathFromURI(Uri contentUri) {
		String res = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
		if(cursor.moveToFirst()){
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			res = cursor.getString(column_index);
		}
		cursor.close();
		return res;
	}
	public void queryMediaImages() {
		Cursor c = getContentResolver().query( Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null );
		if ( c != null ) {
		    if (c.moveToFirst()) {            
		           do {         
						long id = c.getLong( c.getColumnIndex( Images.Media._ID ) );
						Uri imageUri = Uri.parse( Images.Media.EXTERNAL_CONTENT_URI + "/" + id );
						imageUrls.add(imageUri.toString());
						//imageUrls.add(getRealFilePath(MainActivity.this, imageUri));
		          } while (c.moveToNext());         
		       }
		}	
		c.close();
		adapter.notifyDataSetChanged();
	}

	public static String getRealFilePath(final Context context, final Uri uri ) {
		if ( null == uri ) return null;
		final String scheme = uri.getScheme();
		String data = null;
		if ( scheme == null )
			data = uri.getPath();
		else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
			data = uri.getPath();
		} else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
			Cursor cursor = context.getContentResolver().query( uri, new String[] { Images.ImageColumns.DATA }, null, null, null );
			if ( null != cursor ) {
				if ( cursor.moveToFirst() ) {
					int index = cursor.getColumnIndex( Images.ImageColumns.DATA );
					if ( index > -1 ) {
						data = cursor.getString( index );
					}
				}
				cursor.close();
			}
		}
		return data;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacksAndMessages(null);
		RefWatcher refWatcher = Application.getInstance().getRefWatcher(this);
		refWatcher.watch(this);
	}
}//end of class
