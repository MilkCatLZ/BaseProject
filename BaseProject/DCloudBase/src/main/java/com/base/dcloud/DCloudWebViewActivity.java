package com.base.dcloud;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;

import com.base.dcloud.CoreStatusListener.OnInitEndListener;

import io.dcloud.common.DHInterface.ICore;
import io.dcloud.common.DHInterface.ISysEventListener.SysEventType;


/**
 * Created by LZ on 2017/5/10.
 * DCloudWebViewActivity的基类
 */
public class DCloudWebViewActivity extends AppCompatActivity implements OnInitEndListener {
    
    DCloudManager manager=null;
    private boolean canStart;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager= DCloudManager.createInstance(this,this);
    }
    
    @Override
    public void onInitEnd(ICore iCore) {
        canStart = true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return manager.mEntryProxy.onActivityExecute(this, SysEventType.onCreateOptionMenu, menu);
    }

    @Override
    public void onPause() {
        super.onPause();
        manager.mEntryProxy.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        manager.mEntryProxy.onResume(this);
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getFlags() != 0x10600000) {
            // 非点击icon调用activity时才调用newintent事件
            manager.mEntryProxy.onNewIntent(this, intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.mEntryProxy.onStop(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean _ret = manager.mEntryProxy.onActivityExecute(this, SysEventType.onKeyDown, new Object[]{keyCode, event});
        return _ret ? _ret : super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean _ret = manager.mEntryProxy.onActivityExecute(this, SysEventType.onKeyUp, new Object[]{keyCode, event});
        return _ret ? _ret : super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        boolean _ret = manager.mEntryProxy.onActivityExecute(this, SysEventType.onKeyLongPress, new Object[]{keyCode, event});
        return _ret ? _ret : super.onKeyLongPress(keyCode, event);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        try {
            int temp = this.getResources().getConfiguration().orientation;
            if (manager.mEntryProxy != null) {
                manager.mEntryProxy.onConfigurationChanged(this, temp);
            }
            super.onConfigurationChanged(newConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        manager.mEntryProxy.onActivityExecute(this, SysEventType.onActivityResult, new Object[]{requestCode, resultCode, data});
    }
}
