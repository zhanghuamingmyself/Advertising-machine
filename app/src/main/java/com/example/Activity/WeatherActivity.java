package com.example.Activity;



import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.example.Util.Near;
import com.example.Weather.ApiClient;
import com.example.Weather.WeatherInfo;
import com.example.Weather.XmlPullParseUtil;
import com.example.adapter.WeatherPagerAdapter;
import com.example.fragment.FirstWeatherFragment;
import com.example.fragment.SecondWeatherFragment;
import com.squareup.leakcanary.RefWatcher;

import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

/**
 * Created by zhang on 2017/4/18.
 */

public class WeatherActivity extends FragmentActivity {


    private final String TAG = "WeatherActivity";


    private static boolean isTime = true;//定时器是否允许
    private static WeatherActivity.MyHandler mHandler ;
    private static class MyHandler extends android.os.Handler {
        private WeakReference<Context> reference;
        public MyHandler(Context context) {
            reference = new WeakReference<>(context);
        }
        @Override
        public void handleMessage(Message msg) {
            NearActivityList activity = (NearActivityList) reference.get();
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
    //private TimeThread timeThread = new TimeThread();
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
        finish();
    }


    private Application mApplication;
    private WeatherPagerAdapter mWeatherPagerAdapter;

    private ViewPager mViewPager;
    private List<Fragment> fragments;
    private  FirstWeatherFragment firstWeatherFragment;
    private  SecondWeatherFragment secondWeatherFragment;
    private String cityName = "中山";//天气的城市
    private FloatingActionButton back;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_weather);
        initData();
        initView();
        mHandler = new WeatherActivity.MyHandler(this);
        new GetWeatherTask(cityName).execute();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        RefWatcher refWatcher = Application.getInstance().getRefWatcher(this);
        refWatcher.watch(this);
    }
    private void initView() {

        fragments = new ArrayList<Fragment>();
        secondWeatherFragment =  new SecondWeatherFragment();
        firstWeatherFragment = new FirstWeatherFragment();
        fragments.add(firstWeatherFragment);
        fragments.add(secondWeatherFragment);


        mViewPager = (ViewPager) findViewById(R.id.viewpagerforweather);
        mWeatherPagerAdapter = new WeatherPagerAdapter(
                getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mWeatherPagerAdapter);
        back=(FloatingActionButton)findViewById(R.id.fab);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close(0X22);
            }
        });
    }

    private void initData() {
        mApplication = Application.getInstance();
    }



    public class GetWeatherTask extends AsyncTask<Void, Void, Integer> {
        private static final String BASE_URL = "http://sixweather.3gpk.net/SixWeather.aspx?city=%s";
        private Application mApplication;
        private String mCity;
        private static final int SCUESS = 0;
        private static final int FAIL = -1;


        public GetWeatherTask(String city) {
            this.mCity = city;
            mApplication = Application.getInstance();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                String url = String.format(BASE_URL,
                        URLEncoder.encode(mCity, "utf-8"));


                // 最后才执行网络请求
                String netResult = ApiClient.connServerForResult(url);
                if (!TextUtils.isEmpty(netResult)) {
                    WeatherInfo allWeather = XmlPullParseUtil
                            .parseWeatherInfo(netResult);
                    if (allWeather != null) {
                        mApplication.SetAllWeather(allWeather);

                    }
                    return SCUESS;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return FAIL;
        }


        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case SCUESS:
                    WeatherInfo weatherInfo = mApplication.GetAllWeather();
                    firstWeatherFragment.updateWeather(weatherInfo);
                    secondWeatherFragment.updateWeather(weatherInfo);
                    break;
                case FAIL:
                    Toast.makeText(WeatherActivity.this, "updata weater false", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }

}
