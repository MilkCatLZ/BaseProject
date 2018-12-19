package com.base.dcloud;


import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.base.dcloud.CoreStatusListener.OnInitEndListener;

import io.dcloud.EntryProxy;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.DHInterface.IWebviewStateListener;
import io.dcloud.feature.internal.sdk.SDK;


/**
 * Created by LZ on 2017/5/9.
 * DCloud辅助类
 */
public class DCloudManager {
    public interface WebViewStateListener {
        void onWebViewReady();
        void onWebViewStarted();
        void onProgressChange(Integer progress);
        void onPageFinish();
    }
    
    
    Activity activity;
    public EntryProxy mEntryProxy = null;
    CoreStatusListener coreStatusListener = new CoreStatusListener();
    
    public static DCloudManager createInstance(Activity context, OnInitEndListener listener) {
        
        DCloudManager dCloudManager = new DCloudManager();
        dCloudManager.activity = context;
        dCloudManager.coreStatusListener.setListener(listener);
        dCloudManager.mEntryProxy = EntryProxy.init(context, dCloudManager.coreStatusListener);
        dCloudManager.mEntryProxy.onCreate(context, context.getIntent().getExtras(), SDK.IntegratedMode.WEBAPP, null);
        
        
        return dCloudManager;
    }
    
    
    public EntryProxy getmEntryProxy() {
        return mEntryProxy;
    }
    
    IWebview webView = null;
    
    /**
     * 获取WebView
     * @param path DCould项目存放地址
     * @param rootView 用于显示的RootView，加载完成后会自动显示WebView
     * @param webViewStateListener
     * @return
     */
    public IWebview createWebView(String path, final ViewGroup rootView, @Nullable final WebViewStateListener webViewStateListener) {
        
        
        webView = SDK.createWebview(activity, path, new IWebviewStateListener() {
            // 设置Webview事件监听，可在监监听内获取WebIvew加载内容的进度
            @Override
            public Object onCallBack(int pType, Object pArgs) {
                switch (pType) {
                    case IWebviewStateListener.ON_WEBVIEW_READY:
                        // 准备完毕之后添加webview到显示父View中，设置排版不显示状态，避免显示webview时，html内容排版错乱问题
                        ((IWebview) pArgs).obtainFrameView().obtainMainView().setVisibility(View.INVISIBLE);
                        SDK.attach(rootView, ((IWebview) pArgs));
                        if (webViewStateListener != null) {
                            webViewStateListener.onWebViewReady();
                        }
                        
                        break;
                    case IWebviewStateListener.ON_PAGE_STARTED:
                        if (webViewStateListener != null) {
                            webViewStateListener.onWebViewStarted();
                        }
                        break;
                    case IWebviewStateListener.ON_PROGRESS_CHANGED:
                        if (webViewStateListener != null&&pArgs instanceof Integer) {
                            webViewStateListener.onProgressChange((Integer)pArgs);
                        }
                        break;
                    case IWebviewStateListener.ON_PAGE_FINISHED:
                        // 页面加载完毕，设置显示webview
                        if (webView != null) {
                            webView.obtainFrameView().obtainMainView().setVisibility(View.VISIBLE);
                        }
    
                        if (webViewStateListener != null) {
                            webViewStateListener.onPageFinish();
                        }
                        break;
                }
                return null;
            }
        });
        return webView;
    }
    
    
    //Activity中需要实现以下内容

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        return mEntryProxy.onActivityExecute(this, SysEventType.onCreateOptionMenu, menu);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mEntryProxy.onPause(this);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mEntryProxy.onResume(this);
//    }
//
//    public void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        if (intent.getFlags() != 0x10600000) {
//            // 非点击icon调用activity时才调用newintent事件
//            mEntryProxy.onNewIntent(this, intent);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mEntryProxy.onStop(this);
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        boolean _ret = mEntryProxy.onActivityExecute(this, SysEventType.onKeyDown, new Object[]{keyCode, event});
//        return _ret ? _ret : super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        boolean _ret = mEntryProxy.onActivityExecute(this, SysEventType.onKeyUp, new Object[]{keyCode, event});
//        return _ret ? _ret : super.onKeyUp(keyCode, event);
//    }
//
//    @Override
//    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
//        boolean _ret = mEntryProxy.onActivityExecute(this, SysEventType.onKeyLongPress, new Object[]{keyCode, event});
//        return _ret ? _ret : super.onKeyLongPress(keyCode, event);
//    }
//
//    public void onConfigurationChanged(Configuration newConfig) {
//        try {
//            int temp = this.getResources().getConfiguration().orientation;
//            if (mEntryProxy != null) {
//                mEntryProxy.onConfigurationChanged(this, temp);
//            }
//            super.onConfigurationChanged(newConfig);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        mEntryProxy.onActivityExecute(this, SysEventType.onActivityResult, new Object[]{requestCode, resultCode, data});
//    }
}
