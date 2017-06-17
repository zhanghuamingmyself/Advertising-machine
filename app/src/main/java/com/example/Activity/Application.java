package com.example.Activity;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.example.Weather.WeatherInfo;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.store.PersistentCookieStore;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Application extends android.app.Application {
    private String TAG = "App";
    private WeatherInfo allweather;
    private static Application mApplication;
    private HashMap<String, Integer> mWeatherIcon;// 天气图标

    public static synchronized Application getInstance() {
        return mApplication;
    }


    public static RefWatcher getRefWatcher(Context context) {
        Application application = (Application) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        refWatcher = LeakCanary.install(this);
        readSettingFile();
        readWordFile();
        readImageSwitchTitleFile();




        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
        params.put("commonParamsKey2", "这里支持中文参数");
        //-----------------------------------------------------------------------------------//


        //必须调用初始化
        OkGo.init(this);

        //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
        //好处是全局参数统一,特定请求可以特别定制参数
        try {
            //以下都不是必须的，根据需要自行选择,一般来说只需要 debug,缓存相关,cookie相关的 就可以了
            OkGo.getInstance()

                    //打开该调试开关,控制台会使用 红色error 级别打印log,并不是错误,是为了显眼,不需要就不要加入该行
                    .debug("OkGo")

                    //如果使用默认的 60秒,以下三行也不需要传
                    .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)  //全局的连接超时时间
                    .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)     //全局的读取超时时间
                    .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)    //全局的写入超时时间

                    //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体其他模式看 github 介绍 https://github.com/jeasonlzy0216/
                    .setCacheMode(CacheMode.NO_CACHE)

                    //可以全局统一设置缓存时间,默认永不过期,具体使用方法看 github 介绍
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)

                    //可以全局统一设置超时重连次数,默认为三次,那么最差的情况会请求4次(一次原始请求,三次重连请求),不需要可以设置为0
                    .setRetryCount(3)

                    //如果不想让框架管理cookie,以下不需要
//                .setCookieStore(new MemoryCookieStore())                //cookie使用内存缓存（app退出后，cookie消失）
                    .setCookieStore(new PersistentCookieStore())          //cookie持久化存储，如果cookie不过期，则一直有效

                    //可以设置https的证书,以下几种方案根据需要自己设置
//                    .setCertificates()                                  //方法一：信任所有证书（选一种即可）
//                    .setCertificates(getAssets().open("srca.cer"))      //方法二：也可以自己设置https证书（选一种即可）
//                    .setCertificates(getAssets().open("aaaa.bks"), "123456", getAssets().open("srca.cer"))//方法三：传入bks证书,密码,和cer证书,支持双向加密

                    //可以添加全局拦截器,不会用的千万不要传,错误写法直接导致任何回调不执行
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        return chain.proceed(chain.request());
//                    }
//                })

                    //这两行同上,不需要就不要传
                    .addCommonHeaders(headers)                                         //设置全局公共头
                    .addCommonParams(params);                                          //设置全局公共参数
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WeatherInfo GetAllWeather() {
        return allweather;
    }

    public void SetAllWeather(WeatherInfo allweather) {
        this.allweather = allweather;
    }

    private HashMap<String, Integer> initWeatherIconMap() {
        if (mWeatherIcon != null && !mWeatherIcon.isEmpty())
            return mWeatherIcon;
        mWeatherIcon = new HashMap<String, Integer>();
        mWeatherIcon.put("暴雪", R.drawable.biz_plugin_weather_baoxue);
        mWeatherIcon.put("暴雨", R.drawable.biz_plugin_weather_baoyu);
        mWeatherIcon.put("大暴雨", R.drawable.biz_plugin_weather_dabaoyu);
        mWeatherIcon.put("大雪", R.drawable.biz_plugin_weather_daxue);
        mWeatherIcon.put("大雨", R.drawable.biz_plugin_weather_dayu);

        mWeatherIcon.put("多云", R.drawable.biz_plugin_weather_duoyun);
        mWeatherIcon.put("雷阵雨", R.drawable.biz_plugin_weather_leizhenyu);
        mWeatherIcon.put("雷阵雨冰雹",
                R.drawable.biz_plugin_weather_leizhenyubingbao);
        mWeatherIcon.put("晴", R.drawable.biz_plugin_weather_qing);
        mWeatherIcon.put("沙尘暴", R.drawable.biz_plugin_weather_shachenbao);

        mWeatherIcon.put("特大暴雨", R.drawable.biz_plugin_weather_tedabaoyu);
        mWeatherIcon.put("雾", R.drawable.biz_plugin_weather_wu);
        mWeatherIcon.put("小雪", R.drawable.biz_plugin_weather_xiaoxue);
        mWeatherIcon.put("小雨", R.drawable.biz_plugin_weather_xiaoyu);
        mWeatherIcon.put("阴", R.drawable.biz_plugin_weather_yin);

        mWeatherIcon.put("雨夹雪", R.drawable.biz_plugin_weather_yujiaxue);
        mWeatherIcon.put("阵雪", R.drawable.biz_plugin_weather_zhenxue);
        mWeatherIcon.put("阵雨", R.drawable.biz_plugin_weather_zhenyu);
        mWeatherIcon.put("中雪", R.drawable.biz_plugin_weather_zhongxue);
        mWeatherIcon.put("中雨", R.drawable.biz_plugin_weather_zhongyu);
        return mWeatherIcon;
    }

    public Map<String, Integer> getWeatherIconMap() {
        if (mWeatherIcon == null || mWeatherIcon.isEmpty())
            mWeatherIcon = initWeatherIconMap();
        return mWeatherIcon;
    }

    public int getWeatherIcon(String climate) {
        int weatherRes = R.drawable.biz_plugin_weather_qing;
        if (TextUtils.isEmpty(climate))
            return weatherRes;
        String[] strs = { "晴", "晴" };
        if (climate.contains("转")) {// 天气带转字，取前面那部分
            strs = climate.split("转");
            climate = strs[0];
            if (climate.contains("到")) {// 如果转字前面那部分带到字，则取它的后部分
                strs = climate.split("到");
                climate = strs[1];
            }
        }
        if (mWeatherIcon == null || mWeatherIcon.isEmpty())
            mWeatherIcon = initWeatherIconMap();
        if (mWeatherIcon.containsKey(climate)) {
            weatherRes = mWeatherIcon.get(climate);
        }
        return weatherRes;
    }


    private int webPictureCounter = 0;
    private String[] webPictureAddr = new String[10];//网络图片最多10张
    private String settingFileName = Environment.getExternalStorageDirectory().toString() + "/myapp/setting";
    private String imagePlayPath,musicPlayPath,videoPlayPath;
    /*
        获取设置文件
     */
    public void readSettingFile() {
        BufferedReader bre = null;
        File[] fs=new File(settingFileName).listFiles();
        try {
            bre = new BufferedReader(new FileReader(fs[0].toString()+"/setting.txt"));//此时获取到的bre就是整个文件的缓存流
            String str = null;
            while ((str = bre.readLine()) != null) // 判断最后一行不存在，为空结束循环
            {
                getSetting(str);//解析设置文件
            }
            bre.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
    解析设置文件
     */
    public void getSetting(String s) {
        String[] set = s.split(" ");
        switch (set[0]) {
            case "model":
                switch (set[1]) {
                    case "work":

                }
                break;
            case "webPictureAddr":
                webPictureAddr[webPictureCounter] = set[1];
                webPictureCounter++;
                break;
            case "imagePlayPath":
                imagePlayPath=set[1];
                break;
            case"musicPlayPath":
                musicPlayPath=set[1];
                break;
            case "videoPlayPath":
                videoPlayPath=set[1];
                break;

            default:

        }
    }

    public String[] GetWebPictureAddr() {
        return webPictureAddr;
    }

    public int GetWebPictureCounter() {
        return webPictureCounter;
    }
    public String GetMusicPlayPath()
    {
        return musicPlayPath;
    }
    public String GetImagePlayPath()
    {
        return imagePlayPath;
    }
    public String GetVideoPlayPath()
    {
        return videoPlayPath;
    }




    private int wordCounter = 0;
    private String[] word = new String[30];//word最多30句
    private String WordFileName = Environment.getExternalStorageDirectory().toString() + "/myapp/word";

    /*
        获取word文件
     */
    public void readWordFile() {
        BufferedReader bre = null;
        try {
            File[] fs=new File(WordFileName).listFiles();
            bre = new BufferedReader(new FileReader(fs[0].getPath()+"/word.txt"));//此时获取到的bre就是整个文件的缓存流
            String str = null;
            while ((str = bre.readLine()) != null) // 判断最后一行不存在，为空结束循环
            {
                getWord(str);//解析设置文件
            }
            bre.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /*
解析word文件
*/
    public void getWord(String s) {
        String[] w = s.split(" ");
        String mess=new String();
        for(int i=1;i<w.length;i++)
        {
            mess+=' '+w[i];
        }
        word[wordCounter] =mess;
        wordCounter++;

    }

    public String[] GetWord() {
        return word;
    }

    public int GetWordCounter() {
        return wordCounter;
    }




    /*
        获取ImageSwitch文件
     */
    private int ImageSwitchTitleCounter = 0;
    private String[] ImageSwitchTitle = new String[30];//ImageSwitch最多30张图切换,这里是显示图片的标题
    private String ImageSwitchTitleFileName = Environment.getExternalStorageDirectory().toString() + "/myapp/word";

    public void readImageSwitchTitleFile() {
        BufferedReader bre = null;
        try {
            File[] fs=new File(ImageSwitchTitleFileName).listFiles();
            bre = new BufferedReader(new FileReader(fs[0].toString()+"/imageSwitchTitle.txt"));//此时获取到的bre就是整个文件的缓存流
            String str = null;
            while ((str = bre.readLine()) != null) // 判断最后一行不存在，为空结束循环
            {
                getImageSwitchTitle(str);//解析设置文件
            }
            bre.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
    解析ImageSwitch文件
     */

    public void getImageSwitchTitle(String s) {
        String[] w = s.split(" ");
        ImageSwitchTitle[ImageSwitchTitleCounter] = w[1];
        ImageSwitchTitleCounter++;

    }

    public String[] GetImageSwitchTitle() {
        return ImageSwitchTitle;
    }

    public int GetImageSwitchTitleCounter() {
        return ImageSwitchTitleCounter;
    }
}
