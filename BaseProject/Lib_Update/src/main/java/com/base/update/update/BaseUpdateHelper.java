package com.base.update.update;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.webkit.MimeTypeMap;

import com.base.network.xutils.NetworkState;
import com.base.network.xutils.XUtils;
import com.base.update.InstallActivity;
import com.base.update.R;
import com.base.update.model.BaseUpdate;
import com.base.util.DialogManager;
import com.base.util.FileManager;
import com.base.util.Log;
import com.base.util.SPCache;
import com.base.util.StringUtils;
import com.base.util.ToastManager;
import com.base.util.Version;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.common.Callback.ProgressCallback;
import org.xutils.http.RequestParams;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by LZ on 2016/8/16.
 * 取消
 * -------------------------------------------
 * 1.在gradle.properties中添加：
 * CANCEL_ACTIVITY = CancelActivity  //对应的是本类中的{@link BaseUpdateHelper#CANCEL_ACTIVITY}，名字要相同
 * CANCEL_ACTIVITY_ACTION = "update.cancelActivity"
 * CANCEL_ACTIVITY_ACTION_MANIFEST = update.cancelActivity
 * 2.在build.gradle的defaultConfig中添加
 * buildConfigField "String", CANCEL_ACTIVITY, CANCEL_ACTIVITY_ACTION
 * 3.在主项目的AndroidManifest.xml配置取消下载的Activity（activity可以自己建立并做相应操作，但是下面的<intent-filter>要一致）:
 * <activity
 * android:name="com.lianni.app.CancelActivity"
 * android:exported="false">
 * <intent-filter>
 * <action android:name="${CancelAction}" />
 * <category android:name="android.intent.category.DEFAULT" />
 * </intent-filter>
 * </activity>
 * 4.配置placeHolder:
 * CancelAction      : CANCEL_ACTIVITY_ACTION_MANIFEST,
 */
public abstract class BaseUpdateHelper<Update extends BaseUpdate> {

    private final OnCheckCompleteListener listener;
    private String ChannelID = "com.lianni.delivery";
    private String ChannelName = "连你配送";


    /**
     * 检查更新完成后，调用这个回调
     */
    public interface OnCheckCompleteListener<Update extends BaseUpdate> {
        void onCheckComplete(Update info);

        void onCheckError(Throwable ex);
    }


    public static final String APK_PATH = "apk_path";
    private static final String APK_SIZE = "apk_length";
    public static final String CANCEL_ACTIVITY = "CancelActivity";
    /**
     * 取消下载时，用的action，请在manifest中定义对应action
     */
    public final String CANCEL_ACTION;
    public static final String HAS_NEW_VERSION = "BaseUpdateHelper:hasNewVersion";

    public final int NOTIFICATION_DOWNLOAD_ID = 32;
    private final String url;
    /**
     * 弹出框确认
     */
    private final boolean isAutoUpdate;
    /**
     * 已经是最新版提示
     */
    private boolean needMessage;
    /**
     * 每秒刷新一次进度用的
     */
    private boolean canChangeProgress = true;

    private boolean silenceDownLoad;

    public static final int FORCE_UPDATE = 1;
    final String compulsion = "BaseUpdateHelper:compulsion";
    private String nowVersion;
    private String nowString;
    protected Context context;


    private Update info;
    private Update todayInfo;


    private Callback.Cancelable downLoadCancelable;

    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder = null;
    private int logo;

    /**
     * 这里配置的sting会被主项目的覆盖，所以这里的不算数。
     * 需要在主项目的string.xml中配置：
     * <string name="pack_build_config_name">真正的包名.BuildConfig</string>
     * 例:
     * <string name="pack_build_config_name">com.lianni.mall.BuildConfig</string>
     *
     * @param context
     * @param fieldName
     * @return
     */
    private static Object getBuildConfigValue(Context context, String fieldName) {
        try {
            Class<?> clazz = Class.forName(context.getResources().getString(R.string.pack_build_config_name));
            Field field = clazz.getField(fieldName);
            return field.get(null);
        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
        } catch (IllegalAccessException e) {
//            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param context
     * @param url          检查更新的完整地址
     * @param logo         默认logo
     * @param isAutoUpdate 是否是自动更新
     * @param needMessage  是否需要显示更新信息
     */
    public BaseUpdateHelper(@NonNull Context context, String url, int logo, boolean isAutoUpdate, boolean needMessage, OnCheckCompleteListener listener) {
        this.context = context;
        EventBus.getDefault()
                .register(this);
        notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(ChannelID, ChannelName, NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(false);
            channel.setSound(null, null);
            notificationManager.createNotificationChannel(channel);
        }
        nowString = new SimpleDateFormat("yyyy-MM-dd", Locale.US)
                .format(Calendar.getInstance()
                        .getTime());
        todayInfo = getTodayInfo(nowString);
//        todayInfo = SPCache.getObject(context, nowString, UpdateInfo.class);
        nowVersion = Version.getVersion(context);
        this.needMessage = needMessage;
        this.isAutoUpdate = isAutoUpdate;
        this.url = url;
        this.logo = logo;
        CANCEL_ACTION = (String) getBuildConfigValue(context, CANCEL_ACTIVITY);
        this.listener = listener;
        Log.d(compulsion, nowString);
    }

    /**
     * @param context
     * @param url          检查更新的完整地址
     * @param logo         默认logo
     * @param isAutoUpdate 是否是自动更新
     * @param needMessage  是否需要显示更新信息
     */
    public BaseUpdateHelper(@NonNull Context context, String url, int logo, boolean isAutoUpdate, boolean needMessage) {
        this(context, url, logo, isAutoUpdate, needMessage, null);
    }

    /**
     * @param result 原始json字符串
     * @return 返回最新的版本信息
     */
    protected abstract Update getNewUpdateInfo(String result);

    /**
     * @param nowString 日期，格式为"yyyy-MM-dd"
     * @return 当天的自动更新信息, 如果当天第一次打开，则会返回null
     */
    protected abstract Update getTodayInfo(String nowString);

    public void checkUpdate() {
        RequestParams params = new RequestParams(url);
//        params.addParameter("b", "b");
//        params.addParameter("c", "c");
//        params.addParameter("d", "d");
//        params.addParameter("a", "a");
        Callback.Cancelable cancelable = XUtils.get(params, checkUpdateCallback);
    }


    /**
     * 开始下载
     */
    public void startDownLoad() {
        if (info != null) {
            if (!StringUtils.isEmpty(info.getDownloadUrl()) && info.getDownloadUrl()
                    .contains("apk")) {

//
//                DownloadManager downloadManager = (DownloadManager)context.getSystemService(DOWNLOAD_SERVICE);
//                DownloadManager.Request request = new Request(Uri.parse(info.getDownloadUrl()));
//                request.setDestinationInExternalPublicDir("Lianni",getAppName()+info.getVersion());
//
//                request.allowScanningByMediaScanner();//表示允许MediaScanner扫描到这个文件，默认不允许。
//                request.setTitle("连你配送");//设置下载中通知栏提示的标题
//                request.setDescription("连你配送版本更新");//设置下载中通知栏提示的介绍
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//
//                long downloadId = downloadManager.enqueue(request);

                RequestParams params = new RequestParams(info.getDownloadUrl());
                params.setSaveFilePath(FileManager.getDirectory(context) + FileManager
                        .convertUrlToFileName(info.getDownloadUrl()));
                downLoadCancelable = XUtils.downLoad(params, progressCallback);
            }
            //用来取消
            EventBus.getDefault()
                    .postSticky(this);
        }

    }

    /**
     * 取消：在取消的Activity中，使用如下代码：
     *
     * @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
     * public void cancelUpdate(BaseUpdateHelper updateHelper) {
     * if (updateHelper != null) {
     * updateHelper.cancelDownload();
     * }
     * finish();
     * }
     */
    public void cancelDownload() {
        if (downLoadCancelable != null) {
            downLoadCancelable.cancel();
        }
    }

    /**
     * 检查更新回调
     */
    public CommonCallback<String> checkUpdateCallback = new CommonCallback<String>() {
        @Override
        public void onSuccess(String result) {
//            info = JSONObject.parseObject(result, Update.class);
            info = getNewUpdateInfo(result);
            boolean forceUpdate = info.getCompulsion() == FORCE_UPDATE && ((nowVersion.compareTo(
                    info.getVersion()) < 0));
            //强制更新,isAutoUpdate=true就是自动，否则就是手动检查更新
            if (forceUpdate /*&& isAutoUpdate*/) {
                if (nowVersion.compareTo(info.getVersion()) < 0) {
//                    if (NetworkState.isWifi(context)) {
//                        silenceDownLoad = true;
                    startDownLoad();
//                    }
                    SPCache.saveObject(context, HAS_NEW_VERSION, true);
                    SPCache.saveObject(context, Version.NewVersion, info.getVersion());
                } else {
                    SPCache.saveObject(context, HAS_NEW_VERSION, false);
                }
            } else {
                //非强制更新
                boolean todayIsChecked = checkToday();
                SPCache.saveObject(context, nowString, info);
                todayInfo = info;
                if (todayIsChecked) {
                    if (checkHasNewVersion(nowVersion, info.getVersion())/*(nowVersion.compareTo(info.getVersion()) < 0)*/) {
                        if (needMessage()) {
                            showUpdateConfirmDialog();
                        }
                        SPCache.saveObject(context, HAS_NEW_VERSION, true);
                        SPCache.saveObject(context, Version.NewVersion, info.getVersion());
                    } else {
                        if (needMessage()) {
                            ToastManager.showShortToast(context, getAlreadyNewMessage());
                        }
                        SPCache.saveObject(context, HAS_NEW_VERSION, false);
                    }
                } else {
                    if ((nowVersion.compareTo(info.getVersion()) < 0)) {
                        showUpdateConfirmDialog();
                        SPCache.saveObject(context, HAS_NEW_VERSION, true);
                        SPCache.saveObject(context, Version.NewVersion, info.getVersion());
                    } else {
                        if (needMessage()) {
                            ToastManager.showShortToast(context, getAlreadyNewMessage());
                        }
                        SPCache.saveObject(context, HAS_NEW_VERSION, false);
                    }
                }
            }
            if (listener != null) {
                listener.onCheckComplete(info);
            }
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            if (listener != null) {
                listener.onCheckError(ex);
            }
        }

        @Override
        public void onCancelled(CancelledException cex) {

        }

        @Override
        public void onFinished() {

        }
    };

    /**
     * @param nowVersion
     * @param newVersion
     * @return true:有新版本，false：没有新版本
     */
    protected abstract boolean checkHasNewVersion(String nowVersion, String newVersion);

    /**
     * @return 当前已是最新版本
     */
    protected abstract String getAlreadyNewMessage();

    private boolean needMessage() {
        return !isAutoUpdate && needMessage;
    }

    /**
     * 弹出提示窗口
     */
    private void showUpdateConfirmDialog() {
        if (context instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) context;
            DialogManager.with(appCompatActivity, appCompatActivity.getSupportFragmentManager())
                    .title(R.string.str_find_new_version)
                    .message(info.getContent())
                    .rightButtonText(R.string.str_update)
                    .onRightClick(new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startDownLoad();
                            ToastManager.showShortToast(context, context
                                    .getString(R.string.str_start_update));
                        }
                    })
                    .show();
        }
    }

    /**
     * 检查今天是否已经检查过更新
     *
     * @return true:已经检查过，并且没有新版本，不需要再提示
     * false:需要再次提示更新
     */
    private boolean checkToday() {
        if (todayInfo == null) {
            return false;
        }
        if (info != null) {
            //今天检查过了，而且新的版本和今天的相同
            if (todayInfo.getVersion()
                    .equals(info.getVersion())) {
                return true;
            } else {
                //今天检查过，但是新版本比今天检查过的跟更新，所以要重新提示
                if (todayInfo.getVersion()
                        .compareTo(info.getVersion()) < 0) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 刷新通知
     */
    private ProgressCallback<File> progressCallback = new ProgressCallback<File>() {
        @Override
        public void onSuccess(File result) {
            canChangeProgress = true;
            SPCache.saveObject(context, APK_PATH, result.getAbsolutePath());
            SPCache.saveObject(context, APK_SIZE, result.length());
            startInstall(result);
            EventBus.getDefault()
                    .removeStickyEvent(BaseUpdateHelper.class);

            Intent installIntent = new Intent(context, InstallActivity.class);
            installIntent.putExtra(InstallActivity.APK_URL, result.getAbsolutePath());
            installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, installIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            notifyChange(builder
                    .setContentIntent(pendingIntent)
                    .setContentInfo(context.getString(R.string.str_down_load_success))
                    .setTicker(context.getString(R.string.str_down_load_success))
                    .setProgress(0, 0, false)
                    .setAutoCancel(true)
                    .setChannelId(ChannelID)
                    .setOngoing(false));
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            canChangeProgress = true;
            notifyChange(builder
                    .setContentInfo(context.getString(R.string.str_down_load_failed))
                    .setTicker(context.getString(R.string.str_down_load_failed))
                    .setProgress(0, 0, false)
                    .setAutoCancel(true)
                    .setChannelId(ChannelID)
                    .setOngoing(false));
            EventBus.getDefault()
                    .removeStickyEvent(BaseUpdateHelper.class);
        }

        @Override
        public void onCancelled(CancelledException cex) {
            canChangeProgress = true;
            notifyChange(builder
                    .setContentInfo(context.getString(R.string.str_down_load_cancel))
                    .setTicker(context.getString(R.string.str_down_load_cancel))
                    .setProgress(0, 0, false)
                    .setAutoCancel(true)
                    .setChannelId(ChannelID)
                    .setOngoing(false));
            EventBus.getDefault()
                    .removeStickyEvent(BaseUpdateHelper.class);
        }

        @Override
        public void onFinished() {
            canChangeProgress = true;
        }

        @Override
        public void onWaiting() {

        }

        @Override
        public void onStarted() {
            updateProgress(0, 0);
        }

        @Override
        public void onLoading(long total, long current, boolean isDownloading) {
            if (canChangeProgress)
                updateProgress(total, current);
        }

        private void updateProgress(long total, long current) {
            EventBus.getDefault()
                    .post(new Progress(total, current));
        }

    };

    /**
     * 安装
     *
     * @param result
     */
    public void startInstall(File result) {
        openFile(result, context);
    }

    static class Progress {
        long total;
        long current;

        Progress(long total, long current) {
            this.total = total;
            this.current = current;
        }
    }


    /**
     * 更新进度
     *
     * @param progress
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateDownLoadProgress(Progress progress) {
        canChangeProgress = false;

        Log.d("updateProgress", "total=" + progress.total + "/current=" + progress.current);

        int pro = (int) (progress.total == 0 ? 0 : progress.current * 100 / progress.total);

        if (builder == null) {
            builder = new NotificationCompat.Builder(context, ChannelID);
            builder.setOnlyAlertOnce(true);
        }


        //Intent
        Intent intent = new Intent(CANCEL_ACTION);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //PendingIntent
        PendingIntent pendingIntent = PendingIntent
                .getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent)
                .setContentTitle(getAppName() + info.getVersion())
                .setTicker("开始下载")
                .setSmallIcon(logo)
                .setContentInfo("正在准备下载")
                .setAutoCancel(true)
                .setOngoing(true)
                .setChannelId(ChannelID)
                .setProgress(100, pro, false);

        notifyChange(builder);
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
                canChangeProgress = true;
            }
        }.start();
    }

    protected abstract String getAppName();

    private void notifyChange(NotificationCompat.Builder builder) {
        //后台静默下载
        if (!silenceDownLoad)
            notificationManager
                    .notify(getClass().getSimpleName(), NOTIFICATION_DOWNLOAD_ID, builder.build());
    }

    /**
     * 是否有新版
     *
     * @param context
     * @return
     */
    public static boolean hasNewVersion(Context context) {
        return SPCache.getObject(context, HAS_NEW_VERSION, Boolean.class, false);
    }

    /**
     * 是否已经下载好新版
     *
     * @param context
     * @return
     */
    public static boolean hasNewVersionApk(Context context) {
        if (hasNewVersion(context)) {
            File file = new File(SPCache.getObject(context, APK_PATH, String.class, ""));
            long length = Long.valueOf(SPCache.getObject(context, APK_SIZE, Long.class, 0l));
            if (file.exists() && length == file.length()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public static void openFile(File var0, Context var1) {
        Intent var2 = new Intent();
        var2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        var2.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri uriForFile = FileProvider.getUriForFile(var1, var1.getApplicationContext().getPackageName() + ".provider", var0);
            var2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            var2.setDataAndType(uriForFile, var1.getContentResolver().getType(uriForFile));
        } else {
            var2.setDataAndType(Uri.fromFile(var0), getMIMEType(var0));
        }
        try {
            var1.startActivity(var2);
        } catch (Exception var5) {
            var5.printStackTrace();
        }
    }

    public static String getMIMEType(File var0) {
        String var1 = "";
        String var2 = var0.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }
}
