package com.base.photo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 解决与ViewPager和PhotoView同时使用时，出现nullPointer的BUG
 */
public class ViewPagerWithPhotoView extends ViewPager {

	public ViewPagerWithPhotoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ViewPagerWithPhotoView(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		try {
			return super.onInterceptTouchEvent(arg0);
		} catch (Exception ex) {
		}
		return false;

	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		try {
			return super.onTouchEvent(arg0);
		} catch (IllegalArgumentException ex) {
		}
		return false;
	}
}
