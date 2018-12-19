package com.base.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by LZ on 2016/11/21.
 * 跑马灯用法，设置如下属性：
 * android:ellipsize="marquee"
 * android:focusable="true"
 * android:focusableInTouchMode="true"
 * android:marqueeRepeatLimit="marquee_forever"
 */
public class MarqueeTextView extends TextView {
    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
