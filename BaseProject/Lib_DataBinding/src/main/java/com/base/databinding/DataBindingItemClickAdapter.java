package com.base.databinding;


import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View.OnClickListener;


/**
 * Created by Syokora on 2016/8/21.
 */
public class DataBindingItemClickAdapter<ItemType> extends DataBindingAdapter<ItemType> implements DataBindingAdapter.CallBack {

    private final int clickVariableID;
    private final OnClickListener onClickListener;
    CallBack callBack;

    /**
     * @param itemViewID      item view 的 layout
     * @param itemVariableId  item view 里面 item 所对应的ID
     * @param clickVariableID 点击的VariableId
     * @param onClickListener 点击事件
     */
    public DataBindingItemClickAdapter(@LayoutRes int itemViewID, int itemVariableId, @NonNull int clickVariableID, @NonNull OnClickListener onClickListener) {
        super(itemViewID, itemVariableId, null);
        super.callBack = this;
        this.clickVariableID = clickVariableID;
        this.onClickListener = onClickListener;
    }

    /**
     * @param itemLayout      item view 的 layout
     * @param itemVariableId  item view 里面 item 所对应的ID
     * @param clickVariableID 点击的VariableId
     * @param onClickListener 点击事件
     */
    public DataBindingItemClickAdapter(@LayoutRes int itemLayout, int itemVariableId, @NonNull int clickVariableID, @NonNull OnClickListener onClickListener, @NonNull CallBack callBack) {
        super(itemLayout, itemVariableId, null);
        super.callBack = this;
        this.callBack = callBack;
        this.clickVariableID = clickVariableID;
        this.onClickListener = onClickListener;
    }


    @Override
    public void onAfterBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.binding.setVariable(clickVariableID, onClickListener);
        if (this.callBack != null) {
            this.callBack.onAfterBindViewHolder(holder, position);
        }
    }

}
