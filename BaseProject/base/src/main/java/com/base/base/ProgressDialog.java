package com.base.base;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.R;


/**
 * 用于TCBaseActivity中的showLoadingView的全屏进度条
 *
 * @author LiuZhi
 */
public class ProgressDialog extends Dialog {
    // private Context context = null;
    private static ProgressDialog customProgressDialog = null;
    private static boolean canCancelTouchOuside;
    private static Activity activity;

    public ProgressDialog(Context context, int theme) {
        super(context, theme);

    }

    protected ProgressDialog(
            Context context,
            boolean cancelable,
            OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);

    }

    public ProgressDialog(Context context) {
        super(context);

    }

    public static ProgressDialog createDialog(Context context, Activity activity) {
        ProgressDialog.activity = activity;

        customProgressDialog = new ProgressDialog(context, R.style.ProgressDialog);
        View view = LayoutInflater.from(context)
                .inflate(R.layout.progress_dialog_layout, null);
        // 从xml中得到GifView的句柄，当然，事先还要引入GifView的包import com.ant.liao.GifView;
//            GifView myGifView = (GifView)view.findViewById(R.id.img_gif);
//// 设置Gif图片源，首先要将loading.gif导入到drawable文件内
//            myGifView.setGifImage(R.drawable.zhs_loading);
//
//// 设置显示的大小，拉伸或者压缩
//            myGifView.setShowDimension(context.getResources().getDimensionPixelOffset(R.dimen._300px), context.getResources().getDimensionPixelOffset(R.dimen._300px));
//// 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示
//            myGifView.setGifImageType(GifView.GifImageType.COVER);
//            view.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//ODO
//                    if (customProgressDialog != null && canCancelTouchOuside) {
//                        if (customProgressDialog.isShowing()) {
//                            customProgressDialog.dismiss();
//                        }
//                    }
//                }
//            });
        customProgressDialog.setContentView(view);
        customProgressDialog.getWindow()
                .getAttributes().gravity = Gravity.CENTER;


        return customProgressDialog;
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        canCancelTouchOuside = cancel;
    }

    @Override
    public void onBackPressed() {
        if (activity != null) {
            if (customProgressDialog != null) {
                customProgressDialog.dismiss();
                customProgressDialog.setCanceledOnTouchOutside(true);// 恢复默认状态，因为用的是单实例
                customProgressDialog = null;
            }
            activity.onBackPressed();
            activity = null;
        }
        super.onBackPressed();
    }

    //region 进度展示框

    /**
     * 初始化加载中对话框
     *
     * @param dismissListener
     * @param cancelListener
     * @param canceledOnTouchOutside
     */

    private static void initProgressDialog(
            @NonNull Activity activity,
            @Nullable OnDismissListener dismissListener,
            @Nullable OnCancelListener cancelListener,
            boolean canceledOnTouchOutside) {
        customProgressDialog = ProgressDialog.createDialog(activity, activity);
        customProgressDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        // progressDialog.setMessage(getString(R.string.commom_loading_text));
        if (dismissListener != null) {
            customProgressDialog.setOnDismissListener(dismissListener);
        }
        if (cancelListener != null) {
            customProgressDialog.setOnCancelListener(cancelListener);
        }

    }

    /**
     * 展示loadingProgressDialog
     *
     * @param dismissListener
     * @param cancelListener
     * @param canceledOnTouchOutside 点击外面区域是否去掉LoadingView；true:可取消,false:不可
     */
    @Keep
    public static void showLoadingView(
            @NonNull Activity activity,
            @Nullable OnDismissListener dismissListener,
            @Nullable OnCancelListener cancelListener,
            boolean canceledOnTouchOutside,
            boolean needHide) {

        try {
            if (ProgressDialog.activity != null) {
                hideLoadingView(ProgressDialog.activity);
            }
            if (ProgressDialog.activity != null && needHide) {
                View view = ((ViewGroup) ProgressDialog.activity.findViewById(android.R.id.content)).getChildAt(0);
                if (view != null && view.getVisibility() == View.VISIBLE) {
                    view.setVisibility(View.GONE);
                }
            }
            if (!activity.isFinishing()) {
                if (customProgressDialog == null) {
                    initProgressDialog(activity, dismissListener, cancelListener,
                            canceledOnTouchOutside);
                    try {
                        customProgressDialog.show();
                    } catch (Exception e) {

                    }
                } else if (!customProgressDialog.isShowing()) {
                    ProgressDialog.activity = activity;
                    customProgressDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
                    try {
                        customProgressDialog.show();
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception ignored) {
//            e.printStackTrace();
        }

    }


    /**
     * 展示loadingProgressDialog
     *
     * @param activity
     * @param dismissListener
     * @param cancelListener
     */
    @Keep
    public static void showLoadingView(
            @NonNull Activity activity,
            @Nullable OnDismissListener dismissListener,
            @Nullable OnCancelListener cancelListener) {
        showLoadingView(activity, dismissListener, cancelListener, false,false);// 默认点击外面不可以取消
    }


    @Keep
    public static void showLoadingView(
            @NonNull Context activity,
            @Nullable OnDismissListener dismissListener,
            @Nullable OnCancelListener cancelListener) {
        if (activity instanceof Activity)
            showLoadingView((Activity) activity, dismissListener, cancelListener, false,false);// 默认点击外面不可以取消
    }

    @Keep
    public static void showLoadingView(
            @NonNull Context activity) {
        if (activity instanceof Activity)
            showLoadingView((Activity) activity, null, null, false,false);// 默认点击外面不可以取消
    }

    @Keep
    public static void showLoadingView(
            @NonNull Activity activity) {
            showLoadingView(activity, null, null, false,false);// 默认点击外面不可以取消
    }

    @Keep
    public static void showLoadingView(
            @NonNull Activity activity,boolean needHideRoot) {
            showLoadingView(activity, null, null, false,needHideRoot);// 默认点击外面不可以取消
    }
   @Keep
    public static void showLoadingView(
            @NonNull Context activity,boolean needHideRoot) {
       if (activity instanceof Activity)
            showLoadingView((Activity) activity, null, null, false,needHideRoot);// 默认点击外面不可以取消
    }

    /**
     * 隱藏ProgressDialog
     *
     * @param activity
     */
    @Keep
    public static void hideLoadingView(@NonNull Activity activity) {
        try {
            if (!activity.isFinishing()) {
                if (customProgressDialog != null) {
                    if (ProgressDialog.activity != null) {
                        View view = ((ViewGroup) ProgressDialog.activity.findViewById(android.R.id.content)).getChildAt(0);
                        if (view != null && view.getVisibility() == View.GONE) {
                            view.setVisibility(View.VISIBLE);
                        }
                    }

                    customProgressDialog.dismiss();
                    customProgressDialog.setCanceledOnTouchOutside(true);// 恢复默认状态，因为用的是单实例
                    customProgressDialog = null;
                    ProgressDialog.activity = null;
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * 隱藏ProgressDialog
     *
     * @param activity
     */
    @Keep
    public static void hideLoadingView(@NonNull Context activity) {
        if (activity instanceof Activity) {
            hideLoadingView((Activity) activity);
        }
    }


    //endregion


}
