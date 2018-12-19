package com.base.util;


import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


/**
 * Created by Syokora on 2016/8/20.
 */
public class JsonManager {
    
    /**
     * @param key
     *
     * @return
     */
    @NonNull
    public static String getJsonString(@NonNull String resource, @NonNull String key) {
        JSONObject jsonObject;
        try {
            jsonObject = JSON.parseObject(resource);
        } catch (Exception e) {
            Log.e("getJsonString",resource +"不是一个JsonString!!!!!!!!!!!!!!!!!!!!!!");
            return "";
        }
        try {
            return jsonObject.get(key).toString();
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * @param resource
     * @param key
     * @param index
     *
     * @return
     */
    public static String getJsonArrayString(@NonNull String resource, @NonNull String key, int index) {
        JSONObject jsonObject = JSON.parseObject(resource);
        try {
            return jsonObject.getJSONArray(key).get(index).toString();
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * @param key
     *
     * @return
     */
    @NonNull
    public static int getJsonInt(@NonNull String resource, @NonNull String key) {
        JSONObject jsonObject = JSON.parseObject(resource);
        try {
            return jsonObject.getInteger(key);
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * @param key
     *
     * @return
     */
    @NonNull
    public static double getJsonDouble(@NonNull String resource, @NonNull String key) {
        JSONObject jsonObject = JSON.parseObject(resource);
        try {
            return jsonObject.getDouble(key);
        } catch (Exception e) {
            return 0;
        }
    }
}
