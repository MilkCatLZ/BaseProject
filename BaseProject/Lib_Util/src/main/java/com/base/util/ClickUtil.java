package com.base.util;


/**
 * Created by LZ on 2016/8/22.
 */
public class ClickUtil {

    private static long lastClickTime;

    public synchronized static boolean canClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 800) {
            return false;
        }
        lastClickTime = time;
        return true;
    }

}
