package com.base.delegate;


import android.app.Activity;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.base.R;
import com.base.app.BaseApplicationInterface;
import com.base.interfaces.BaseInterface;
import com.base.util.drawable.DrawableTintManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;


/**
 * Created by lz on 16-1-21.
 * 这里主要是统一实现Fragment和Activity的行为做的实现
 */
public class BaseDelegate {
    @NonNull
    public String tag = '/' + getClass().getSimpleName();
    
    public BaseApplicationInterface app;
    private ArrayList<Object> registerList = new ArrayList<>();
    private ArrayList<Object> postStickyList = new ArrayList<>();
    
    
    private boolean overWriteTitle = true;
    private int homeIndicator;
    
    public void setHomeIndicator(int homeIndicator) {
        this.homeIndicator = homeIndicator;
    }
    
    @Keep
    public final boolean isActive() {
        return isResumed;
    }
    
    volatile transient boolean isResumed = false;
    
    @Keep
    public void onCreate(BaseInterface baseInterface, Bundle savedInstanceState, BaseApplicationInterface baseApplicationInterface) {
        ActionBar title = baseInterface.getSupportActionBar();
        if (title != null) {
            title.setDisplayHomeAsUpEnabled(true);
            if (overWriteTitle)
                title.setTitle(baseInterface.getTitleText());
        }
        app = baseApplicationInterface;
        tag = baseInterface.getTitleText() + "/" + getClass().getSimpleName();
    }
    
    @Keep
    public void onResume() {
        isResumed = true;
    }
    
    @Keep
    public void onPause() {
        isResumed = false;
    }
    
    @Keep
    public void onOptionsItemSelected(MenuItem item, BaseInterface baseInterface) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (baseInterface instanceof Activity) {
                    ((Activity) baseInterface).onBackPressed();
                } else {
                    baseInterface.finish();
                }
                break;
            default:
                break;
        }
    }
    
    @Keep
    public void onDestroy(BaseInterface baseInterface) {
        //反注册已经注册的所有Object
        unregisterObject();
        removeStickyEvent();
        unRegisterThis(baseInterface);
        unBindView(baseInterface);
    }
    
    private void unBindView(BaseInterface baseInterface) {
        try {
            if (baseInterface.getViewDataBinds() != null) {
                baseInterface.getViewDataBinds()
                             .unbind();
            }
        } catch (Exception ignore) {
            
        }
    }
    
    private void unRegisterThis(BaseInterface baseInterface) {
        try {
            if (getEventBusDefault().isRegistered(baseInterface)) {
                getEventBusDefault().unregister(baseInterface);
            }
        } catch (Exception e) {
            
        }
    }
    
    private void removeStickyEvent() {
        //反注册已经注册的所有Object
        if (postStickyList != null && postStickyList.size() > 0) {
            for(int i = 0; i < postStickyList.size(); i++) {
                
                try {
                    getEventBusDefault().removeStickyEvent(postStickyList.get(i));
                } catch (Exception e) {
                    
                }
            }
            postStickyList.clear();
        }
    }
    
    private void unregisterObject() {
        if (registerList != null && registerList.size() > 0) {
            for(int i = 0; i < registerList.size(); i++) {
                getEventBusDefault().unregister(registerList.get(i));
            }
            registerList.clear();
        }
    }
    
    @Keep
    public EventBus getEventBusDefault() {
        return EventBus.getDefault();
    }
    
    /**
     * 将Object注册到全局EventBus
     *
     * @param o event subscriber
     */
    @Keep
    public void register(Object o) {
        if (registerList != null && !getEventBusDefault().isRegistered(o)) {
            registerList.add(o);
            getEventBusDefault().register(o);
        }
    }
    
    @Keep
    public void setSupportActionBar(BaseInterface baseInterface) {
        ActionBar title = baseInterface.getSupportActionBar();
        if (title != null) {
            title.setDisplayHomeAsUpEnabled(true);
            title.setTitle(baseInterface.getTitleText());
            if (homeIndicator != 0) {
                if (VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    title.setHomeAsUpIndicator(ActivityCompat.getDrawable(baseInterface.getApplication(), homeIndicator));
                } else {
                    title.setHomeAsUpIndicator(homeIndicator);
                }
            }
        }
    }
    
    /**
     * @param overWriteTitle
     */
    public void setOverWriteTitle(boolean overWriteTitle) {
        this.overWriteTitle = overWriteTitle;
    }
    
    /**
     * @param o
     */
    public void postSticky(Object o) {
        postStickyList.add(o);
        getEventBusDefault().postSticky(o);
    }
    
    public void tintHomeAsUp(@NonNull BaseInterface baseInterface, @ColorRes int colorID) {
        try {
            if (homeIndicator != 0) {
                int color = ContextCompat.getColor(baseInterface.getApplication(), colorID);
                baseInterface.getSupportActionBar()
                             .setHomeAsUpIndicator(DrawableTintManager.tintColor(ContextCompat.getDrawable(baseInterface.getApplication(), homeIndicator), color));
                
            } else {
                int color = ContextCompat.getColor(baseInterface.getApplication(), colorID);
                baseInterface.getSupportActionBar()
                             .setHomeAsUpIndicator(DrawableTintManager.tintColor(ContextCompat.getDrawable(baseInterface.getApplication(), R.drawable.ic_arrow_back_black_24dp),
                                                                                 color));
            }
        } catch (Exception ignored) {
            
        }
    }
    
    public void tintHomeAsUpWithColor(@NonNull BaseInterface baseInterface, @ColorInt int color) {
        try {
            if (homeIndicator != 0) {
           
                baseInterface.getSupportActionBar()
                             .setHomeAsUpIndicator(DrawableTintManager.tintColor(ContextCompat.getDrawable(baseInterface.getApplication(), homeIndicator), color));
                
            } else {
                baseInterface.getSupportActionBar()
                             .setHomeAsUpIndicator(DrawableTintManager.tintColor(ContextCompat.getDrawable(baseInterface.getApplication(), R.drawable.ic_arrow_back_black_24dp),
                                                                                 color));
            }
        } catch (Exception ignored) {
            
        }
    }
    
    /**
     * 设置颜色
     *
     * @param activity
     * @param color
     */
    public void setStatusBarColor(@NonNull Activity activity, @ColorRes int color) {
        if (VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow()
                    .setStatusBarColor(activity.getResources()
                                               .getColor(color));
        }
    }
}
