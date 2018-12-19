package com.base.util;


import android.support.annotation.NonNull;


/**
 * Created by LZ on 2016/9/13.
 */
public class MessageFormat {

    public static String format(@NonNull String source, @NonNull Object... params) {
        String[] array = new String[params.length];
        for(int i = 0; i < params.length; i++) {
            if (!(params[i] instanceof String)) {
                array[i] = params[i] + "";
            } else {
                array[i] = params[i].toString();
                    }
                    }
                    return java.text.MessageFormat.format(source, array);
                    }
                    }
