package com.example.adapter;


import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

import com.example.Util.FloatCircleView;

public class FloatViewManager {
	private Context context;
	private WindowManager wm;
	private FloatCircleView floatCircleView;
	private OnTouchListener onTouchListener=new OnTouchListener() {
		
		private float startx;
		private float starty;

		@SuppressLint("ClickableViewAccessibility") @Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch(event.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				startx = event.getRawX();
				starty = event.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				float x=event.getRawX();
				float y=event.getRawY();
				float dx=x-startx;
				float dy=y-starty;
				params.x+=dx;
				params.y+=dy;
				floatCircleView.setDragState(true);
				wm.updateViewLayout(floatCircleView, params);
				startx=x;
				starty=y;
				break;
				case MotionEvent.ACTION_UP:
					float x1=event.getRawX();
					if(x1>getScreenWidth()/2)
						params.x=getScreenWidth()-floatCircleView.width;
					else {
						params.x=0;
					}
					floatCircleView.setDragState(false);
					wm.updateViewLayout(floatCircleView, params);

			default:
				break;
			}
			return false;
		}
	};
	@SuppressWarnings("deprecation")
	public int getScreenWidth()
	{
		return wm.getDefaultDisplay().getWidth();
		
	}
	@SuppressLint("ClickableViewAccessibility")
	public FloatViewManager(final Context context)
	{
		this.context=context;
		wm=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		floatCircleView=new FloatCircleView(context);
		floatCircleView.setOnTouchListener(onTouchListener);
		floatCircleView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wm.removeView(floatCircleView);
				Instrumentation inst = new Instrumentation();
				inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
			}


		});
	}

	private static FloatViewManager instance;
	private WindowManager.LayoutParams params;

	public static FloatViewManager getInstance(Context context)
	{
		synchronized(FloatViewManager.class)
		{
			if(instance==null)
			{
				instance=new FloatViewManager(context);
			}
		}
		return instance;
		
	
	}
	public void showFloatcircleView() {
		
		params = new WindowManager.LayoutParams();
	    params.type = WindowManager.LayoutParams.TYPE_PHONE;
	    params.gravity = Gravity.LEFT | Gravity.BOTTOM-30;
	    params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
	    params.width = floatCircleView.width;
	    params.height = floatCircleView.height;
	    params.x = 0;
	    params.y = 0;
	    params.format=PixelFormat.RGBA_8888;
	    wm.addView(floatCircleView, params);
		
	}


}
