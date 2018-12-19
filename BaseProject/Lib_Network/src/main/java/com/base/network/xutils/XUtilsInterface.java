package com.base.network.xutils;


import org.xutils.common.Callback.CancelledException;
import org.xutils.http.RequestParams;


/**
 * Created by LZ on 2016/8/19.
 */
public interface XUtilsInterface {
    void beforeGetStart(RequestParams entity);
    void beforePostStart(RequestParams entity);

    void onSuccess(String result);
    void onFinished();
    void onCancelled(CancelledException cex);
    void onError(Throwable ex);
}
