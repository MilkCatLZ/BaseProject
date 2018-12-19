package com.base.base;


import android.app.Activity;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.base.app.BaseApplicationInterface;
import com.base.delegate.BaseDelegate;
import com.base.interfaces.BaseInterface;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public abstract class BaseActivity<App extends BaseApplicationInterface> extends AppCompatActivity implements BaseInterface {
    
    @NonNull
    protected String tag = '/' + getClass().getSimpleName();
    private BaseDelegate tcBaseDelegate = new BaseDelegate();
    public App app;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            app = getBaseApplicationInterface();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tcBaseDelegate.onCreate(this, savedInstanceState, app);
        if (app != null) {
            app.addActivity(this);
        }
    }
    
    /**
     * @return 返回主项目中定制的App
     */
    protected abstract App getBaseApplicationInterface();
    
    @Override
    protected void onResume() {
        super.onResume();
        tcBaseDelegate.onResume();
    }
    
    @Override
    protected void onPause() {
        ProgressDialog.hideLoadingView(this);
        super.onPause();
        tcBaseDelegate.onPause();
    }
    
    @Override
    protected void onDestroy() {
        app.removeActivity(this);
        tcBaseDelegate.onDestroy(this);
        super.onDestroy();
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        tcBaseDelegate.onOptionsItemSelected(item, this);
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * 对原有方法的保留
     *
     * @return 显示做标题的Activity名称
     */
    public CharSequence getActivityName() {
        return "";
    }
    
    @Override
    public CharSequence getTitleText() {
        return getActivityName();
    }
    
    
    protected ViewDataBinding getViewDataBind() {
        return null;
    }
    
    @Override
    public ViewDataBinding getViewDataBinds() {
        return getViewDataBind();
    }
    
    @Override
    public EventBus getEventBusDefault() {
        return tcBaseDelegate.getEventBusDefault();
    }
    
    @Override
    public void register(Object o) {
        tcBaseDelegate.register(o);
    }
    
    @Override
    public boolean isActive() {
        return tcBaseDelegate.isActive();
    }
    
    @Override
    public void postSticky(Object o) {
        tcBaseDelegate.postSticky(o);
    }
    
    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        tcBaseDelegate.setSupportActionBar(this);
        
    }
    
    @Keep
    public void setSupportActionBar(@IdRes int toolBarID) {
        Toolbar toolbar = (Toolbar) findViewById(toolBarID);
        setSupportActionBar(toolbar);
//      tcBaseDelegate.setSupportActionBar(this);
    }
    
    //    @Override
//    public <T extends View> T findViewById(int id) {
//        return super.findViewById(id);
//    }
//
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
    
    public final <A extends Activity> void startActivity(@NonNull Class<A> target) {
        startActivity(new Intent(this, target));
//        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }
    
    public final <A extends Activity> void startActivity(@NonNull Class<A> target, int flags) {
        startActivity(new Intent(this, target).addFlags(flags));
//        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }
    
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }
    
    @Override
    public void finish() {
        super.finish();
    }
    
    @Override
    public void setStatusBarColor(@ColorRes int color) {
        tcBaseDelegate.setStatusBarColor(this, color);
    }
    
    @Override
    public void hideSoftKeyboard() {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
            .hideSoftInputFromWindow(findViewById(android.R.id.content).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0) {
            for(Fragment fragment : fragments) {
                if (fragment != null)
                    fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
    
    protected void tintHomeAsUp(@ColorRes int color) {
        tcBaseDelegate.tintHomeAsUp(this, color);
    }
    
    protected void tintHomeAsUpWithColor(@ColorInt int color) {
        tcBaseDelegate.tintHomeAsUpWithColor(this, color);
    }
    
    public void setHomeIndicator(@DrawableRes int homeIndicator) {
        tcBaseDelegate.setHomeIndicator(homeIndicator);
    }
}
