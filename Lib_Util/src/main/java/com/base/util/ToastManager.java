package com.base.util;


import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;


public class ToastManager {
    static ToastManager manager=new ToastManager();

    public static final String MSG_ERROR = "系统错误,请稍候重试";

    private Toast mToast;
    private static Handler mhandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            manager.mToast.cancel();
        }
    };

    public static void setCanShowMsg(boolean canShowMsg) {
        ToastManager.canShowMsg = canShowMsg;
    }

    private static boolean canShowMsg = true;

    /**
     * @param context
     * @param text
     * @param duration
     */
    public static Toast showToast(Context context, String text, int duration) {
        if (context == null) {
            return manager.mToast;
        }
        mhandler.removeCallbacks(r);
        if (null != manager.mToast) {
            manager.mToast.setText(text);
        } else {
            manager.mToast = Toast.makeText(context, text, duration);
        }
        mhandler.postDelayed(r, 5000);
        if (canShowMsg)
            manager.mToast.show();
        return manager.mToast;
    }

    /**
     * @param context
     * @param text
     */
    public static Toast showShortToast(Context context, String text) {
        return showToast(context, text, Toast.LENGTH_SHORT);
    }

    /**
     * @param context
     * @param text
     */
    public static Toast showLongToast(Context context, String text) {
        return showToast(context, text, Toast.LENGTH_LONG);
    }

    /**
     * @param context
     * @param text
     */
    public static Toast showShortToast(Context context, @StringRes int text) {
        return showToast(context, context.getString(text), Toast.LENGTH_SHORT);
    }

    /**
     * @param context
     * @param text
     */
    public static Toast showLongToast(@NonNull Context context, @StringRes int text) {
        return showToast(context, context.getString(text), Toast.LENGTH_LONG);
    }

    public static Toast showToast(Context context, int strId, int duration) {
        return showToast(context, context.getString(strId), duration);
    }

    /**
     * 重新启用，setCanShowMsg
     */
    public static void hideMsg() {
        if (null != manager.mToast) {
            manager.mToast.cancel();
            canShowMsg = false;
        }
    }


}
