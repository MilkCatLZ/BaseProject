package com.base.util;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.widget.TextView;

import java.lang.reflect.Field;


/**
 * 确认对话框
 *
 * @author LiuZHi
 */
public class DialogManager extends DialogFragment {
    private static final String LNCUSTOM_DIALOG_DIALOG = "DialogManager:dialog";
    public static int titleColor, messageColor, titleID;
    protected CharSequence title = "提示";
    protected CharSequence message = "";
    protected CharSequence rightButtonText;
    protected CharSequence leftButtonText;
    protected OnClickListener rightButtonOnClickListener;
    protected OnClickListener leftButtonOnClickListener;
    
    protected Context context;
    protected FragmentManager manager;
    private boolean rightButtonVisible = true;
    private boolean leftButtonVisible = true;
    private OnDismissListener onDismissListener;
    
    public static void init(int titleColor, int messageColor, int titleID) {
        DialogManager.titleColor = titleColor;
        DialogManager.messageColor = messageColor;
        DialogManager.titleID = titleID;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (rightButtonText == null)
            rightButtonText = getString(android.R.string.ok);
        if (leftButtonText == null)
            leftButtonText = getString(android.R.string.cancel);
        if (StringUtils.isEmpty(title)) {
            if (titleID == 0) {
                PackageInfo pkg = null;
                try {
                    pkg = getActivity().getPackageManager()
                                       .getPackageInfo(getActivity().getApplication()
                                                                    .getPackageName(), 0);
                    String appName = pkg.applicationInfo.loadLabel(getActivity().getPackageManager())
                                                        .toString();
                    title = appName;
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                title = getString(titleID);
            }
        }
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        
        
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
               .setMessage(message);
        if (rightButtonVisible)
            builder.setPositiveButton(rightButtonText, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                    if (rightButtonOnClickListener != null) {
                        rightButtonOnClickListener.onClick(dialog, which);
                    }
                }
            });
        if (leftButtonVisible)
            builder.setNegativeButton(leftButtonText, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                    if (leftButtonOnClickListener != null) {
                        leftButtonOnClickListener.onClick(dialog, which);
                    }
                }
            });
        
        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        
        return alert;
    }
    
    public static DialogManager with(Context context, FragmentManager manager) {
        return new DialogManager().setContext(context)
                                  .setManager(manager);
    }
    
    private DialogManager setContext(Context context) {
        this.context = context;
        return this;
    }
    
    public DialogManager setManager(FragmentManager manager) {
        this.manager = manager;
        return this;
    }
    
    public DialogManager leftButtonText(@StringRes int leftButtonText) {
        this.leftButtonText = context.getString(leftButtonText);
        return this;
    }
    
    public DialogManager leftButtonText(String leftButtonText) {
        this.leftButtonText = leftButtonText;
        return this;
    }
    
    public DialogManager rightButtonText(@StringRes int rightButtonText) {
        this.rightButtonText = context.getString(rightButtonText);
        return this;
    }
    
    public DialogManager rightButtonText(String rightButtonText) {
        this.rightButtonText = rightButtonText;
        return this;
    }
    
    
    public DialogManager onLeftClick(OnClickListener onLeftClick) {
        this.leftButtonOnClickListener = onLeftClick;
        return this;
    }
    
    public DialogManager onRightClick(OnClickListener onRightClick) {
        this.rightButtonOnClickListener = onRightClick;
        return this;
    }
    
    
    public DialogManager title(String title) {
        this.title = title;
        return this;
    }
    
    public DialogManager title(@StringRes int title) {
        this.title = context.getString(title);
        return this;
    }
    
    public DialogManager message(String message) {
        this.message = message;
        return this;
    }
    
    public DialogManager message(@StringRes int message) {
        this.message = context.getString(message);
        return this;
    }
    
    public DialogManager rightButtonVisible(boolean rightButtonVisible) {
        this.rightButtonVisible = rightButtonVisible;
        return this;
    }
    
    public DialogManager leftButtonVisible(boolean leftButtonVisible) {
        this.leftButtonVisible = leftButtonVisible;
        return this;
    }
    public DialogManager onDismiss(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }
    
    
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(onDismissListener!=null){
            onDismissListener.onDismiss(dialog);
        }
    }
    
    public DialogManager show() {
        show(manager, LNCUSTOM_DIALOG_DIALOG);
        if (DialogManager.titleColor != 0 || DialogManager.messageColor != 0) {
            setDialogTextColor();
        }
        return this;
    }
    
    private void setDialogTextColor() {
        new AsyncTask<String, String, String>() {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    Field mAlertController = getDialog().getClass()
                                                        .getDeclaredField("mAlert");
                    mAlertController.setAccessible(true);
                    Object object = mAlertController.get(getDialog());
                    mAlertController.setAccessible(true);
                    
                    Field mTitleViewField = object.getClass()
                                                  .getDeclaredField("mTitleView");
                    Field mMessageViewField = object.getClass()
                                                    .getDeclaredField("mMessageView");
                    Field mViewField = object.getClass()
                                             .getDeclaredField("mWindow");
                    
                    mTitleViewField.setAccessible(true);
                    mMessageViewField.setAccessible(true);
                    mViewField.setAccessible(true);
                    
                    TextView mTitleView = (TextView) mTitleViewField.get(object);
                    TextView mMessageView = (TextView) mMessageViewField.get(object);
                    Window mView = (Window) mViewField.get(object);
//
                    if (titleColor != 0 && mTitleView != null)
                        mTitleView.setTextColor(DialogManager.titleColor);
                    if (messageColor != 0 && mMessageView != null)
                        mMessageView.setTextColor(DialogManager.messageColor);
                    mView.setBackgroundDrawableResource(android.R.color.white);
                    
                } catch (Exception e) {
                    Log.e("DialogBuilder", "onCreateDialog", e);
                }
            }
            
            @Override
            protected String doInBackground(String... params) {
                for(int i = 0; getDialog() == null || !getDialog().isShowing(); i++) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }.execute("");
    }
}
