package com.base.network.xutils;


import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.base.network.BuildConfig;
import com.base.util.Log;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;


/**
 * Created by LZ on 2016/8/17.
 * 网络方案
 */
public class XUtils {

    public static final int CONNECT_TIMEOUT = 60000;
    public static final int MAX_RETRY_COUNT = 1;
    private static XUtilsInterface xutilsInterface = null;
    static Application application;


    /**
     * @param context
     */
    public static void init(Application context, XUtilsInterface xutilsInterface) {
        x.Ext.init(context);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
        application = (Application) context;
        XUtils.xutilsInterface = xutilsInterface;
    }

    /**
     * @param entity
     * @param callback
     *
     * @return
     */
    @NonNull
    public static Callback.Cancelable get(@NonNull RequestParams entity, @NonNull final Callback.CommonCallback<String> callback) {
        if (xutilsInterface != null) {
            xutilsInterface.beforeGetStart(entity);
        }
        Log.d("XUtils/get-------URL", entity.toString());
        entity.setUseCookie(true);
        entity.setMaxRetryCount(MAX_RETRY_COUNT);
        entity.setConnectTimeout(CONNECT_TIMEOUT);
        return x.http()
                .get(entity, new XUtilsCallBack(callback, xutilsInterface));
    }
    /**
     * @param entity
     * @param callback
     *
     * @return
     */
    @NonNull
    public static Callback.Cancelable downLoad(@NonNull RequestParams entity, @NonNull final Callback.ProgressCallback<File> callback) {
//        if (xutilsInterface != null) {
//            xutilsInterface.beforeGetStart(entity);
//        }
        Log.d("XUtils/get-------URL", entity.toString());
        entity.setUseCookie(true);
        entity.setMaxRetryCount(MAX_RETRY_COUNT);
        entity.setConnectTimeout(CONNECT_TIMEOUT);
        return x.http()
                .get(entity,callback);
    }


    /**
     * 不做共通回调的get调用
     *
     * @param entity
     * @param callback
     *
     * @return
     */
    @NonNull
    public static <T> Callback.Cancelable get(@NonNull RequestParams entity, @NonNull final Callback.CommonCallback<T> callback, String nulls) {
        if (xutilsInterface != null) {
            xutilsInterface.beforeGetStart(entity);
        }
        Log.d("XUtils/get-------URL", entity.toString());
        entity.setUseCookie(true);
        entity.setMaxRetryCount(MAX_RETRY_COUNT);
        entity.setConnectTimeout(CONNECT_TIMEOUT);
        return x.http()
                .get(entity, callback);
    }


    /**
     * @param entity
     * @param callback
     *
     * @return
     */
    @NonNull
    public static Callback.Cancelable post(@NonNull RequestParams entity, @Nullable final Callback.CommonCallback<String> callback) {
        if (xutilsInterface != null) {
            xutilsInterface.beforePostStart(entity);
        }
        Log.d("XUtils/post------URL", entity.toString());
        entity.setUseCookie(true);
        entity.setMaxRetryCount(MAX_RETRY_COUNT);
        entity.setConnectTimeout(CONNECT_TIMEOUT);
        return x.http()
                .post(entity, new XUtilsCallBack(callback, xutilsInterface));
    }

    /**
     * @param entity
     * @param callback
     * @param <T>
     *
     * @return
     */
    @NonNull
    public static <T> Callback.Cancelable delete(@NonNull RequestParams entity, @NonNull Callback.CommonCallback<T> callback) {
        if (xutilsInterface != null) {
            xutilsInterface.beforeGetStart(entity);
        }
        Log.d("XUtils/delete----URL", entity.toString());
        entity.setMaxRetryCount(MAX_RETRY_COUNT);
        entity.setConnectTimeout(CONNECT_TIMEOUT);
        entity.setUseCookie(true);
        return x.http()
                .request(HttpMethod.DELETE, entity, callback);
    }

    /**
     * @param entity
     * @param callback
     *
     * @return
     */
    @NonNull
    public static Callback.Cancelable put(@NonNull RequestParams entity, @NonNull final Callback.CommonCallback<String> callback) {
        if (xutilsInterface != null) {
            xutilsInterface.beforeGetStart(entity);
        }
        Log.d("XUtils/put-------URL", entity.toString());
        entity.setMaxRetryCount(MAX_RETRY_COUNT);
        entity.setConnectTimeout(CONNECT_TIMEOUT);
        entity.setUseCookie(true);
        return x.http()
                .request(HttpMethod.PUT, entity, new XUtilsCallBack(callback, xutilsInterface));
    }

}
