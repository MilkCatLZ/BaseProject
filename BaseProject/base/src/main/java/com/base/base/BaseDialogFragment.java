package com.base.base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.base.R;
import com.base.app.BaseApplicationInterface;


/**
 * Created by LZ on 2016/9/9.
 */
public abstract class BaseDialogFragment<App extends BaseApplicationInterface> extends DialogFragment {
    
    protected App app;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (app == null) {
            try {
                app = getBaseApplicationInterface();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    protected abstract App getBaseApplicationInterface();
    
    @Override
    public void onResume() {
        LayoutParams params = getDialog().getWindow()
                                                   .getAttributes();
        params.width = LayoutParams.MATCH_PARENT;
        params.height = LayoutParams.WRAP_CONTENT;
        getDialog().getWindow()
                   .setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.ActivityDialog);
    }
    
    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }
}
