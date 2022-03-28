package com.aiglasses.ui.fragment;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import com.aiglasses.R;
import com.aiglasses.aop.SingleClick;
import com.aiglasses.app.TitleBarFragment;
import com.aiglasses.ui.activity.*;
import java.net.InetAddress;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 *    desc   : 首页 Fragment
 */
public final class HomeFragment extends TitleBarFragment<HomeActivity> {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
    private static MediaPlayer mMediaPlayer;
    private boolean isConnect = false;
    private final boolean showConnect = false;
    private Button text_connectstatus;
    private Button button_connect;
    private final int lenth_box = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment;
    }

    @Override
    protected void initView() {
        new Thread(this::MainSocketClient).start();
        //setOnClickListener(R.id.btn_home_left,R.id.btn_home_leftfront,
                //R.id.btn_home_front,R.id.btn_home_rightfront,R.id.btn_home_right);
        text_connectstatus = findViewById(R.id.tex_home_connectstatus);
        text_connectstatus.setText("当前状态：未连接");
        button_connect = findViewById(R.id.btn_home_connect);
        button_connect.setText("点击连接");

    }

    @Override
    protected void initData() {

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
        toast("服务器连接成功！");
    }

    public void MainSocketClient() {

        Socket socket = null;
        BufferedReader br = null;

        try {
            InetAddress netAddress = InetAddress.getLocalHost();
            //192.168.86.218
            //socket = new Socket("114.115.213.98",8080);
            //socket = new Socket("192.168.31.97",8080);
            socket = new Socket(netAddress.getHostAddress(),8080);
            // 创建读取服务端发送消息的流对象
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String SocketIsConnect = br.readLine();
            if ("isConnect".equals(SocketIsConnect)){
                isConnect = true;
                new Thread(() -> {
                    try {
                        UpdateView();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
            if (isConnect){
                while(true) {
                    String SocketInput = br.readLine();
                    toast("服务器发送："+SocketInput);
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
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(socket!= null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } if(br != null){
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
        if (viewId == R.id.btn_home_left) {
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.sound_left);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_leftfront) {
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.sound_leftfront);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_rightfront) {
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.sound_rightfront);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_right) {
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.sound_right);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_front) {
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.sound_front);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_connect) {
            if (!isConnect){
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

        }else if (viewId == R.id.pv_home_video_play_view) {
            new VideoPlayActivity.Builder()
                    .setVideoTitle("测试视频")
                    .setVideoSource("http://vfx.mtime.cn/Video/2019/06/29/mp4/190629004821240734.mp4")
                    .setActivityOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                    .start(getAttachActivity());
        }
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
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
