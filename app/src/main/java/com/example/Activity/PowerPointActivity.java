package com.example.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.Util.FloatCircleView;
import com.olivephone.office.TempFileManager;
import com.olivephone.office.powerpoint.DocumentSession;
import com.olivephone.office.powerpoint.DocumentSessionBuilder;
import com.olivephone.office.powerpoint.DocumentSessionStatusListener;
import com.olivephone.office.powerpoint.IMessageProvider;
import com.olivephone.office.powerpoint.ISystemColorProvider;
import com.olivephone.office.powerpoint.android.AndroidMessageProvider;
import com.olivephone.office.powerpoint.android.AndroidSystemColorProvider;
import com.olivephone.office.powerpoint.android.AndroidTempFileStorageProvider;
import com.olivephone.office.powerpoint.view.PersentationView;
import com.olivephone.office.powerpoint.view.SlideShowNavigator;
import com.olivephone.office.powerpoint.view.SlideView;
import com.squareup.leakcanary.RefWatcher;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class PowerPointActivity extends Activity implements
		DocumentSessionStatusListener {
	private String TAG = "PowerPointActivity";
	private PersentationView content;
	private DocumentSession session;
	private SlideShowNavigator navitator;


	private int currentSlideNumber;
	private FloatingActionButton prev;
	private FloatingActionButton next;
	private FloatingActionButton back;
	private SeekBar scale;

	private List<String> PPTPathList;
	private int PPTcurrent = 0;//PPT个数
	private String PPTPath = Environment.getExternalStorageDirectory().toString() + "/myapp/ppt";//PPT的扫描路径

	private static boolean isTime = true;//定时器是否允许
	private static PowerPointActivity.MyHandler mHandler ;
	private static class MyHandler extends Handler {
		private WeakReference<Context> reference;
		public MyHandler(Context context) {
			reference = new WeakReference<>(context);
		}
		@Override
		public void handleMessage(Message msg) {
			PowerPointActivity activity = (PowerPointActivity) reference.get();
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

		try {
			File PPTDir = new File(PPTPath);
			PPTPathList = new ArrayList<String>();
			if (!PPTDir.exists()) {
				PPTDir.mkdirs();
				setResult(0X22);
				finish();
			} else if (PPTDir.isDirectory()) {
				getPPTPathFromSD(PPTPath);
			}
		}catch (Exception e)
		{
			e.printStackTrace();
		}




		this.setContentView(R.layout.activity_powerpoint);
		this.content = (PersentationView) this.findViewById(R.id.content);
		this.prev = (FloatingActionButton) this.findViewById(R.id.prev);
		this.prev.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				prev();
			}
		});
		this.next = (FloatingActionButton) this.findViewById(R.id.next);
		this.next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				next();
			}
		});
		this.back = (FloatingActionButton)findViewById(R.id.fab);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				close(0X22);
			}
		});
		this.scale = (SeekBar) this.findViewById(R.id.scale);
		this.scale
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {

						if (progress < 1) {
							progress = 1;
						}
						PowerPointActivity.this.content
								.notifyScale(progress / 250.0);
					}
				});




		getPPT();
		mHandler = new PowerPointActivity.MyHandler(this);
		new Thread(mRunnable).start(); //启动新的线程

	}

	void getPPT()
	{
		prev.setVisibility(View.INVISIBLE);
		next.setVisibility(View.INVISIBLE);
		try {
			Context context = PowerPointActivity.this.getApplicationContext();
			IMessageProvider msgProvider = new AndroidMessageProvider(context);
			TempFileManager tmpFileManager = new TempFileManager(
					new AndroidTempFileStorageProvider(context));
			ISystemColorProvider sysColorProvider = new AndroidSystemColorProvider();

			session = new DocumentSessionBuilder(new File(PPTPathList.get(PPTcurrent)))
					.setMessageProvider(msgProvider)
					.setTempFileManager(tmpFileManager)
					.setSystemColorProvider(sysColorProvider)
					.setSessionStatusListener(this).build();
			session.startSession();
			prev.setVisibility(View.VISIBLE);
			next.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void onStart() {
		super.onStart();
		this.content.setContentView(null);
	}

	@Override
	protected void onDestroy() {
		if (this.session != null) {
			this.session.endSession();

		}
		super.onDestroy();
		mHandler.removeCallbacksAndMessages(null);
		RefWatcher refWatcher = Application.getInstance().getRefWatcher(this);
		refWatcher.watch(this);
		System.gc();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Toast.makeText(this,
		// "(" + event.getRawX() + "," + event.getRawY() + ")",
		// Toast.LENGTH_SHORT).show();
		return super.onTouchEvent(event);
	}

	@Override
	public void onSessionStarted() {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(PowerPointActivity.this, "onSessionStarted",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onDocumentReady() {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(PowerPointActivity.this, "onDocumentReady",
						Toast.LENGTH_SHORT).show();
				PowerPointActivity.this.navitator = new SlideShowNavigator(
						PowerPointActivity.this.session.getPPTContext());
				PowerPointActivity.this.currentSlideNumber = PowerPointActivity.this.navitator
						.getFirstSlideNumber() - 1;
				PowerPointActivity.this.next();
			}
		});
	}

	@Override
	public void onDocumentException(Exception e) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(PowerPointActivity.this, "onDocumentException",
						Toast.LENGTH_SHORT).show();
				PowerPointActivity.this.finish();
			}
		});
	}

	@Override
	public void onSessionEnded() {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(PowerPointActivity.this, "onSessionEnded",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void navigateTo(int slideNumber) {
		SlideView slideShow = this.navitator.navigateToSlide(
				this.content.getGraphicsContext(), slideNumber);
		this.content.setContentView(slideShow);
	}

	private void next() {
		if (this.navitator != null) {
			if (this.navitator.getFirstSlideNumber()
					+ this.navitator.getSlideCount() - 1 > this.currentSlideNumber) {
				this.navigateTo(++this.currentSlideNumber);
			} else {
				if(PPTcurrent<PPTPathList.size()-1) {
					PPTcurrent++;
					getPPT();
				}
				else
					Log.i(TAG,"is last file");
			}
		}
	}

	private void prev() {
		if (this.navitator != null) {
			if (this.navitator.getFirstSlideNumber() < this.currentSlideNumber) {
				this.navigateTo(--this.currentSlideNumber);
			} else {
				if(PPTcurrent!=0) {
					PPTcurrent--;
					getPPT();
				}
				else
					Log.i(TAG,"is first file");
			}
		}

	}



	/**
	 * 从SD卡中获取ppt的路径
	 */
	private void getPPTPathFromSD(String p) {

		File mFile = new File(p);
		File[] files = mFile.listFiles();

        /* 将所有文件存入ArrayList中 */
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.isDirectory()) {
				getPPTPathFromSD(file.getPath());
			}
			if (checkIsPPTFile(file.getPath()))
				PPTPathList.add(file.getPath());
		}

	}

	/**
	 * 判断是否相应的ppt格式
	 */
	private boolean checkIsPPTFile(String fName) {
		boolean isPPTFormat;

        /* 取得扩展名 */
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

        /* 按扩展名的类型决定MimeType */
		if (end.equals("ppt") || end.equals("pptx")) {
			isPPTFormat = true;
		} else {
			isPPTFormat = false;
		}
		return isPPTFormat;
	}



}
