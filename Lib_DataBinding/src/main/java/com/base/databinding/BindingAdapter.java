package com.base.databinding;


import android.databinding.BindingConversion;
import android.databinding.ObservableBoolean;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.base.widget.UltimateRecyclerView;
import com.base.widget.UltimateViewAdapter;


/**
 * Created by Syokora on 2016/8/19.
 * Adapter的绑定
 * View的visible、invisible、gone
 */
public class BindingAdapter {
    
    @android.databinding.BindingAdapter("android:adapter")
    public static <T extends ListAdapter & Filterable> void setAdapter(@NonNull AutoCompleteTextView autoCompleteTextView, @Nullable T adapter) {
        autoCompleteTextView.setAdapter(adapter);
    }
    
    @android.databinding.BindingAdapter("android:onItemClickListener")
    public static void setOnItemClickListener(@NonNull AutoCompleteTextView autoCompleteTextView, @Nullable OnItemClickListener listener) {
        autoCompleteTextView.setOnItemClickListener(listener);
    }
    
    @android.databinding.BindingAdapter("android:bindAdapter")
    public static <T extends UltimateViewAdapter> void setUltimateRecyclerViewAdapter(@NonNull UltimateRecyclerView recyclerView, @Nullable T adapter) {
        if (adapter != null) {
            recyclerView.setAdapter(adapter);
        }
    }
    
    @android.databinding.BindingAdapter("android:bindAdapter")
    public static <T extends UltimateViewAdapter> void setRecyclerViewAdapter(@NonNull RecyclerView recyclerView, @Nullable T adapter) {
        if (adapter != null) {
            recyclerView.setAdapter(adapter);
        }
    }
    
    
    /**
     * 这个只处理visible和invisible
     *
     * @param view
     * @param visible
     */
    @android.databinding.BindingAdapter("android:visibleOrInvisible")
    public static void setVisibleses(@NonNull View view, @NonNull boolean visible) {
        if (visible) {
            if (view.getVisibility() != View.VISIBLE)
                view.setVisibility(View.VISIBLE);
        } else {
            if (view.getVisibility() != View.INVISIBLE)
                view.setVisibility(View.INVISIBLE);
        }
    }
    
    /**
     * 这个只处理visible和gone
     *
     * @param view
     * @param visible
     */
    @android.databinding.BindingAdapter("android:visibleOrGone")
    public static void setViewVisiblesOrGones(@NonNull View view, @NonNull boolean visible) {
        if (visible) {
            if (view.getVisibility() != View.VISIBLE)
                view.setVisibility(View.VISIBLE);
        } else {
            if (view.getVisibility() != View.GONE)
                view.setVisibility(View.GONE);
        }
    }
    
    /**
     * 这个只处理visible和gone
     *
     * @param view
     * @param visible
     */
    @android.databinding.BindingAdapter("android:visibleOrGone")
    public static void setViewGroupVisiblesOrGone(@NonNull ViewGroup view, @NonNull boolean visible) {
        if (visible) {
            if (view.getVisibility() != View.VISIBLE)
                view.setVisibility(View.VISIBLE);
        } else {
            if (view.getVisibility() != View.GONE)
                view.setVisibility(View.GONE);
        }
    }
    
    /**
     * 添加下划线
     *
     * @param textView
     * @param d
     */
    @android.databinding.BindingAdapter("android:deleteLines")
    public static void setTextViewDeleteLine(@NonNull TextView textView, @NonNull boolean d) {
        if (d)
            textView.getPaint()
                    .setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }
    
    /**
     * 添加下划线
     *
     * @param textView
     * @param d
     */
    @android.databinding.BindingAdapter("android:underLine")
    public static void setTextViewUnderLine(@NonNull TextView textView, @NonNull boolean d) {
        if (d)
            textView.getPaint()
                    .setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }
    
    /**
     * 添加下划线
     *
     * @param radioButton
     * @param style
     */
    @android.databinding.BindingAdapter("android:typeFaces")
    public static void setTextViewTypeFaces(@NonNull TextView radioButton, @StyleRes int style) {
        radioButton.setTypeface(Typeface.defaultFromStyle(style));
    }
    
    @BindingConversion
    public static boolean convertBindableToBoolean(ObservableBoolean observableBoolean) {
        return observableBoolean.get();
    }
}