package com.base.base;


import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;


/**
 * Created by LZ on 2016/8/11.
 * 弹出框辅助类
 */
public class PopupWindowManager {

    /**
     * @param contentView
     *
     * @return
     */
    public static PopupWindow init(@NonNull View contentView, @Nullable OnDismissListener onDismissListener) {

        PopupWindow popupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT,
                                                  LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        if (onDismissListener != null) {
            popupWindow.setOnDismissListener(onDismissListener);
        }
        return popupWindow;
    }

    /**
     * @param contentView
     *
     * @return
     */
    public static PopupWindow init(@NonNull View contentView) {
        return init(contentView, null);
    }
    
    /**
     * 设置背景透明度
     * @param activity
     * @param alpha
     */
    public static void setWindowAlpha(@NonNull Activity activity, float alpha) {
        WindowManager.LayoutParams lp = activity.getWindow()
                                                .getAttributes();
        lp.alpha = alpha;
        activity.getWindow()
                .setAttributes(lp);
    }
}
