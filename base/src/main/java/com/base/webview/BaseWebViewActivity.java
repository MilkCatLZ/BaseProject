/*
 * {EasyGank}  Copyright (C) {2015}  {CaMnter}
 *
 * This program comes with ABSOLUTELY NO WARRANTY; for details type `show w'.
 * This is free software, and you are welcome to redistribute it
 * under certain conditions; type `show c' for details.
 *
 * The hypothetical commands `show w' and `show c' should show the appropriate
 * parts of the General Public License.  Of course, your program's commands
 * might be different; for a GUI interface, you would use an "about box".
 *
 * You should also get your employer (if you work as a programmer) or school,
 * if any, to sign a "copyright disclaimer" for the program, if necessary.
 * For more information on this, and how to apply and follow the GNU GPL, see
 * <http://www.gnu.org/licenses/>.
 *
 * The GNU General Public License does not permit incorporating your program
 * into proprietary programs.  If your program is a subroutine library, you
 * may consider it more useful to permit linking proprietary applications with
 * the library.  If this is what you want to do, use the GNU Lesser General
 * Public License instead of this License.  But first, please read
 * <http://www.gnu.org/philosophy/why-not-lgpl.html>.
 */

package com.base.webview;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.base.R;
import com.base.app.BaseApplication;
import com.base.app.BaseApplicationInterface;
import com.base.base.BaseActivity;
import com.base.util.ToastManager;


/**
 * webview基础类，带进图展示
 * 使用方法：
 * 1.继承这个activity
 * 2.实现方法{@link #getWebView()}和{@link #getProgressBar()}方法
 * 3.需要调用的地方使用intent.putExtra(EXTRA_URL,url)传参数
 */
public abstract class BaseWebViewActivity<App extends BaseApplicationInterface> extends BaseActivity<App> {
    
    /**
     * Intent传入的URl
     */
    public static final String EXTRA_URL = "com.camnter.easygank.EXTRA_URL";
    
    /**
     * Intent传入的标题
     */
    public static final String EXTRA_TITLE = "com.camnter.easygank.EXTRA_TITLE";
    
    // For gank api
    public static final String EXTRA_GANK_TYPE = "com.camnter.easygank.EXTRA_GANK_TYPE";
    
    private static final int PROGRESS_RATIO = 1000;
    
    public abstract ProgressBar getProgressBar();
    public abstract WebView getWebView();
    /**
     * 必须在这里初始化view，否则会报错
     * 先
     */
    public abstract void initView();
    /**
     * 注意调用顺序
     * 后
     */
    public abstract void afterOnCreate();
    
    private boolean goBack = false;
    private static final int RESET_GO_BACK_INTERVAL = 2666;
    private static final int MSG_WHAT_RESET_GO_BACK = 206;
    private final Handler mHandler = new Handler(new Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case BaseWebViewActivity.MSG_WHAT_RESET_GO_BACK:
                    BaseWebViewActivity.this.goBack = false;
                    return true;
            }
            return false;
        }
    });
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注意调用问题
        initView();
        this.enableJavascript();
        this.enableCaching();
        this.enableCustomClients();
        this.enableAdjust();
        this.zoomedOut();
        this.getWebView().loadUrl(this.getUrl());
//        this.showBack();
//        this.setTitle(this.getUrlTitle());
        //注意调用问题
        afterOnCreate();
        
        if (this.getGankType() == null) return;
    }
    
    /**
     * @param context Any context
     * @param url     A valid url to navigate to
     */
    public static void toUrl(Context context, String url) {
        toUrl(context, url, android.R.string.untitled);
    }
    
    
    /**
     * @param context    Any context
     * @param url        A valid url to navigate to
     * @param titleResId A String resource to display as the title
     */
    public static void toUrl(Context context, String url, int titleResId) {
        toUrl(context, url, context.getString(titleResId));
    }
    
    
    /**
     * @param context Any context
     * @param url     A valid url to navigate to
     * @param title   A title to display
     */
    public static void toUrl(Context context, String url, String title) {
        Intent intent = new Intent(context, BaseWebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        context.startActivity(intent);
    }
    
    
    /**
     * For gank api
     *
     * @param context context
     * @param url     url
     * @param title   title
     * @param type    type
     */
    public static void toUrl(Context context, String url, String title, String type) {
        Intent intent = new Intent(context, BaseWebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_GANK_TYPE, type);
        context.startActivity(intent);
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_web_view, menu);
        return true;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.web_refresh) {
            this.refreshWebView();
        } else if (item.getItemId() == R.id.web_copy) {
            DeviceUtils.copy2Clipboard(this, this.getWebView().getUrl());
            Snackbar.make(this.getWebView(), this.getString(R.string.common_copy_success),
                          Snackbar.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.web_copy) {
            ShareUtils.share(this, this.getUrl());
        } else {
            this.switchScreenConfiguration(item);
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void switchScreenConfiguration(MenuItem item) {
        if (this.getResources().getConfiguration().orientation ==
            Configuration.ORIENTATION_PORTRAIT) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            if (item != null) item.setTitle(this.getString(R.string.menu_web_vertical));
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
            if (item != null) item.setTitle(this.getString(R.string.menu_web_horizontal));
        }
    }
    
    
    protected void refreshWebView() {
        this.getWebView().reload();
    }
    
    
    private void enableCustomClients() {
        this.getWebView().setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            
            
            /**
             * @param view The WebView that is initiating the callback.
             * @param url  The url of the page.
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url.contains("www.vmovier.com")) {
                    WebViewUtils.injectCSS(BaseWebViewActivity.this,
                                           BaseWebViewActivity.this.getWebView(), "vmovier.css");
                } else if (url.contains("video.weibo.com")) {
                    WebViewUtils.injectCSS(BaseWebViewActivity.this,
                                           BaseWebViewActivity.this.getWebView(), "weibo.css");
                } else if (url.contains("m.miaopai.com")) {
                    WebViewUtils.injectCSS(BaseWebViewActivity.this,
                                           BaseWebViewActivity.this.getWebView(), "miaopai.css");
                }
            }
        });
        this.getWebView().setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                BaseWebViewActivity.this.getProgressBar().setProgress(progress);
//                setProgress(progress * PROGRESS_RATIO);
                if (progress >= 95) {
                    BaseWebViewActivity.this.getProgressBar().setVisibility(View.GONE);
                } else {
                    BaseWebViewActivity.this.getProgressBar().setVisibility(View.VISIBLE);
                }
            }
        });
    }
    
    
    @SuppressLint("SetJavaScriptEnabled")
    private void enableJavascript() {
        this.getWebView().getSettings().setJavaScriptEnabled(true);
        this.getWebView().getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    }
    
    
    private void enableCaching() {
        this.getWebView().getSettings().setAppCachePath(getFilesDir() + getPackageName() + "/cache");
        this.getWebView().getSettings().setAppCacheEnabled(true);
        this.getWebView().getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
    }
    
    
    private void enableAdjust() {
        this.getWebView().getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        this.getWebView().getSettings().setLoadWithOverviewMode(true);
    }
    
    
    private void zoomedOut() {
        this.getWebView().getSettings().setLoadWithOverviewMode(true);
        this.getWebView().getSettings().setUseWideViewPort(true);
        this.getWebView().getSettings().setSupportZoom(true);
    }
    
    
    private String getUrl() {
        return IntentUtils.getStringExtra(this.getIntent(), EXTRA_URL);
    }
    
    
    protected String getUrlTitle() {
        return IntentUtils.getStringExtra(this.getIntent(), EXTRA_TITLE);
    }
    
    
    /**
     * For gank api
     *
     * @return tyep
     */
    private String getGankType() {
        return IntentUtils.getStringExtra(this.getIntent(), EXTRA_GANK_TYPE);
    }
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.getWebView() != null) this.getWebView().destroy();
    }
    
    
    /**
     * Take care of calling onBackPressed() for pre-Eclair platforms.
     *
     * @param keyCode keyCode
     * @param event   event
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 优先拦截没gankType 和 不是video类型的
            if (this.getGankType() == null) {
                this.keyBackProcessScreenLandscape();
//            } else if (GankTypeDict.urlType2TypeDict.get(this.getGankType()) != GankType.video) {
//                this.keyBackProcessScreenLandscape();
            } else {
                if (this.goBack) {
                    this.finish();
                } else {
                    this.goBack = true;
                    Message msg = this.mHandler.obtainMessage();
                    msg.what = MSG_WHAT_RESET_GO_BACK;
                    this.mHandler.sendMessageDelayed(msg, RESET_GO_BACK_INTERVAL);
                    ToastManager.showShortToast(this, this.getString(R.string.common_go_back_tip));
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    
    /**
     * 非视频页面 并 横屏的时候 的细节处理
     */
    private void keyBackProcessScreenLandscape() {
        if (this.getResources().getConfiguration().orientation ==
            Configuration.ORIENTATION_LANDSCAPE) {
            this.switchScreenConfiguration(null);
        } else {
            if (this.getWebView().canGoBack()) {
                this.getWebView().goBack();
            } else {
                this.finish();
            }
        }
    }
}
