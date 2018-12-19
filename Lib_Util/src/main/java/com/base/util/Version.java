package com.base.util;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;


/**
 * Created by LZ on 2016/8/16.
 */
public class Version {

    public static final String OldVersion = "Version:old";
    public static final String NewVersion = "Version:new";

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    @NonNull
    public static String getVersion(@NonNull Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    @NonNull
    public static String getNewVersion(@NonNull Context context) {
        return SPCache.getObject(context, NewVersion, String.class, "");
    }

    /**
     * 是否是第一次安装
     *
     * @param context
     *
     * @return
     */
    public static boolean isFirstInstall(Context context) {
        if (StringUtils.isEmpty(SPCache.getObject(context, OldVersion, String.class, ""))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 改变安装状态
     *
     * @param context
     */
    public static void firstInstallComplete(Context context) {
        SPCache.saveObject(context, OldVersion, getVersion(context));
    }
}
