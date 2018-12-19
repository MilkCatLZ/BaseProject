package com.base.network.xutils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;


/**
 * Created by LZ on 2016/8/19.
 */
public class NetworkState {

    /**
     * 是否连接wifi
     * @param context
     * @return
     */
    public static boolean isWifi(@NonNull Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiNetworkInfo.isConnected()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return true;
        }
    }
}
