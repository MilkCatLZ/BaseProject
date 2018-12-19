package com.base.databinding;


import android.support.v7.util.DiffUtil.Callback;

import java.util.List;


/**
 * Created by LZ on 2017/4/5.
 *
 */
public class DiffUtilCallback<Item> extends Callback {
    private List<Item> oldList;
    private List<Item> newList;
    
    public DiffUtilCallback(List<Item> oldList, List<Item> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }
    
    @Override
    public int getOldListSize() {
        return 0;
    }
    
    @Override
    public int getNewListSize() {
        return 0;
    }
    
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }
    
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }
}
