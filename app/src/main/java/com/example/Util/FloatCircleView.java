package com.example.Util;


import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.view.View;

public class FloatCircleView extends View{

    public int width=150;
    public int height=150;
    private Paint circlepaint;
    private Paint textpaint;
    private String text="返回";
    private boolean drag=false;
	private Bitmap prcBitmap;
	private Bitmap bitmap;
	private void initpaints() {
		// TODO Auto-generated method stub
		circlepaint=new Paint();
		circlepaint.setColor(Color.GRAY);
		circlepaint.setAntiAlias(true);
		
		textpaint=new Paint();
		textpaint.setTextSize(25);
		textpaint.setAntiAlias(true);
		textpaint.setColor(Color.WHITE);
		textpaint.setFakeBoldText(true);
		prcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_btn_speak_now);
		bitmap = Bitmap.createScaledBitmap(prcBitmap, width, height, true);
	}

	public FloatCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		initpaints();
	}

	public FloatCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initpaints();
	}

	public FloatCircleView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initpaints();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		if(drag)
		{
			canvas.drawBitmap(bitmap,0,0,null);
			}
		else {
			canvas.drawCircle(width/2, height/2, width/2,circlepaint);

			float textwidth=textpaint.measureText(text);
			float x=width/2-textwidth/2;
			FontMetrics metrics=textpaint.getFontMetrics();
			float dy=-(metrics.descent+metrics.ascent)/2;
			float y=height/2+dy;
			canvas.drawText(text, x, y, textpaint);
		}
		
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(width,height);
		
	}

	public void setDragState(boolean b) {
		// TODO Auto-generated method stub
		drag=b;
		invalidate();
	}
	


}
