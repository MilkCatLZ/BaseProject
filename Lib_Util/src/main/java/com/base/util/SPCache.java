package com.base.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;

import java.util.List;


/**
 * @author lz
 *         SharedPreferences辅助类，让读写缓存变得更简单一些
 *         以前写好的一个类暂时先放上来
 */
public class SPCache {
    public static final String SETTINGS = "com.lianni.delivery";
    private static final String tag = "SPCache";
    
    /**
     * 缓存信息
     *
     * @param context   上下文
     * @param cacheName 存入名字
     * @param obj       对象
     */
    public static void saveObject(@Nullable Context context, @NonNull String cacheName, @Nullable
        Object obj) {
        if (context == null) return;
        Editor e = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).edit();
        String json = JSON.toJSONString(obj);
        e.putString(cacheName, json);
        e.commit();
    }
    
    /**
     * 读取缓存信息
     *
     * @param <T>
     * @param context   上下文
     * @param cacheName 缓存名
     * @param classes   对象类型
     */
    @Nullable
    public static <T> T getObject(Context context, String cacheName, Class classes) {
        return getObject(context, cacheName, classes, null);
    }
    
    /**
     * 读取缓存信息
     *
     * @param <T>
     * @param context      上下文
     * @param cacheName    缓存名
     * @param classes      对象类型
     * @param defaultValue 默认参数
     */
    @NonNull
    public static <T> T getObject(@Nullable Context context, @NonNull String cacheName, @NonNull
        Class classes, T defaultValue) {
        if (context == null) return defaultValue;
        SharedPreferences s = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        String jsonString = s.getString(cacheName, "");
        if (!"".equals(jsonString)) {
//            T o = (T) new Gson().fromJson(jsonString, classes);
            T o = null;
            try {
                o = (T) JSON.parseObject(jsonString, classes);
                if (o == null) {
                    return defaultValue;
                }
                return o;
            } catch (Exception e) {
                Log.e(tag, "parse Object failed", e);
            }
        }
        return defaultValue;
    }

//    /**
//     * 读取缓存信息
//     * 读取List 等列表要这么写 new TypeToken<ArrayList<GoodsCategory>>(){}.getType()
//     *
//     * @param <T>
//     * @param context   上下文
//     * @param cacheName 缓存名
//     * @param type      对象类型
//     */
//    public static <T> T getObject(Context context, String cacheName, Type type) {
//        return getObject(context, cacheName, type, null);
//    }

//    /**
//     * 读取缓存信息
//     *
//     * @param <T>
//     * @param context      上下文
//     * @param cacheName    缓存名
//     * @param type         对象类型
//     * @param defaultValue 默认参数
//     */
//    public static <T> T getObject(Context context, String cacheName, Type type, T defaultValue) {
//        SharedPreferences s = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
//        String jsonString = s.getString(cacheName, "");
//        if (!"".equals(jsonString)) {
//            T o = JSON.parseObject(jsonString, type, Feature.IgnoreNotMatch);
//            return o;
//        }
//        return defaultValue;
//    }
    
    /**
     * @param context
     * @param cacheName
     * @param cls
     * @param <T>
     *
     * @return
     */
    @Nullable
    public static <T> List<T> getObjectList(@NonNull Context context, @NonNull String cacheName,
                                            @NonNull Class cls) {
        SharedPreferences s = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        String jsonString = s.getString(cacheName, "");
        if (!"".equals(jsonString)) {
            List<T> o = null;
            try {
                o = JSON.parseArray(jsonString, cls);
            } catch (Exception e) {
                Log.e(tag, "parse Object list failed", e);
            }
            return o;
        }
        return null;
    }
    
    
    /**
     * 删除信息
     *
     * @param context   上下文
     * @param cacheName 缓存名
     */
    public static void delObject(@NonNull Context context, @NonNull String cacheName) {
        SharedPreferences s = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        s.edit().remove(cacheName).apply();
    }
    
}
