package com.example.Activity;
/*
团队展示主Activity
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.example.Okhttp.base.BaseActivity;
import com.example.Util.BannerItem;
import com.example.Util.GifView;
import com.example.Util.NetworkImageHolderView;
import com.example.Util.TimeUtil;
import com.example.Weather.ApiClient;
import com.example.Weather.WeatherInfo;
import com.example.Weather.XmlPullParseUtil;
import com.example.adapter.DialogCallback;
import com.example.adapter.LzyResponse;
import com.example.adapter.ServerModel;
import com.example.adapter.ServerModelForFile;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.request.BaseRequest;
import com.nineoldandroids.view.ViewHelper;
import com.squareup.leakcanary.RefWatcher;
import com.wifi.getFile.Data.ChatMessage;
import com.wifi.getFile.Interfaces.ReceiveMsgListener;
import com.wifi.getFile.utils.IpMessageConst;
import com.wifi.getFile.utils.IpMessageProtocol;




import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;

import static com.example.Activity.R.id.imagecodelayout;
import static com.nineoldandroids.view.ViewPropertyAnimator.animate;



public class TeamActivity extends BaseActivity implements View.OnClickListener, ReceiveMsgListener {
    private String TAG = "main";
    public static String hostIp;
    private TextView time_show;//底部显示时间
    private TextView weekTv, temperatureTv, climateTv, windTv, famournameTv, peoplelanguageTv;//天气的显示模块
    private View funtion_aboutus, funtion_more, funtion_weater, funtion_picture, funtion_open, funtion_ourdo, funtion_nearactivity, funtion_famouspeople, funtion_famoursall, mainLayout, funtion_ppt;//每个模块
    private ImageView weatherImg;//显示天气图片，返回按钮
    private FloatingActionButton back;
    private String cityName = "中山";//天气的城市
    private View imagecodeLayout, funtion_imagecode;//
    private View funtionlayoutView;
    private TextView pointToimagecode;
    private Thread gbthread;
    private int[] imageId = new int[]{R.drawable.fnym,
            R.drawable.gdme,
            R.drawable.alk, R.drawable.lbtnys}; // 定义并初始化保存图片id的数组
    private String[] imageSwitchTitle = new String[Application.getInstance().GetImageSwitchTitleCounter()];//ImageSwitch的标题
    private String[] word = new String[Application.getInstance().GetWordCounter()];

    private ImageSwitcher imageSwitcher; // 声明一个图像切换器对象

    private int timenumForF = 0;//名人图片切换计数，每个计数为一秒
    private int imageSwitchTitleIndex = 0;//名人图片索引
    private int timenumForP = 0;//进入自动播放Activity
    private int timenumForL = 0;//语录切换时间计数
    private int wordIndex = 0;//语录索引


    private static boolean TimeRunning = false;
    private String wifimess;
    private String selfName = "广告机";
    private String selfGroup = "android";
    private String receiverIp;            //要接收本activity所发送的消息的用户IP

    private GifView gif2;
    private static Boolean T = true;
    private static String URIAddress = "http://172.16.40.247:57451/";
    private static Runnable mRunnable = new Runnable() {

        public void run() {
            while (T) {
                while (TimeRunning) {
                    try {
                        Thread.sleep(1000);
                        Message msg = new Message();
                        msg.what = 1;  //消息(一个整型值)
                        mHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }

        }
    };
    //在主线程里面处理消息并更新UI界面

    private boolean AorP = true;
    private boolean changeAP = false;

    private static MyHandler mHandler;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Team Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private static class MyHandler extends Handler {
        private WeakReference<Context> reference;

        public MyHandler(Context context) {
            reference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            TeamActivity activity = (TeamActivity) reference.get();
            if (activity != null) {
                switch (msg.what) {
                    case 1:
                        long sysTime = System.currentTimeMillis();
                        CharSequence sysTimeStr = DateFormat.format("hh:mm:ss", sysTime);
                        CharSequence s = DateFormat.format("a", sysTime);
                        if (s.toString().equals("下午") && activity.AorP == true) {
                            activity.AorP = false;
                            // requestJson("http://www.livetune.me:57451/alldir");
                            activity.changeAppBrightness(activity, 30);

                        } else if (s.toString().equals("上午") && activity.AorP == false) {
                            activity.AorP = true;
                            activity.changeAppBrightness(activity, 230);
                        }
                        activity.time_show.setText(sysTimeStr); //更新时间

                        if (activity.timenumForF == 4) {
                            activity.timenumForF = 0;
                            if (activity.imageSwitchTitleIndex < activity.imageId.length) {
                                activity.imageSwitcher.setImageResource(activity.imageId[activity.imageSwitchTitleIndex]);
                                activity.famournameTv.setText(activity.imageSwitchTitle[activity.imageSwitchTitleIndex]);
                                activity.imageSwitchTitleIndex++;
                                activity.weateranim();
                            } else
                                activity.imageSwitchTitleIndex = 0;
                        } else activity.timenumForF++;

                        if (activity.timenumForP == 150) {
                            activity.timenumForP = 0;
                            activity.TimeRunning = false;
                            Intent pictureplayIntent = new Intent(activity, PicturePlayLocation.class);
                            activity.startActivityForResult(pictureplayIntent, 0X11);
                        } else activity.timenumForP++;

                        if (activity.timenumForL == 5) {
                            activity.timenumForL = 0;
                            if (activity.wordIndex < activity.word.length) {
                                activity.peoplelanguageTv.setText(activity.word[activity.wordIndex]);
                                activity.wordIndex++;
                            } else {
                                activity.wordIndex = 0;
                                activity.peoplelanguageTv.setText(activity.word[activity.wordIndex]);
                                activity.wordIndex++;
                            }
                        } else activity.timenumForL++;
                        break;

                    case 2:
                        activity.showLoading();
                        activity.TimeRunning = false;
                        activity.timenumForP = 0;
                        break;
                    case 3:
                        activity.dismissLoading();
                        activity.TimeRunning = true;
                        activity.timenumForP = 0;
                        break;
                    case 4:
                        for (int i = 0; i < activity.needDir.size(); i++) {
                            activity.MakeDir(activity.needDir.get(i));
                            String URL = activity.needDir.get(i).replaceFirst(Environment.getExternalStorageDirectory().toString() + "/myapp/",
                                    URIAddress+"show/"
                            );
                            activity.requestFileJson(URL);
                            Log.e(activity.TAG, "正在请求显示"+"文件夹的文件------" + URL);
                        }

                        break;
                    default:
                        break;

                }
            }
        }
    }

    // 根据亮度值修改当前window亮度随便更新天气进程
    public void changeAppBrightness(Context context, int brightness) {
        new GetWeatherTask(cityName).execute();// 启动更新天气进程
        Window window = ((Activity) context).getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
        }
        window.setAttributes(lp);
    }

    private ConvenientBanner convenientBanner;
    private List<BannerItem> bannerItems = new ArrayList<>();
    private List<String> webPathList = new ArrayList<>();//每张图片的绝对地址

    void getWebPicture() {
        Fresco.initialize(this);
        //生成所需的数据
        //Application.getInstance().readSettingFile();
        String[] webPictureAddr = Application.getInstance().GetWebPictureAddr();
        int counter = Application.getInstance().GetWebPictureCounter();
        while (0 != counter--) {
            webPathList.add(webPictureAddr[counter]);
        }

        for (int i = 0; i < webPathList.size(); i++) {
            bannerItems.add(new BannerItem("第" + i + "张", webPathList.get(i)));
        }

        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, bannerItems)
                .startTurning(4000)     //设置自动切换（同时设置了切换时间间隔）
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT) //设置指示器位置（左、中、右）
                .setPageIndicator(new int[]{R.drawable.dot_unselected, R.drawable.dot_selected})
                //.setManualPageable(true)  //设置手动影响（设置了该项无法手动切换）
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        TimeRunning = false;
                        timenumForP = 0;
                        Intent w = new Intent(TeamActivity.this, WorksActivity.class);
                        startActivityForResult(w, 0X22);
                    }
                }); //设置点击监听事件
    }

    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.activity_team);
        mHandler = new MyHandler(this);
        word = Application.getInstance().GetWord();//获取名人语录
        imageSwitchTitle = Application.getInstance().GetImageSwitchTitle();//获取ImageSwitchTitle
        initView();
        initData();

        if (!isWifiActive()) { // 若wifi没有打开，提示
            //Toast.makeText(this, R.string.no_wifi, Toast.LENGTH_LONG).show();
            Snackbar.make(time_show, R.string.no_wifi, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }

        netThreadHelper.connectSocket(); // 开始监听数据
        gbthread = new Thread(new Runnable() {
            public void run() {
                netThreadHelper.connectSocket(); // 开始监听数据
                netThreadHelper.noticeOnline(); // 广播上线
            }

        });
        gbthread.start();
        netThreadHelper.addReceiveMsgListener(this);    //注册到listeners
        Iterator<ChatMessage> it = netThreadHelper.getReceiveMsgQueue().iterator();
        getWebPicture();
        gif2 = (GifView) findViewById(R.id.gif2);
        gif2.setMovieResource(R.raw.b);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        mShimmerViewContainer = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.setDuration(5000);
        mShimmerViewContainer.setRepeatMode(ObjectAnimator.REVERSE);
        mShimmerViewContainer.startShimmerAnimation();

    }


    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        gbthread = new Thread(new Runnable() {
            public void run() {
                netThreadHelper.noticeOffline(); // 通知下线
                netThreadHelper.disconnectSocket(); // 停止监听
            }
        });
        gbthread.start();
        netThreadHelper.removeReceiveMsgListener(this);
    }

    // 判断wifi是否打开
    public boolean isWifiActive() {
        ConnectivityManager mConnectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnectivity != null) {
            NetworkInfo[] infos = mConnectivity.getAllNetworkInfo();

            if (infos != null) {
                for (NetworkInfo ni : infos) {
                    if ("WIFI".equals(ni.getTypeName()) && ni.isConnected())
                        return true;
                }
            }
        }

        return false;
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
                    weekTv.setText("今天 " + TimeUtil.getWeek(0, TimeUtil.XING_QI));
                    String climate = mApplication.GetAllWeather().getWeather0();
                    climateTv.setText(climate);
                    temperatureTv.setText(mApplication.GetAllWeather().getTemp0());
                    int weatherIcon = R.drawable.biz_plugin_weather_qing;
                    if (climate.contains("转")) {// 天气带转字，取前面那部分
                        String[] strs = climate.split("转");
                        climate = strs[0];
                        if (climate.contains("到")) {// 如果转字前面那部分带到字，则取它的后部分
                            strs = climate.split("到");
                            climate = strs[1];
                        }
                    }
                    if (mApplication.getWeatherIconMap().containsKey(climate)) {
                        weatherIcon = mApplication.getWeatherIconMap().get(climate);
                    }

                    weatherImg.setImageResource(weatherIcon);
                    windTv.setText(mApplication.GetAllWeather().getWind0());


                    break;
                case FAIL:
                    Snackbar.make(time_show, "upadta weater false", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    break;
            }

        }
    }

    private void initData() {
        new GetWeatherTask(cityName).execute();// 启动更新天气进程
        TimeRunning = true;
        new Thread(mRunnable).start(); //启动新的线程
        peoplelanguageTv.setText(word[0]);
    }

    private void initView() {
        // TODO Auto-generated method stub
        time_show = (TextView) findViewById(R.id.time_now);
        weekTv = (TextView) findViewById(R.id.week_today);
        temperatureTv = (TextView) findViewById(R.id.temperature);
        climateTv = (TextView) findViewById(R.id.climate);
        windTv = (TextView) findViewById(R.id.wind);
        famournameTv = (TextView) findViewById(R.id.famourname);
        peoplelanguageTv = (TextView) findViewById(R.id.peoplelanguage);

        weatherImg = (ImageView) findViewById(R.id.weather_img);

        back = (FloatingActionButton) findViewById(R.id.back);
        back.setOnClickListener(this);
        funtion_aboutus = (View) findViewById(R.id.funtion_about_us);
        funtion_aboutus.setOnClickListener(this);
        funtion_more = (View) findViewById(R.id.funtion_more);
        funtion_more.setOnClickListener(this);
        funtion_picture = (View) findViewById(R.id.funtion_picture);
        funtion_picture.setOnClickListener(this);
        funtion_weater = (View) findViewById(R.id.funtion_weater);
        funtion_weater.setOnClickListener(this);
        funtion_nearactivity = (View) findViewById(R.id.funtion_nearactivity);
        funtion_nearactivity.setOnClickListener(this);
        funtion_open = (View) findViewById(R.id.funtion_open);
        funtion_open.setOnClickListener(this);
        funtion_ourdo = (View) findViewById(R.id.funtion_ourdo);
        funtion_ourdo.setOnClickListener(this);
        funtion_famouspeople = (View) findViewById(R.id.famouspeople);
        funtion_famouspeople.setOnClickListener(this);
        funtion_famoursall = (View) findViewById(R.id.famoursall);
        funtion_ppt = (View) findViewById(R.id.funtion_ppt);
        funtion_ppt.setOnClickListener(this);
        funtion_famoursall.setOnClickListener(this);
        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher1); // 获取图像切换器
        // 设置动画效果
        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(TeamActivity.this,
                android.R.anim.fade_in)); // 设置淡入动画
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(TeamActivity.this,
                R.anim.fade_out)); // 设置淡出动画

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            @Override
            public View makeView() {
                ImageView imageView = new ImageView(TeamActivity.this); // 实例化一个ImageView类的对象
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER); // 设置保持纵横比居中缩放图像
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
                        ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT));
                return imageView; // 返回imageView对象
            }

        });
        imageSwitcher.setImageResource(imageId[0]);
        imagecodeLayout = (View) findViewById(imagecodelayout);
        imagecodeLayout.setVisibility(View.INVISIBLE);
        imagecodeLayout.setOnClickListener(this);
        funtion_imagecode = (View) findViewById(R.id.imagecode);
        funtion_imagecode.setOnClickListener(this);
        funtionlayoutView = (View) findViewById(R.id.funtionlayout);
        pointToimagecode = (TextView) findViewById(R.id.textViewpoint);
        mainLayout = (View) findViewById(R.id.mainLayout);
        convenientBanner = (ConvenientBanner) findViewById(R.id.convenientBanner);
    }

    public interface ClickAnimation {
        public void onClick(View v);
    }

    private void animateClickView(final View v, final ClickAnimation callback) {
        float factor = (float)0.8;
        animate(v).scaleX(factor).scaleY(factor).alpha(0).setListener(new com.nineoldandroids.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                ViewHelper.setScaleX(v, 1);
                ViewHelper.setScaleY(v, 1);
                ViewHelper.setAlpha(v, 1);
                if (callback != null) {
                    callback.onClick(v);
                }
                super.onAnimationEnd(animation);
            }
        });
    }

    @Override
    public void onClick(View v) {

        animateClickView(v, new ClickAnimation() {
            @Override
            public void onClick(View v) {
                timenumForP = 0;
                switch (v.getId()) {
                    case R.id.funtion_ppt:
                        Intent ii = new Intent();
                        ii.setClass(TeamActivity.this, PowerPointActivity.class);
                        startActivityForResult(ii, 0X13);
                        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);//设置切换动画，从右边进入，左边退出
                        break;
                    case R.id.funtion_about_us:

                        Dialog alertDialog = new AlertDialog.Builder(TeamActivity.this)
                                .setTitle("关于我们：")
                                .setIcon(android.R.drawable.ic_menu_upload_you_tube)
                                .setMessage(
                                        "Firefly是天启科技于2014年3月成立的开源团队。致力于开源硬件的设计、生产和销售，以及开源文化和知识的推广。同时也提供软硬件定制、产品生产和技术支持等服务。\n"
                                                + "\n"
                                                + "Firefly开源团队由超过40人的专业成员组成，我们擅长Android、Linux的系统级开发、多平台应用开发和云服务支持，以及硬件的电路设计和工业设计。\n"
                                                + "\n"
                                                + "\"Light up your ideas\" 是Firefly的理念，我们希望Firefly对技术的热情和执着能帮助实现你的创意和梦想。\n")
                                .setNegativeButton("确定",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface arg0,
                                                                int arg1) {
                                                // TODO Auto-generated method stub
                                            }
                                        }).create();
                        alertDialog.show();
                        break;
                    case R.id.funtion_more:
                       // updataAPK("new.apk");
                        //changeBackGroundByAPK();
                        //requestJson("http://172.16.40.247:57451/alldir");//获取服务器所有文件信息
                        requestJson(URIAddress+"alldir");
                        break;
                    case R.id.funtion_picture:
                        TimeRunning = false;
                        Intent p = new Intent();
                        p.setClass(TeamActivity.this, PictureGridActivity.class);
                        startActivityForResult(p, 0X14);
                        overridePendingTransition(R.anim.in_from_down, R.anim.out_to_up); //设置切换动画，从下进入，上退出
                        break;
                    case R.id.funtion_weater:
                        //new GetWeatherTask(cityName).execute();// 启动更新天气进程
                        Intent iiiii = new Intent(TeamActivity.this, WeatherActivity.class);
                        startActivityForResult(iiiii, 0X16);
                        overridePendingTransition(R.anim.in_from_down, R.anim.out_to_up); //设置切换动画，从下进入，上退出

                        break;
                    case R.id.back:

                        TimeRunning = false;
                        Intent pictureplayIntent = new Intent(TeamActivity.this, PicturePlayLocation.class);
                        startActivityForResult(pictureplayIntent, 0X11);
                        overridePendingTransition(R.anim.in_from_down, R.anim.out_to_up); //设置切换动画，从下进入，上退出

                       changeBackGroundByAPK();
                        break;
                    case R.id.imagecode:
                        imagecodeLayout.setVisibility(View.VISIBLE);
                        //  funtionlayoutView.setVisibility(View.INVISIBLE);
                        back.setVisibility(View.INVISIBLE);
                        break;
                    case imagecodelayout:
                        timenumForP = 0;
                        //  funtionlayoutView.setVisibility(View.VISIBLE);
                        imagecodeLayout.setVisibility(View.INVISIBLE);
                        back.setVisibility(View.VISIBLE);
                        break;
                    case R.id.funtion_open:
                        TimeRunning = false;
                        Intent i = new Intent();
                        i.setClass(TeamActivity.this, OpenListActivity.class);
                        startActivityForResult(i, 0X12);
                        //设置切换动画，从右边进入，左边退出,带动态效果
                        overridePendingTransition(R.anim.dync_in_from_right, R.anim.dync_out_to_left);
                        break;
                    case R.id.funtion_nearactivity:
                        TimeRunning = false;
                        Intent n = new Intent();
                        n.setClass(TeamActivity.this, NearActivityList.class);
                        startActivityForResult(n, 0X15);
                        //设置切换动画，从右边进入，左边退出,带动态效果
                        overridePendingTransition(R.anim.new_dync_in_from_right, R.anim.new_dync_out_to_left);
                        break;
                    case R.id.famouspeople:
                        timenumForF = 4;
                        break;
                    case R.id.famoursall:
                        timenumForL = 5;
                        break;
                    case R.id.funtion_ourdo:
                        TimeRunning = false;
                        Intent nn = new Intent();
                        nn.setClass(TeamActivity.this, WorksActivity.class);
                        startActivityForResult(nn, 0X15);
                        //设置切换动画，从右边进入，左边退出,带动态效果
                        overridePendingTransition(R.anim.new_dync_in_from_right, R.anim.new_dync_out_to_left);
                    case R.id.time_now:
                        TimeRunning = false;
                        Intent nnn = new Intent();
                        nnn.setClass(TeamActivity.this, CalendarActivity.class);
                        startActivityForResult(nnn, 0X15);
                        //设置切换动画，从右边进入，左边退出,带动态效果
                        overridePendingTransition(R.anim.new_dync_in_from_right, R.anim.new_dync_out_to_left);
                    default:
                        break;
                }
            }
        });
    }


    void weateranim() {
        Animation anim = AnimationUtils.loadAnimation(TeamActivity.this,
                R.anim.alpha_out);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                weatherImg.setVisibility(View.VISIBLE);


            }
        });
        weatherImg.startAnimation(anim);
        Animation anim2 = AnimationUtils.loadAnimation(TeamActivity.this,
                R.anim.point);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                pointToimagecode.setVisibility(View.VISIBLE);


            }
        });
        pointToimagecode.startAnimation(anim2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0X22) {
            TimeRunning = true;
        }
    }

    public void setBackGround(String file) {
        File bg = new File(Environment.getExternalStorageDirectory() + "/myapp" + file);
        if (!bg.exists()) {
            return;
        }
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] bt = new byte[2048];
        //得到文件的输入流
        try {
            InputStream in = new FileInputStream(bg);
            //将文件读出到输出流中
            int readLength = in.read(bt);
            while (readLength != -1) {
                outStream.write(bt, 0, readLength);
                readLength = in.read(bt);
            }
            //转换成byte 后 再格式化成位图
            byte[] data = outStream.toByteArray();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            Drawable bd = new BitmapDrawable(bitmap);
            mainLayout.setBackgroundDrawable(bd);
            in.close();
            outStream.close();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 发送消息并将该消息添加到UI显示
     */
    private InetAddress sendto = null;

    private void sendAndAddMessage(String msm) {
        String msgStr = msm.trim();
        if (!"".equals(msgStr)) {
            //发送消息
            final IpMessageProtocol sendMsg = new IpMessageProtocol();
            sendMsg.setVersion(String.valueOf(IpMessageConst.VERSION));
            sendMsg.setSenderName(selfName);
            sendMsg.setSenderHost(selfGroup);
            sendMsg.setCommandNo(IpMessageConst.IPMSG_SENDMSG);
            sendMsg.setAdditionalSection(msgStr);
            try {
                sendto = InetAddress.getByName(receiverIp);
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                Log.e(TAG, "发送地址有误");
            }
            if (sendto != null)
                gbthread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        netThreadHelper.sendUdpData(sendMsg.getProtocolString() + "\0", sendto, IpMessageConst.PORT);
                        Log.i(TAG, "receiverIP=" + receiverIp);
                    }
                });
            gbthread.start();

        } else {
            makeTextShort("不能发送空内容");
        }


    }

    private String now_dir = Environment.getExternalStorageDirectory() + "/myapp";
    private String before_dir = Environment.getExternalStorageDirectory() + "/myapp";

    public void CMD(String m) {
        timenumForP = 0;
        String[] cmd = m.split(" ");
        switch (cmd[0]) {
            case "setbg":
                File mFile = new File(now_dir);
                File[] files = mFile.listFiles();

                for (int i = 0; i < files.length; i++) {
                    if (files[i].toString() == now_dir + cmd[1]) {
                        setBackGround(now_dir + cmd[1]);
                        sendAndAddMessage("设置背景为:" + now_dir + cmd[1]);
                    }
                }
                break;
            case "cd":
                if (cmd[1].startsWith("/")) {
                    before_dir = now_dir;
                    now_dir = Environment.getExternalStorageDirectory() + "/myapp";
                    now_dir += cmd[1];
                    File mFileforcd = new File(now_dir);
                    if (mFileforcd.isDirectory())
                        sendAndAddMessage("进入到目录:" + now_dir);
                    else {
                        sendAndAddMessage("输入正确的目录");
                        now_dir = before_dir;
                    }
                } else if (cmd[1].equals("..")) {
                    if (now_dir.equals(Environment.getExternalStorageDirectory() + "/myapp")) {
                        sendAndAddMessage("当前目录为根目录,不能返回");
                        break;
                    } else {
                        File fileforcdback = new File(now_dir);
                        now_dir = fileforcdback.getParentFile().toString();
                        if (now_dir.equals(Environment.getExternalStorageDirectory() + "/myapp")) {
                            sendAndAddMessage("当前目录为根目录");
                        } else sendAndAddMessage("当前目录" + now_dir);
                    }
                } else {
                    before_dir = now_dir;
                    now_dir = now_dir + "/";
                    now_dir += cmd[1];
                    File mFileforcd = new File(now_dir);
                    if (mFileforcd.isDirectory())
                        sendAndAddMessage("当前目录" + now_dir);
                    else {
                        sendAndAddMessage("输入正确的目录");
                        now_dir = before_dir;
                    }
                }
                break;
            case "ls":
                File mFileforls = new File(now_dir);
                File[] filesforls = mFileforls.listFiles();

                for (int i = 0; i < filesforls.length; i++) {
                    File file = filesforls[i];
                    if (file.isDirectory())
                        sendAndAddMessage("目录:" + file.getName());
                    else
                        sendAndAddMessage("文件:" + file.getName());
                }
                break;
            case "pwd":
                sendAndAddMessage("当前目录" + now_dir);
                break;
            case "updata weather":
                new GetWeatherTask(cityName).execute();// 启动更新天气进程
                sendAndAddMessage("更新天气");
                break;
            case "open":
                switch (cmd[1]) {
                    case "play":
                        Intent pictureplayIntent = new Intent(TeamActivity.this, PicturePlayLocation.class);
                        startActivityForResult(pictureplayIntent, 0X11);
                        break;
                }
                break;
            case "rm":
                if (cmd[1].startsWith("/")) {
                    before_dir = now_dir;
                    now_dir = Environment.getExternalStorageDirectory() + "/myapp";
                    now_dir += cmd[1];
                    File mFileforcd = new File(now_dir);
                    if (mFileforcd.exists())
                        mFileforcd.delete();
                    else sendAndAddMessage("没有此文件或文件夹");

                } else {
                    before_dir = now_dir;
                    now_dir = now_dir + "/";
                    now_dir += cmd[1];
                    File mFileforcd = new File(now_dir);
                    if (mFileforcd.exists())
                        mFileforcd.delete();
                    else sendAndAddMessage("没有此文件或文件夹");
                }
                break;
            case "fin":
                finish();
                break;
            case "time":
                if (cmd[1] == "start") {
                    Intent inRestart = getIntent();
                    overridePendingTransition(0, 0);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(inRestart);
                    break;
                } else if (cmd[1] == "stop") {
                    TimeRunning = false;
                    break;
                }
                break;
            case "help":
                sendAndAddMessage("1.setbg pictureName 设置壁纸");
                sendAndAddMessage("2.cd 切换目录");
                sendAndAddMessage("3.ls 查看");
                sendAndAddMessage("3.pwd");
                sendAndAddMessage("4.updata weather 更新天气");
                sendAndAddMessage("5.open activityName 打开activity");
                sendAndAddMessage("6.rm 删除");
                sendAndAddMessage("7.fin ");
                sendAndAddMessage("8.time start/stop");
                sendAndAddMessage("9.help");

                break;
        }
    }

    public void MakeDir(String d) {
        File f = new File(d);
        if (!f.exists()) {
            Log.e(TAG, "正在新建文件夹" + f.getPath());
            f.mkdirs();
        }

    }

    public void DelDir(String d) {
        File f = new File(d);
        deleteAllFiles(f);
        f.delete();
        Log.e(TAG, "已经调用删除文件夹函数删除" + d);
    }

    private void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        Log.e(TAG, "删除文件" + f.getPath());
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }

    @Override
    public void processMessage(Message msg) {
        // TODO Auto-generated method stub
        switch (msg.what) {
            case IpMessageConst.IPMSG_SENDMSG:
                CMD(wifimess);
                break;
        }
    }

    @Override
    public boolean receive(ChatMessage msg) {
        // TODO Auto-generated method stub
        sendEmptyMessage(IpMessageConst.IPMSG_SENDMSG); //使用handle通知，来更新UI
        wifimess = msg.getMsg();
        receiverIp = msg.getSenderIp();
        Log.i(TAG, receiverIp);
        return true;
    }

    private String path = Environment.getExternalStorageDirectory().toString()
            + "/myapp/";

    void saveFile(String URL) // 将URL传入就能下载到相应的文件夹
    {
        Log.e(TAG,"正在保存文件-----------"+URL);
        String nowPath = path;
        String fileName = null;
        String[] item = URL.split("/");
        int strLen = item.length;
        for (int i = 2; i < strLen; i++) {
            String s = item[i];
            if (s.equals("open")) {
                nowPath += "open/";
                nowPath += item[i + 1];
                fileName = item[item.length - 1];
                break;
            } else if (s.equals("near")) {
                nowPath += "near/";
                nowPath += item[i + 1];
                fileName = item[item.length - 1];
                break;
            } else if (s.equals("works")) {
                nowPath += "works/";
                nowPath += item[i + 1];
                fileName = item[item.length - 1];
                break;
            } else if (s.equals("picture")) {
                nowPath += "picture/";
                nowPath += item[i + 1];
                fileName = item[item.length - 1];
                break;
            } else if (s.equals("mynewapp")) {
                nowPath += "mynewapp/";
                nowPath += item[i + 1];
                fileName = item[item.length - 1];
            } else if (s.equals("myskin")) {
                nowPath += "myskin/";
                nowPath += item[i + 1];
                fileName = item[item.length - 1];
            } else if (s.equals("video")) {
                nowPath += "video/";
                nowPath += item[i + 1];
                fileName = item[item.length - 1];
            } else if (s.equals("setting")) {
                nowPath += "setting/";
                nowPath += item[i + 1];
                fileName = item[item.length - 1];
            } else if (s.equals("music")) {
                nowPath += "music/";
                nowPath += item[i + 1];
                fileName = item[item.length - 1];
            } else if (s.equals("word")) {
                nowPath += "word/";
                nowPath += item[i + 1];
                fileName = item[item.length - 1];
            } else if (s.equals("ppt")) {
                nowPath += "ppt/";
                nowPath += item[i + 1];
                fileName = item[item.length - 1];
            }

        }
        Log.e(TAG, "down:" + nowPath + "...." + fileName);
        fileDownload(URL, nowPath, fileName);

    }

    private ArrayList<String> MyDir;
    private Boolean LocatDirThreadOver = false;
    private ArrayList<String> netDir;
    private Boolean NetDirThreadOver = false;
    private ArrayList<String> needDir;

    class CheckDirThread extends Thread // 线程查看文件夹
    {

        String json;

        @Override
        public void run() {
            LocatDirThreadOver = false;
            if (MyDir == null) {
                MyDir = new ArrayList<>();
            } else {
                MyDir.clear();
            }
            File Root = new File(Environment.getExternalStorageDirectory()
                    .toString() + "/myapp/");
            File[] Dir1Item = Root.listFiles();

            if (Dir1Item != null) {
                for (int i = 0; i < Dir1Item.length; i++) {
                    if (Dir1Item[i].isDirectory()) {
                        File[] Dir2Item = Dir1Item[i].listFiles();
                        for (int j = 0; j < Dir2Item.length; j++) {
                            if (Dir2Item[j].isDirectory()) {
                                MyDir.add(Dir2Item[j].getPath());
                                Log.e(TAG, "我的本地文件夹有：" + Dir2Item[j].getPath());
                            }
                        }
                    }
                }
            }
            LocatDirThreadOver = true;

            NetDirThreadOver = false;
            if (netDir == null) {
                netDir = new ArrayList<String>();
            } else {
                netDir.clear();
            }
            String path = Environment.getExternalStorageDirectory()
                    .toString() + "/myapp/";
            String[] action = json.split("/");
            Log.i(TAG,"01Dir json get fileList            "+action[0]+"           "+action[1]);
            if (action != null) {
                int actionLen = action.length;
                for (int i = 0; i < actionLen; i++) {
                    if ((!action[i].equals(" ")) && action[i] != null) {
                        String pp = path;
                        String[] item = action[i].split(" ");
                        if (item != null) {
                            int itemLen = item.length;
                            pp += item[0];
                            pp += '/';
                            for (int j = 1; j < itemLen; j++) {
                                if ((!item[j].equals(" "))
                                        && item[j] != null) {
                                    String p = pp;
                                    p += item[j];
                                    netDir.add(p);
                                    Log.e(TAG, "网络文件夹有：" + p);
                                }
                            }
                        }
                    }
                }
            }
            NetDirThreadOver = true;

            if (needDir == null) {
                needDir = new ArrayList<>();
            } else {
                needDir.clear();
            }
            for (int i = 0; i < netDir.size(); i++) {
                int m_diff_n = 0;
                for (int j = 0; j < MyDir.size(); j++) {
                    if (!MyDir.get(j).equals(netDir.get(i))) {
                        m_diff_n++;
                    }
                }
                if (m_diff_n == MyDir.size()) ;
                {
                    needDir.add(netDir.get(i));
                    Log.e(TAG, "I Want the dir" + netDir.get(i));
                }
            }


            for (int i = 0; i < MyDir.size(); i++) {
                int n_diff_m = 0;
                for (int j = 0; j < netDir.size(); j++) {
                    if (!netDir.get(j).equals(MyDir.get(i))) {
                        n_diff_m++;
                    }
                }
                if (n_diff_m == netDir.size()) {
                    DelDir(MyDir.get(i));//删除不要的文件夹
                    Log.e(TAG, "I Want to delect the dir name:" + MyDir.get(i));
                }
            }
            Message msg = new Message();
            msg.what = 4;  //消息(一个整型值)
            mHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
        }

        public CheckDirThread(String json) {
            this.json = json;
        }
    }

    public void fileDownload(String URL, String filePath, String fileName) {
        final String URL_DOWNLOAD = URL;
        final String p = filePath;
        final String f = fileName;
        Thread thread = new Thread(new Runnable() {
            public void run() {
                OkGo.get(URL_DOWNLOAD)//
                        .tag(this)//
                        .execute(new FileCallback(p, f) {
                            @Override
                            public void onBefore(BaseRequest request) {
                                Log.e(TAG, "开始下载");
                                Message msg = new Message();
                                msg.what = 2;  //消息(一个整型值)
                                mHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                            }

                            @Override
                            public void onSuccess(File file, Call call, Response response) {
                                Log.e(TAG, "下载成功");
                                Message msg = new Message();
                                msg.what = 3;  //消息(一个整型值)
                                mHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                            }

                            @Override
                            public void onError(Call call, @Nullable Response response, @Nullable Exception e) {
                                super.onError(call, response, e);
                                Log.e(TAG, "下载错误" + e.toString());
                            }
                        });
            }
        });
        thread.start();

    }


    public void requestJson(String URL) {
        OkGo.get(URL)//
                .tag(this)//
                .execute(new DialogCallback<LzyResponse<ServerModel>>(this) {
                    @Override
                    public void onSuccess(LzyResponse<ServerModel> responseData, Call call, Response response) {
                        handleResponse(responseData.data, call, response);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        handleError(call, response);
                    }
                });
    }

    protected <T> void handleResponse(T data, Call call, Response response) {
        StringBuilder sb;
        if (call != null) {
            Log.i(TAG, "请求成功  请求方式：" + call.request().method() + "\n" + "url：" + call.request().url());

            Headers requestHeadersString = call.request().headers();
            Set<String> requestNames = requestHeadersString.names();
            sb = new StringBuilder();
            for (String name : requestNames) {
                sb.append(name).append(" ： ").append(requestHeadersString.get(name)).append("\n");
            }
            Log.i(TAG, "*@" + sb.toString());
        } else {
            Log.i(TAG, "--");
        }
        if (data == null) {
            Log.i(TAG, "#--");
        } else {
            if (data instanceof String) {
                Log.i(TAG, (String) data);
            } else if (data instanceof List) {
                sb = new StringBuilder();
                List list = (List) data;
                for (Object obj : list) {
                    sb.append(obj.toString()).append("\n");
                }
                Log.i(TAG, sb.toString());
            } else if (data instanceof Set) {
                sb = new StringBuilder();
                Set set = (Set) data;
                for (Object obj : set) {
                    sb.append(obj.toString()).append("\n");
                }
                Log.i(TAG, sb.toString());
            } else if (data instanceof Map) {
                sb = new StringBuilder();
                Map map = (Map) data;
                Set keySet = map.keySet();
                for (Object key : keySet) {
                    sb.append(key.toString()).append(" ： ").append(map.get(key)).append("\n");
                }
                Log.i(TAG, sb.toString());
            } else if (data instanceof File) {
                File file = (File) data;
                Log.i(TAG, "数据内容即为文件内容\n下载文件路径：" + file.getAbsolutePath());
            } else if (data instanceof Bitmap) {
                Log.i(TAG, "图片的内容即为数据");
            } else {
                Log.i(TAG, "网络文件夹json：" + data.toString());
                CheckDirThread c = new CheckDirThread(data.toString());
                c.start();
            }
        }

        if (response != null) {
            Headers responseHeadersString = response.headers();
            Set<String> names = responseHeadersString.names();
            sb = new StringBuilder();
            sb.append("url ： ").append(response.request().url()).append("\n\n");
            sb.append("stateCode ： ").append(response.code()).append("\n");
            for (String name : names) {
                sb.append(name).append(" ： ").append(responseHeadersString.get(name)).append("\n");
            }
            Log.i(TAG, sb.toString());
        } else {
            Log.i(TAG, "--");
        }
    }

    protected void handleError(Call call, Response response) {
        StringBuilder sb;
        if (call != null) {

            Log.i(TAG, "请求失败  请求方式：" + call.request().method() + "\n" + "url：" + call.request().url());

            Headers requestHeadersString = call.request().headers();
            Set<String> requestNames = requestHeadersString.names();
            sb = new StringBuilder();
            for (String name : requestNames) {
                sb.append(name).append(" ： ").append(requestHeadersString.get(name)).append("\n");
            }
            Log.i(TAG, sb.toString());
        } else {
            Log.i(TAG, "--");
            Log.i(TAG, "--");
        }

        Log.i(TAG, "--");
        if (response != null) {
            Headers responseHeadersString = response.headers();
            Set<String> names = responseHeadersString.names();
            sb = new StringBuilder();
            sb.append("stateCode ： ").append(response.code()).append("\n");
            for (String name : names) {
                sb.append(name).append(" ： ").append(responseHeadersString.get(name)).append("\n");
            }
            Log.i(TAG, sb.toString());
        } else {
            Log.i(TAG, "--");
        }
    }


    public void requestFileJson(String URL) {
        OkGo.get(URL)//
                .tag(this)//
                .execute(new DialogCallback<LzyResponse<ServerModelForFile>>(this) {
                    @Override
                    public void onSuccess(LzyResponse<ServerModelForFile> responseData, Call call, Response response) {
                        handleResponse2(responseData.data, call, response);

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        handleError(call, response);
                    }
                });
    }

    protected <T> void handleResponse2(T data, Call call, Response response) {
        StringBuilder sb;
        if (call != null) {
            Log.i(TAG, "请求成功  请求方式：" + call.request().method() + "\n" + "url：" + call.request().url());

            Headers requestHeadersString = call.request().headers();
            Set<String> requestNames = requestHeadersString.names();
            sb = new StringBuilder();
            for (String name : requestNames) {
                sb.append(name).append(" ： ").append(requestHeadersString.get(name)).append("\n");
            }
            Log.i(TAG, "*@" + sb.toString());
        } else {
            Log.i(TAG, "--");
        }
        if (data == null) {
            Log.i(TAG, "#--");
        } else {
            if (data instanceof String) {
                Log.i(TAG, (String) data);
            } else if (data instanceof List) {
                sb = new StringBuilder();
                List list = (List) data;
                for (Object obj : list) {
                    sb.append(obj.toString()).append("\n");
                }
                Log.i(TAG, sb.toString());
            } else if (data instanceof Set) {
                sb = new StringBuilder();
                Set set = (Set) data;
                for (Object obj : set) {
                    sb.append(obj.toString()).append("\n");
                }
                Log.i(TAG, sb.toString());
            } else if (data instanceof Map) {
                sb = new StringBuilder();
                Map map = (Map) data;
                Set keySet = map.keySet();
                for (Object key : keySet) {
                    sb.append(key.toString()).append(" ： ").append(map.get(key)).append("\n");
                }
                Log.i(TAG, sb.toString());
            } else if (data instanceof File) {
                File file = (File) data;
                Log.i(TAG, "数据内容即为文件内容\n下载文件路径：" + file.getAbsolutePath());
            } else if (data instanceof Bitmap) {
                Log.i(TAG, "图片的内容即为数据");
            } else {
                Log.e(TAG, "我需要的文件URL json：" + data.toString());
                String[] fileURL = data.toString().split(" ");
                int fileURLLen = fileURL.length;
                for (int i = 0; i < fileURLLen; i++) {
                    saveFile(fileURL[i]);
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Activity销毁时，取消网络请求
        OkGo.getInstance().cancelTag(this);
        mHandler.removeCallbacksAndMessages(null);
        T = false;
        TimeRunning = false;
        RefWatcher refWatcher = Application.getInstance().getRefWatcher(this);
        refWatcher.watch(this);
    }


    private String mSkinPkgPath = Environment.getExternalStorageDirectory() + File.separator + "mySkin.apk";

    public void changeBackGroundByAPK() {

        File[] fs = new File(Environment.getExternalStorageDirectory().toString() + "/myapp/myskin").listFiles();
        mSkinPkgPath = fs[0].getPath() + "/mySkin.apk";
        if (new File(mSkinPkgPath).exists()) {
            Toast.makeText(TeamActivity.this, "the file is exists!!", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(TeamActivity.this, mSkinPkgPath, Toast.LENGTH_SHORT).show();
        AssetManager assetManager = null;
        try {
            assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, mSkinPkgPath);

            File file = new File(mSkinPkgPath);
            Resources superRes = getResources();
            Resources mResources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());

            ImageView activityImg = (ImageView) findViewById(R.id.img_near);
             /*背景*/
            int mainID = mResources.getIdentifier("img_gallery", "drawable", "com.zhanghuaming.myskin");
            mainLayout.setBackground(mResources.getDrawable(mainID));


            int imag_activityID = mResources.getIdentifier("imag_activity", "drawable", "com.zhanghuaming.myskin");
            activityImg.setBackground(mResources.getDrawable(imag_activityID));


            /*天气文字颜色*/
            int weatherTextColor = mResources.getIdentifier("weatherTextColor", "color", "com.zhanghuaming.myskin");
            int wtColor = mResources.getColor(weatherTextColor);
            weekTv.setTextColor(wtColor);
            temperatureTv.setTextColor(wtColor);
            climateTv.setTextColor(wtColor);
            windTv.setTextColor(wtColor);
            famournameTv.setTextColor(wtColor);
            peoplelanguageTv.setTextColor(wtColor);


            /*时间颜色*/
            int timeTextColor = mResources.getIdentifier("timeTextColor", "color", "com.zhanghuaming.myskin");
            time_show.setTextColor(mResources.getColor(timeTextColor));

            int img_gallery = mResources.getIdentifier("img_gallery", "drawable", "com.zhanghuaming.myskin");
            ImageView gallery = (ImageView) findViewById(R.id.img_gallery);
            gallery.setBackground(mResources.getDrawable(img_gallery));


            int img_about_us = mResources.getIdentifier("img_about_us", "drawable", "com.zhanghuaming.myskin");
            ImageView about_us = (ImageView) findViewById(R.id.img_about_us);
            about_us.setBackground(mResources.getDrawable(img_about_us));


            int img_open = mResources.getIdentifier("img_open", "drawable", "com.zhanghuaming.myskin");
            ImageView open = (ImageView) findViewById(R.id.img_open);
            open.setBackground(mResources.getDrawable(img_open));


            int img_ppt = mResources.getIdentifier("img_ppt", "drawable", "com.zhanghuaming.myskin");
            ImageView ppt = (ImageView) findViewById(R.id.img_ppt);
            ppt.setBackground(mResources.getDrawable(img_ppt));


//            int img_more = mResources.getIdentifier("img_more", "drawable", "com.zhanghuaming.myskin");
//            ImageView more = (ImageView) findViewById(R.id.img_more);
//            more.setBackground(mResources.getDrawable(img_more));


            int img_wechat = mResources.getIdentifier("img_wechat", "drawable", "com.zhanghuaming.myskin");
            ImageView wechat = (ImageView) findViewById(R.id.img_wechat);
            wechat.setBackground(mResources.getDrawable(img_wechat));


            int weaterID = mResources.getIdentifier("funtion_weater", "drawable", "com.zhanghuaming.myskin");
            funtion_weater.setBackground(mResources.getDrawable(weaterID));


            int imagecodeID = mResources.getIdentifier("imagecodelayout", "drawable", "com.zhanghuaming.myskin");
            ImageView imagecode = (ImageView) findViewById(imagecodelayout);
            imagecodeLayout.setBackground(mResources.getDrawable(imagecodeID));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void updataAPK(String APK) {
        String APK_name = APK;
        Intent intent = new Intent("com.zhanghuamingmyself.autoinstallmanager.MAIN");
        File[] fs = new File(Environment.getExternalStorageDirectory().toString() + "/myapp/mynewapp").listFiles();
        String f = fs[0].getPath() + "/" + APK_name;
        Toast.makeText(TeamActivity.this, f, Toast.LENGTH_SHORT).show();
        intent.putExtra("msg", "install " + f);
        //msg的格式(命令,文件全路径);
        startActivity(Intent.createChooser(intent, "MAIN"));
        overridePendingTransition(R.anim.in_from_down, R.anim.out_to_up); //设置切换动画，从下进入，上退出
        finish();
    }
}
