package com.base.util;


import android.text.TextUtils;


/**
 * Created by LZ on 2016/8/10.
 */
public class StringUtils {

    /**
     * @param string
     *
     * @return
     */
    public static boolean isEmpty(CharSequence string) {
        return TextUtils.isEmpty(string);
    }

    /**
     * @param string
     *
     * @return
     */
    public static boolean isNotEmpty(CharSequence string) {
        return !TextUtils.isEmpty(string);
    }

}
