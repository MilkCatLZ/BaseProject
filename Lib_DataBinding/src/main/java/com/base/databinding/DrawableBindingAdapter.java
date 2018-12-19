package com.base.databinding;


import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.base.util.drawable.DrawableTintManager;


/**
 * Created by LZ on 2016/9/28.
 * DrawableLeft/DrawableTop/DrawableRight/DrawableBottom
 * 的染色，包括一般染色和selector
 * color:    @color/xxx
 * selector: @colorStateList/xxx
 */
public class DrawableBindingAdapter {

    private static Drawable tintColorList(@NonNull Drawable drawable, @Nullable ColorStateList colorStateList) {
        return DrawableTintManager.tintColorList(drawable, colorStateList);
    }

    @android.databinding.BindingAdapter("android:drawableLeftColor")
    public static void setDrawableLeftColor(@NonNull TextView textView, @Nullable ColorStateList colorStateList) {
        Drawable sourceDrawable = textView.getCompoundDrawables()[0];
        textView.setCompoundDrawables(tintColorList(sourceDrawable, colorStateList), null, null, null);
    }

    @android.databinding.BindingAdapter("android:drawableTopColor")
    public static void setDrawableTopColor(@NonNull TextView textView, @Nullable ColorStateList colorStateList) {
        Drawable sourceDrawable = textView.getCompoundDrawables()[1];
        textView.setCompoundDrawables(null, tintColorList(sourceDrawable, colorStateList), null, null);
    }

    @android.databinding.BindingAdapter("android:drawableRightColor")
    public static void setDrawableRightColor(@NonNull TextView textView, @Nullable ColorStateList colorStateList) {
        Drawable sourceDrawable = textView.getCompoundDrawables()[2];
        textView.setCompoundDrawables(null, null, tintColorList(sourceDrawable, colorStateList), null);
    }

    @android.databinding.BindingAdapter("android:drawableBottomColor")
    public static void setDrawableBottomColor(@NonNull TextView textView, @Nullable ColorStateList colorStateList) {
        Drawable sourceDrawable = textView.getCompoundDrawables()[3];
        textView.setCompoundDrawables(null, null, null, tintColorList(sourceDrawable, colorStateList));
    }
    @android.databinding.BindingAdapter("android:drawableTop")
    public static void setDrawableTop(@NonNull TextView textView, @Nullable Drawable drawable) {
        textView.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null);
    }
    
    @android.databinding.BindingAdapter("android:drawableStart")
    public static void setDrawableStarts(@NonNull TextView textView, @Nullable Drawable drawable) {
        textView.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
    }
//    @android.databinding.BindingAdapter("android:drawableStarts")
//    public static void setDrawableStartss(@NonNull TextView textView, @Nullable Drawable drawable) {
//        textView.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
//    }
}
