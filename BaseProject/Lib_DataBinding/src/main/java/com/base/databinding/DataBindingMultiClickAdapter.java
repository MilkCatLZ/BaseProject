package com.base.databinding;


import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.base.databinding.DataBindingAdapter.CallBack;


/**
 * Created by Syokora on 2016/8/21.
 */
public class DataBindingMultiClickAdapter<ItemType> extends DataBindingAdapter<ItemType> implements CallBack {

    private Object[] onClickListeners;
    private int[] clickVariableIDs;

    CallBack callBack;

    /**
     * @param itemViewID       item view 的 layout
     * @param itemVariableId   item view 里面 item 所对应的ID
     * @param clickVariableIDs 点击的绑定id数组，可以用{@link #getClickVariableIDs}生成
     * @param onClickListeners 点击事件数组，可以用{@link #getOnClickListeners}生成
     */
    public DataBindingMultiClickAdapter(@LayoutRes int itemViewID, int itemVariableId, @NonNull int[] clickVariableIDs, @NonNull Object[] onClickListeners) throws
                                                                                                                                                            ClickEventNumMismatchException {
        super(itemViewID, itemVariableId, null);
        init(clickVariableIDs, onClickListeners, null);
    }

    /**
     * @param itemLayout       item view 的 layout
     * @param itemVariableId   item view 里面 item 所对应的ID
     * @param clickVariableIDs 点击的绑定id数组，可以用{@link #getClickVariableIDs}生成
     * @param onClickListeners 点击事件数组，可以用{@link #getOnClickListeners}生成
     */
    public DataBindingMultiClickAdapter(@LayoutRes int itemLayout, int itemVariableId, @NonNull int[] clickVariableIDs, @NonNull Object[] onClickListeners,
                                        @Nullable CallBack callback) throws
                                                                                        ClickEventNumMismatchException {
        super(itemLayout, itemVariableId, null);
        init(clickVariableIDs, onClickListeners, callback);
        this.callBack = callback;
    }

    private void init(int[] clickVariableIDs, Object[] onClickListeners, CallBack callback) throws
                                                                                            ClickEventNumMismatchException {

        if (clickVariableIDs.length != onClickListeners.length) {
            throw new ClickEventNumMismatchException("clickVariableIDs和onClickListeners长度必须相同");
        }
        this.clickVariableIDs = clickVariableIDs;
        this.onClickListeners = onClickListeners;
        super.callBack = this;
    }

    /**
     * 生成绑定id数组
     *
     * @param ids
     *
     * @return
     */
    public static int[] getClickVariableIDs(int... ids) {
        return ids;
    }

    public static Object[] getOnClickListeners(Object... clicks) {
        return clicks;
    }

    @Override
    public void onAfterBindViewHolder(ItemViewHolder holder, int position) {
        for(int i = 0; i < clickVariableIDs.length; i++) {
            holder.binding.setVariable(clickVariableIDs[i], onClickListeners[i]);
        }
        if (this.callBack != null) {
            this.callBack.onAfterBindViewHolder(holder, position);
        }
    }
}
