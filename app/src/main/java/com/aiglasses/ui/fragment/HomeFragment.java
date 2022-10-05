package com.aiglasses.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.aiglasses.R;
import com.aiglasses.aop.SingleClick;
import com.aiglasses.app.AppFragment;
import com.aiglasses.app.TitleBarFragment;
import com.aiglasses.tts.OnlineAPIActivity;
import com.aiglasses.tts.listener.DownloadListener;
import com.aiglasses.tts.model.GetTokenResponse;
import com.aiglasses.tts.network.ApiService;
import com.aiglasses.tts.network.NetCallBack;
import com.aiglasses.tts.network.ServiceGenerator;
import com.aiglasses.ui.activity.*;
import com.hjq.base.FragmentPagerAdapter;
import com.tbruyelle.rxpermissions2.RxPermissions;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * desc: 首页 Fragment
 */
public final class HomeFragment extends TitleBarFragment<HomeActivity> {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private static MediaPlayer mMediaPlayer;
    private boolean isConnect = false;
    private final boolean showConnect = false;
    private Button text_connectstatus;
    private FragmentPagerAdapter<AppFragment<?>> mPagerAdapter;
    private Button button_connect;
    private final int lenth_box = 0;
    //WebView MapWebView = (WebView) findViewById(R.id.map_webView);
    String url = "https://m.amap.com/";
    private static final String TAG = "HomeFragment";
    //输入框
    private EditText etText;
    //界面按钮
    private Button btnSynthApi, btnPlay;
    //APi服务
    private ApiService service;
    //鉴权Token
    private String accessToken;
    //权限请求框架
    private RxPermissions rxPermissions;
    //默认文本，当输入框未输入使用，
    private String defaultText = "你好！百度。";
    //文件路径
    private String mPath;
    //缓冲区大小
    private static int sBufferSize = 8192;
    //文件
    private File file;
    //音频加载是否完成
    private boolean isLoaded = false;
    private int ClickCount = 0;
    private Boolean FristClick = true;
    long timeStart = 0;
    long timeEnd = 0;
    private Boolean canConnect = false;
    private Boolean ConnectedOnece = false;
    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment;
    }

    @Override
    protected void initView() {
        new Thread(this::MainSocketClient).start();

        //访问API获取接口
        //requestApiGetToken();

        etText = findViewById(R.id.et_text);
        btnSynthApi = findViewById(R.id.btn_home_text_to_voice);
        btnPlay = findViewById(R.id.btn_home_voice_start);
        btnSynthApi.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        rxPermissions = new RxPermissions(getAttachActivity());
        setOnClickListener(R.id.btn_home_right, R.id.btn_home_startlisten);
        text_connectstatus = findViewById(R.id.tex_home_connectstatus);
        text_connectstatus.setText("当前状态：未连接");
        button_connect = findViewById(R.id.btn_home_connect);
        button_connect.setText("点击连接");

        button_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (System.currentTimeMillis() - timeEnd > 2000 && canConnect) {
                    canConnect = false;
                    try {
                        UpdateView();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    isConnect = true;
                }
                if (FristClick) {
                    FristClick = false;
                    timeStart = System.currentTimeMillis();
                } else if (System.currentTimeMillis() - timeStart < 1000) {
                    ClickCount ++;
                } else {
                    ClickCount = 0;
                    FristClick = true;
                }
                if (ClickCount == 5) {
                    timeEnd = System.currentTimeMillis();
                    try {
                        isConnect = false;
                        showDialog();
                        TimeUnit.SECONDS.sleep(1);
                        hideDialog();
                        canConnect = true;
                        text_connectstatus.setText("当前状态：未连接");
                        button_connect.setText("点击连接");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        //initPermission();
    }

    public void UpdateView() throws InterruptedException {
        /*
         更新为连接成功后的界面
         */
        showDialog();
        TimeUnit.SECONDS.sleep(2);
        hideDialog();
        text_connectstatus.setText("当前状态：已连接");
        button_connect.setText("点击断开");
        toast("导盲眼镜连接成功！");
    }

    public void MainSocketClient() {

        Socket socket = null;
        BufferedReader br = null;
        try {
            //InetAddress netAddress = InetAddress.getLocalHost();
            //192.168.86.218
            //socket = new Socket("114.115.213.98",8080);
            //socket = new Socket("192.168.31.97",8080);
            socket = new Socket("114.115.213.98", 8888);

            // 创建读取服务端发送消息的流对象
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String SocketIsConnect = br.readLine();

            if ("isConnect".equals(SocketIsConnect)) {
                isConnect = true;
                new Thread(() -> {
                    try {
                        UpdateView();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
            while (true) {
                if (isConnect) {
                    ConnectedOnece = true;
                    String SocketInput = br.readLine();
                    toast("ServerSend: " + SocketInput);
                        /*
                        if("left".equals(SocketInput)){
                            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.sound_left);
                            mMediaPlayer.start();
                        } else if("leftfront".equals(SocketInput)){
                            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.sound_leftfront);
                            mMediaPlayer.start();
                        } else if("front".equals(SocketInput)){
                            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.sound_front);
                            mMediaPlayer.start();
                        } else if("rightfront".equals(SocketInput)){
                            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.sound_rightfront);
                            mMediaPlayer.start();
                        } else if("right".equals(SocketInput)){
                            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.sound_right);
                            mMediaPlayer.start();
                        }
                         */
                    if ("btn_home_front_4".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.front_4_people);
                        mMediaPlayer.start();
                    } else if ("btn_home_front_3_5".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.front_3_5_people);
                        mMediaPlayer.start();
                    } else if ("btn_home_front_3".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.front_3_people);
                        mMediaPlayer.start();
                    } else if ("btn_home_front_2_5".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.front_2_5_people);
                        mMediaPlayer.start();
                    } else if ("btn_home_front_2".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.front_2_people);
                        mMediaPlayer.start();
                    } else if ("btn_home_front_1_5".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.front_1_5_people);
                        mMediaPlayer.start();
                    } else if ("btn_home_front_1".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.front_1_people);
                        mMediaPlayer.start();
                    } else if ("btn_home_leftfront_2_5".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.leftfront_2_5_people);
                        mMediaPlayer.start();
                    } else if ("btn_home_leftfront_2".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.leftfront_2_people);
                        mMediaPlayer.start();
                    } else if ("btn_home_leftfront_1".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.leftfront_1_people);
                        mMediaPlayer.start();
                    } else if ("btn_home_fakeleftfront".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.fakeleftfront);
                        mMediaPlayer.start();
                    } else if ("btn_home_fakefront".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.fakefront);
                        mMediaPlayer.start();
                    } else if ("btn_home_fakerightfront".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.fakerightfront);
                        mMediaPlayer.start();
                    } else if ("btn_home_1m".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.m1);
                        mMediaPlayer.start();
                    } else if ("btn_home_1_5m".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.m1_5);
                        mMediaPlayer.start();
                    } else if ("btn_home_2m".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.m2);
                        mMediaPlayer.start();
                    } else if ("btn_home_2_5m".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.m2_5);
                        mMediaPlayer.start();
                    } else if ("btn_home_3m".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.m3);
                        mMediaPlayer.start();
                    } else if ("btn_home_3_5m".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.m3_5);
                        mMediaPlayer.start();
                    } else if ("btn_home_4m".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.m4);
                        mMediaPlayer.start();
                    } else if ("btn_home_4_5m".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.m4_5);
                        mMediaPlayer.start();
                    } else if ("btn_home_havepeople".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.have_people);
                        mMediaPlayer.start();
                    } else if ("btn_home_havecar".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.have_cars);
                        mMediaPlayer.start();
                    } else if ("btn_home_havemalu".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.have_roads);
                        mMediaPlayer.start();
                    } else if ("btn_home_havezhangai".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.have_barriers);
                        mMediaPlayer.start();
                    } else if ("btn_home_leftguai".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.left_safe);
                        mMediaPlayer.start();
                    } else if ("btn_home_rightguai".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.right_safe);
                        mMediaPlayer.start();
                    } else if ("btn_home_stop".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.pl_stop);
                        mMediaPlayer.start();
                    } else if ("btn_home_gps".equals(SocketInput)) {
                        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.gps_door);
                        mMediaPlayer.start();
                    }
                }
                if (ConnectedOnece && !isConnect) {
                    //socket.close();
                    toast("已断开连接！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SingleClick
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.btn_home_right) {
            startActivity(FakeVoice2Activity.class);
        } /*
        else if (viewId == R.id.btn_home_connect) {
            if (!isConnect) {
                showDialog();
                isConnect = true;
                new Thread(this::MainSocketClient).start();
            } else {
                try {
                    isConnect = false;
                    showDialog();
                    TimeUnit.SECONDS.sleep(2);
                    hideDialog();
                    text_connectstatus.setText("当前状态：未连接");
                    button_connect.setText("点击连接");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        */
        else if (viewId == R.id.btn_home_startlisten) {
            startActivity(FakeVoiceActivity.class);
            //startActivity(OnlineAPIActivity.class);
            //startActivity(ListenActivity.class);
            /*
            Intent intent = getAttachActivity().getPackageManager().getLaunchIntentForPackage("com.llw.xfasrdemo");
            if (intent != null) {
                intent.putExtra("type", "110");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            */
        } else if (viewId == R.id.btn_home_text_to_voice) {
            //在线API合成
            //请求权限 && 合成
            requestPermission();
        } else if (viewId == R.id.btn_home_voice_start) {
            playVoice();
        }
    }


    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getAttachActivity(), perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.
            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(getAttachActivity(), toApplyList.toArray(tmpList), 123);
        }

    }

    /**
     * 访问API获取接口
     */
    private void requestApiGetToken() {
        String grantType = "client_credentials";
        String apiKey = "zESCU0hmwcwf0fXhnGYgPjaG";
        String apiSecret = "KlmmC82ho0IwlAiUzzGqYo8VkV1XG1oZ";
        service = ServiceGenerator.createService(ApiService.class, 0);
        service.getToken(grantType, apiKey, apiSecret)
                .enqueue(new NetCallBack<GetTokenResponse>() {
                    @Override
                    public void onSuccess(Call<GetTokenResponse> call, Response<GetTokenResponse> response) {
                        if (response.body() != null) {
                            //鉴权Token
                            accessToken = response.body().getAccess_token();
                            Log.d(TAG, accessToken);
                        }
                    }

                    @Override
                    public void onFailed(String errorStr) {
                        Log.e(TAG, "获取Token失败，失败原因：" + errorStr);
                        accessToken = null;
                    }
                });
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    @SuppressLint("CheckResult")
    private void requestPermission() {
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(grant -> {
                    if (grant) {
                        //如果输入框的内容为空则使用默认文字进行语音合成
                        String text;
                        if (etText.getText().toString().trim().isEmpty()) {
                            text = defaultText;
                        } else {
                            text = etText.getText().toString().trim();
                        }
                        //发起合成请求
                        requestSynth(text);
                    } else {
                        Toast.makeText(getAttachActivity(), "未获取到权限", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 合成请求
     *
     * @param text 需要合成语音的文本
     */
    private void requestSynth(String text) {
        service = ServiceGenerator.createService(ApiService.class, 1);
        service.synthesis(accessToken, "1", String.valueOf(System.currentTimeMillis()), "zh", text, "106")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            //Log.d(TAG,"请求成功");
                            toast("请求成功");
                            //写入磁盘
                            writeToDisk(response, listener);
                        } else {
                            // Log.d(TAG, "请求失败");
                            toast("请求失败");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        //Log.e(TAG, "error");
                        toast("ERROR");
                    }
                });
    }

    /**
     * 写入磁盘
     *
     * @param response         响应体
     * @param downloadListener 下载监听
     */
    private void writeToDisk(Response<ResponseBody> response, DownloadListener downloadListener) {
        //开始下载
        downloadListener.onStart();
        //输入流  将输入流写入文件
        InputStream is = response.body().byteStream();
        //文件总长
        long totalLength = response.body().contentLength();
        //设置文件存放路径
        //file = new File(Environment.getExternalStorageDirectory(), "test.mp3");
        file = new File(getAttachActivity().getExternalCacheDir() + "/Speech/" + "test.mp3");
        //创建文件
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                downloadListener.onFail("createNewFile IOException");
            }
        }
        //输出流
        OutputStream os = null;
        long currentLength = 0;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            byte data[] = new byte[sBufferSize];
            int len;
            while ((len = is.read(data, 0, sBufferSize)) != -1) {
                os.write(data, 0, len);
                currentLength += len;
                //计算当前下载进度
                downloadListener.onProgress((int) (100 * currentLength / totalLength));
            }
            //下载完成，并返回保存的文件路径
            downloadListener.onFinish(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            downloadListener.onFail("IOException");
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下载文件监听
     */
    private DownloadListener listener = new DownloadListener() {
        @Override
        public void onStart() {
            toast("开始");
            //Log.d(TAG, "开始");
        }

        @Override
        public void onProgress(int progress) {
            //Log.d(TAG, "进度：" + progress);
            toast("进度：" + progress);
        }

        @Override
        public void onFinish(String path) {
            //Log.d(TAG, "完成：" + path);
            toast("完成：" + path);
            mPath = path;
            isLoaded = true;
            playVoice();
            //显示播放控件
            btnPlay.setVisibility(View.VISIBLE);
        }

        @Override
        public void onFail(String errorInfo) {
            //Log.d(TAG, "异常：" + errorInfo);
            toast("异常：" + errorInfo);
        }
    };

    /**
     * 播放
     */
    private void playVoice() {
        if (mPath != null) {
            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(mPath);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
/*


public final class HomeFragment extends TitleBarFragment<HomeActivity>
        implements TabAdapter.OnTabListener, ViewPager.OnPageChangeListener,
        XCollapsingToolbarLayout.OnScrimsListener {

    private XCollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;

    private TextView mAddressView;
    private TextView mHintView;
    private AppCompatImageView mSearchView;

    private RecyclerView mTabView;
    private ViewPager mViewPager;

    private TabAdapter mTabAdapter;
    private FragmentPagerAdapter<AppFragment<?>> mPagerAdapter;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment;
    }

    @Override
    protected void initView() {
        mCollapsingToolbarLayout = findViewById(R.id.ctl_home_bar);
        mToolbar = findViewById(R.id.tb_home_title);

        mAddressView = findViewById(R.id.tv_home_address);
        mHintView = findViewById(R.id.tv_home_hint);
        mSearchView = findViewById(R.id.iv_home_search);

        mTabView = findViewById(R.id.rv_home_tab);
        mViewPager = findViewById(R.id.vp_home_pager);

        mPagerAdapter = new FragmentPagerAdapter<>(this);
        mPagerAdapter.addFragment(StatusFragment.newInstance(), "列表演示");
        mPagerAdapter.addFragment(BrowserFragment.newInstance("https://github.com/getActivity"), "网页演示");
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(this);

        mTabAdapter = new TabAdapter(getAttachActivity());
        mTabView.setAdapter(mTabAdapter);

        // 给这个 ToolBar 设置顶部内边距，才能和 TitleBar 进行对齐
        ImmersionBar.setTitleBar(getAttachActivity(), mToolbar);

        //设置渐变监听
        mCollapsingToolbarLayout.setOnScrimsListener(this);
    }

    @Override
    protected void initData() {
        mTabAdapter.addItem("列表演示");
        mTabAdapter.addItem("网页演示");
        mTabAdapter.setOnTabListener(this);
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Override
    public boolean isStatusBarDarkFont() {
        return mCollapsingToolbarLayout.isScrimsShown();
    }

    /**
     * {@link TabAdapter.OnTabListener}
     */
/*
    @Override
    public boolean onTabSelected(RecyclerView recyclerView, int position) {
        mViewPager.setCurrentItem(position);
        return true;
    }

    /**
     * {@link ViewPager.OnPageChangeListener}


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        if (mTabAdapter == null) {
            return;
        }
        mTabAdapter.setSelectedPosition(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    /**
     * CollapsingToolbarLayout 渐变回调
     *
     * {@link XCollapsingToolbarLayout.OnScrimsListener}

    @SuppressLint("RestrictedApi")
    @Override
    public void onScrimsStateChange(XCollapsingToolbarLayout layout, boolean shown) {
        getStatusBarConfig().statusBarDarkFont(shown).init();
        mAddressView.setTextColor(ContextCompat.getColor(getAttachActivity(), shown ? R.color.black : R.color.white));
        mHintView.setBackgroundResource(shown ? R.drawable.home_search_bar_gray_bg : R.drawable.home_search_bar_transparent_bg);
        mHintView.setTextColor(ContextCompat.getColor(getAttachActivity(), shown ? R.color.black60 : R.color.white60));
        mSearchView.setSupportImageTintList(ColorStateList.valueOf(getColor(shown ? R.color.common_icon_color : R.color.white)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewPager.setAdapter(null);
        mViewPager.removeOnPageChangeListener(this);
        mTabAdapter.setOnTabListener(null);
    }
}
*/
