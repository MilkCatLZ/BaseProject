package com.base.dcloud;


import io.dcloud.common.DHInterface.ICore;
import io.dcloud.feature.internal.sdk.SDK;


/**
 * Created by LZ on 2017/3/27.
 * DCloud初始化监听
 */
public class CoreStatusListener implements ICore.ICoreStatusListener {
    OnInitEndListener listener;
    public interface OnInitEndListener{
        void onInitEnd(ICore iCore);
    }
    
    public void setListener(OnInitEndListener listener) {
        this.listener = listener;
    }
    
    @Override
    public void onCoreReady(ICore iCore) {
        // 调用SDK的初始化接口，初始化5+ SDK
        SDK.initSDK(iCore);
        // 设置当前应用可使用的5+ API
        SDK.requestAllFeature();
    }
    
    @Override
    public void onCoreInitEnd(ICore iCore) {
        if (listener != null) {
            listener.onInitEnd(iCore);
        }
        
        
    }
    
    @Override
    public boolean onCoreStop() {
        return false;
    }
}
