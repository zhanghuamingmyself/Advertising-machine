package com.example.Server;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.adapter.FloatViewManager;

public class MyFloatservice extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		FloatViewManager floatViewManager=FloatViewManager.getInstance(this);
		floatViewManager.showFloatcircleView();
		
	}

}
