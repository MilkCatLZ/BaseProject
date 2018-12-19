package com.base.widget;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * 专门内嵌到ScrollView里面的列表
 * Created by LZ on 15-12-18.
 */
public class RecyclerViewWithScrollView extends RecyclerView {

    public RecyclerViewWithScrollView(Context context) {
        super(context);

    }

    public RecyclerViewWithScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        super.onTouchEvent(e);
        return true;
    }
}
