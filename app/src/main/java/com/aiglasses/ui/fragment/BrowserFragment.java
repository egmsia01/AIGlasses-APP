package com.aiglasses.ui.fragment;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.NonNull;

import com.aiglasses.aop.SingleClick;
import com.aiglasses.ui.activity.BrowserActivity;
import com.aiglasses.R;
import com.aiglasses.action.StatusAction;
import com.aiglasses.aop.CheckNet;
import com.aiglasses.aop.Log;
import com.aiglasses.app.AppActivity;
import com.aiglasses.app.AppFragment;
import com.aiglasses.widget.BrowserView;
import com.aiglasses.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;


public final class BrowserFragment extends AppFragment<AppActivity>
        implements StatusAction, OnRefreshListener {

    private static MediaPlayer mMediaPlayer;
    private static final String INTENT_KEY_IN_URL = "url";

    @Log
    public static BrowserFragment newInstance(String url) {
        BrowserFragment fragment = new BrowserFragment();
        Bundle bundle = new Bundle();
        bundle.putString(INTENT_KEY_IN_URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    private StatusLayout mStatusLayout;
    private SmartRefreshLayout mRefreshLayout;
    private BrowserView mBrowserView;

    @Override
    protected int getLayoutId() {
        return R.layout.browser_fragment;
    }


    @Override
    protected void initView() {
        setOnClickListener(R.id.btn_home_gps);
        mStatusLayout = findViewById(R.id.hl_browser_hint);
        mRefreshLayout = findViewById(R.id.sl_browser_refresh);
        mBrowserView = findViewById(R.id.wv_browser_view);

        // 设置 WebView 生命周期回调
        mBrowserView.setLifecycleOwner(this);
        // 设置网页刷新监听
        //mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        mBrowserView.setBrowserViewClient(new AppBrowserViewClient());
        mBrowserView.setBrowserChromeClient(new BrowserView.BrowserChromeClient(mBrowserView));
        mBrowserView.loadUrl(getString(INTENT_KEY_IN_URL));
        showLoading();
    }

    @Override
    public StatusLayout getStatusLayout() {
        return mStatusLayout;
    }

    /**
     * 重新加载当前页
     */
    @CheckNet
    private void reload() {
        mBrowserView.reload();
    }

    /**
     * {@link OnRefreshListener}
     */

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        reload();
    }

    @SingleClick
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.btn_home_gps) {
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.gps_door);
            mMediaPlayer.start();
        }
    }
    private class AppBrowserViewClient extends BrowserView.BrowserViewClient {

        /**
         * 网页加载错误时回调，这个方法会在 onPageFinished 之前调用
         */
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // 这里为什么要用延迟呢？因为加载出错之后会先调用 onReceivedError 再调用 onPageFinished
            post(() -> showError(listener -> reload()));
        }

        /**
         * 开始加载网页
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {}

        /**
         * 完成加载网页
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            mRefreshLayout.finishRefresh();
            showComplete();
        }

        /**
         * 跳转到其他链接
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, final String url) {
            String scheme = Uri.parse(url).getScheme();
            if (scheme == null) {
                return true;
            }
            switch (scheme.toLowerCase()) {
                // 如果这是跳链接操作
                case "http":
                case "https":
                    BrowserActivity.start(getAttachActivity(), url);
                    break;
                default:
                    break;
            }
            // 已经处理该链接请求
            return true;
        }
    }
}