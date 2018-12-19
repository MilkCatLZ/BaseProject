package com.base.databinding;


import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View.OnClickListener;


/**
 * Created by LZ on 2016/12/21.
 */
public class DataBindingDragAdapter<ItemType> extends DataBindingAdapter<ItemType> implements DataBindingAdapter.CallBack {

    /**
     * @param itemViewID     item view 的 layout
     * @param itemVariableId item view 里面 item 所对应的ID
     *                       * @param onItemClickListener 设置方式为： 在布局中<variable name="onItemClickListener" type="android.view.View.OnClickListener"/>，并绑定到对应需要点击的位置。
     */
    public DataBindingDragAdapter(@LayoutRes int itemViewID, int itemVariableId, @Nullable CallBack callBack) {
        super(itemViewID, itemVariableId, callBack);
        clickVariableID = 0;
        onClickListener = null;
    }


    public void swapPositions(int from, int to) {
        swapPositions(items, from, to);
    }

    public void remove(int position) {
        removeInternal(items, position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition >= 0 && toPosition >= 0) {
            swapPositions(fromPosition, toPosition);
            super.onItemMove(fromPosition, toPosition);
        }

    }

    @Override
    public void onItemDismiss(int position) {
        if (position >= 0) {
            remove(position);
            super.onItemDismiss(position);
        }

    }


    private final int clickVariableID;
    private final OnClickListener onClickListener;
    CallBack callBack;

    /**
     * @param itemViewID      item view 的 layout
     * @param itemVariableId  item view 里面 item 所对应的ID
     * @param clickVariableID 点击的VariableId
     * @param onClickListener 点击事件
     */
    public DataBindingDragAdapter(@LayoutRes int itemViewID, int itemVariableId, @NonNull int clickVariableID, @NonNull OnClickListener onClickListener) {
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
    public DataBindingDragAdapter(@LayoutRes int itemLayout, int itemVariableId, @NonNull int clickVariableID, @NonNull OnClickListener onClickListener,
                                  @NonNull CallBack callBack) {
        super(itemLayout, itemVariableId, null);
        super.callBack = this;
        this.callBack = callBack;
        this.clickVariableID = clickVariableID;
        this.onClickListener = onClickListener;
    }


    @Override
    public void onAfterBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (clickVariableID != 0)
            holder.binding.setVariable(clickVariableID, onClickListener);
        if (this.callBack != null) {
            this.callBack.onAfterBindViewHolder(holder, position);
        }
    }

}
