package com.base.network.xutils;


import android.support.annotation.Nullable;

import org.xutils.common.Callback;
import org.xutils.common.Callback.CommonCallback;

import java.io.File;


/**
 * Created by LZ on 2016/8/26.
 */
class XUtilsFileCallBack implements Callback.ProgressCallback<File> {
    @Nullable
    ProgressCallback<File> callback;

    public XUtilsFileCallBack(@Nullable ProgressCallback callbac) {
        this.callback = callbac;
    }

    @Override
    public void onSuccess(File result) {
        if (callback != null) {
            callback.onSuccess(result);
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        if (callback != null) {
            callback.onError(ex, isOnCallback);
        }
    }

    @Override
    public void onCancelled(CancelledException cex) {
        if (callback != null) {
            callback.onCancelled(cex);
        }
    }

    @Override
    public void onFinished() {
        if (callback != null) {
            callback.onFinished();
        }
    }

    @Override
    public void onWaiting() {

    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onLoading(long total, long current, boolean isDownloading) {
        if (callback != null) {
            callback.onLoading(total, current, isDownloading);
        }
    }
}