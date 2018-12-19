package com.base.app;


import android.app.Activity;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by LZ on 2016/8/18.
 */
public class BaseApplication extends Application implements BaseApplicationInterface{
    protected List<Activity> activityList = new ArrayList<>();

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public void exit() {
        exitActivity();
        System.exit(0);
    }

    public void exitActivity() {
        for(int i = 0; i < activityList.size(); i++) {
            activityList.get(i)
                        .finish();
        }
        activityList.clear();
    }

    /**
     * 判断Activiyt是否存在
     *
     * @param className simpleName
     *
     * @return
     */
    public boolean isActivityRunning(@NonNull String className) {
        for(Activity activity : activityList) {
            if (activity.getClass().getSimpleName().equals(className)) {
                return true;
            }
        }
        return false;
    }
}
