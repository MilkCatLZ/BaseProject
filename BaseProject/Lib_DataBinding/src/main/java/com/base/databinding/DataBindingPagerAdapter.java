package com.base.databinding;


import android.content.Context;
import android.database.DataSetObserver;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.Keep;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


/**
 * Created by LZ on 2016/8/24.
 * 可以从tag:R.id.image_list中获取到items
 * 可以从tag:R.id.pager_adapter_binding 获取到binding
 */
public class DataBindingPagerAdapter<ItemType> extends PagerAdapter {

    private Object[] objects;
    private int[] variableIDList;

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof View) {
            ViewDataBinding binding = (ViewDataBinding) ((View) object).getTag(R.id.pager_adapter_binding);
            binding.unbind();
        }
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), itemLayout, null, false);
        binding.setVariable(itemVariableId, items.get(position));
        binding.getRoot()
               .setTag(R.id.pager_adapter_binding, binding);
        binding.getRoot()
               .setTag(R.id.item_object, items.get(position));
        binding.getRoot()
               .setTag(R.id.item_position, position);
        if (variableIDList != null && variableIDList.length > 0) {
            for(int i = 0; i < variableIDList.length; i++) {
                binding.setVariable(variableIDList[i], objects[i]);
            }
        }
        if (callBack != null) {
            callBack.onInstantiateItem(container, position,binding);
        }
        container.addView(binding.getRoot());
        return binding.getRoot();
    }


    private final Context context;
    @LayoutRes
    private final int itemLayout;
    private final int itemVariableId;
    CallBack callBack;


    public interface CallBack {
        void onInstantiateItem(ViewGroup container, int position, ViewDataBinding binding);
    }


    /**
     *
     */
    @NonNull
    public final ObservableArrayList<ItemType> items = new ObservableArrayList<>();

    @NonNull
    private final ObservableList.OnListChangedCallback<ObservableArrayList<ItemType>>
        observer = new ObservableList.OnListChangedCallback<ObservableArrayList<ItemType>>() {
        {
            items.addOnListChangedCallback(this);
        }

        @Override
        public void onChanged(ObservableArrayList<ItemType> sender) {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(ObservableArrayList<ItemType> sender, int positionStart, int itemCount) {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeInserted(ObservableArrayList<ItemType> sender, int positionStart, int itemCount) {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeMoved(ObservableArrayList<ItemType> sender, int fromPosition, int toPosition, int itemCount) {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeRemoved(ObservableArrayList<ItemType> sender, int positionStart, int itemCount) {
            notifyDataSetChanged();
        }
    };

    /**
     * @param itemViewID     item view 的 layout
     * @param itemVariableId item view 里面 item 所对应的ID
     *                       * @param onItemClickListener 设置方式为： 在布局中<variable name="onItemClickListener" type="android.view.View.OnClickListener"/>，并绑定到对应需要点击的位置。
     */
    public DataBindingPagerAdapter(@NonNull Context context, @LayoutRes int itemViewID, int itemVariableId, @Nullable int[] variableIDList, @Nullable Object[] objects,
                                   @Nullable CallBack callBack) {
        this.itemLayout = itemViewID;
        this.itemVariableId = itemVariableId;
        this.callBack = callBack;
        this.context = context;
        if (variableIDList != null && objects != null) {
            this.variableIDList = variableIDList;
            this.objects = objects;
        }
    }

    /**
     * @param itemViewID     item view 的 layout
     * @param itemVariableId item view 里面 item 所对应的ID
     *                       * @param onItemClickListener 设置方式为： 在布局中<variable name="onItemClickListener" type="android.view.View.OnClickListener"/>，并绑定到对应需要点击的位置。
     */
    public DataBindingPagerAdapter(@NonNull Context context, @LayoutRes int itemViewID, int itemVariableId, @Nullable CallBack callBack) {
        this( context,itemViewID, itemVariableId, null, null, callBack);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        super.unregisterDataSetObserver(observer);
    }

    /**
     * 在列表整体变化时使用这个比较方便
     *
     * @param items      new items
     * @param pageNumber check if init
     */
    @Keep
    public void setItems(List<ItemType> items, int pageNumber) {
        if (pageNumber == 1) {
            this.items.clear();
        }
        addItems(items);
    }

    @Keep
    public void addItems(List<ItemType> items) {
        this.items.addAll(items);
    }

    /**
     * 添加，无动画效果
     * @param item
     */
    @Keep
    public void addItem(ItemType item){
        this.items.add(item);
        notifyDataSetChanged();
    }

    /**
     * 移除，无动画效果
     * @param item
     */
    @Keep
    public void removeItem(ItemType item){
        this.items.remove(item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
