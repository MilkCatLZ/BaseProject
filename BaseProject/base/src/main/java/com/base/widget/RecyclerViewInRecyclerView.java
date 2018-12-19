package com.base.widget;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * 专门内嵌到ScrollView里面的列表
 * Created by LZ on 15-12-18.
 */
public class RecyclerViewInRecyclerView extends RecyclerView {

    public RecyclerViewInRecyclerView(Context context) {
        super(context);

    }

    public RecyclerViewInRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onInterceptTouchEvent(MotionEvent event)
    {
//        super.onInterceptTouchEvent(event);
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
//        super.onTouchEvent(e);
        return false;
    }
}
