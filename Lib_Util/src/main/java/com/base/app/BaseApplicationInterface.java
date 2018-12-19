package com.base.app;


import android.app.Activity;


/**
 * Created by LZ on 2017/3/20.
 */

public interface BaseApplicationInterface {
    void addActivity(Activity activity);
    void removeActivity(Activity activity);
    void exit();
}
