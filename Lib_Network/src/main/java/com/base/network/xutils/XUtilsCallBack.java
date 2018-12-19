package com.base.network.xutils;


import android.support.annotation.Nullable;

import org.xutils.common.Callback.CommonCallback;


/**
 * Created by LZ on 2016/8/26.
 *
 */
class XUtilsCallBack implements CommonCallback<String> {
    private final XUtilsInterface xutilsInterface;
    @Nullable
    CommonCallback<String> callback;

    public XUtilsCallBack(@Nullable CommonCallback callback,@Nullable XUtilsInterface xutilsInterface) {
        this.callback = callback;
        this.xutilsInterface=xutilsInterface;
    }

    @Override
    public void onSuccess(String result) {
        if (callback != null) {
            callback.onSuccess(result);
        }
        if (xutilsInterface != null) {
            xutilsInterface.onSuccess(result);
        }
//        Log.d("XUtils+onSuccess", result);
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        if (callback != null) {
            callback.onError(ex, isOnCallback);
        }
        if (xutilsInterface != null) {
            xutilsInterface.onError(ex);
        }
//        Log.d("XUtils+onError", ex.toString());
    }

    @Override
    public void onCancelled(CancelledException cex) {
        if (callback != null) {
            callback.onCancelled(cex);
        }
        if (xutilsInterface != null) {
            xutilsInterface.onCancelled(cex);
        }
//        Log.d("XUtils+onError", cex.getCause().toString());
    }

    @Override
    public void onFinished() {
        if (callback != null) {
            callback.onFinished();
        }

        if (xutilsInterface != null) {
            xutilsInterface.onFinished();
        }
//        Log.d("XUtils+onFinished", "");
    }

}