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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;


/**
 * Created by LZ on 2016/8/24.
 *
 */
public class DataBindingBaseAdapter<ItemType> extends BaseAdapter {

    private final Context context;
    @LayoutRes
    private final int itemLayout;
    private final int itemVariableId;


    public interface CallBack {
        void onGetView(int i, View view, ViewGroup viewGroup);
    }


    CallBack callBack;
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
    public DataBindingBaseAdapter(@LayoutRes int itemViewID, int itemVariableId, @NonNull Context context, @Nullable CallBack callBack) {
        this.itemLayout = itemViewID;
        this.itemVariableId = itemVariableId;
        this.callBack = callBack;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder<>();
            holder.binding = DataBindingUtil.inflate(LayoutInflater.from(context), itemLayout, null, false);
            convertView = holder.binding.getRoot();
            convertView.setTag(R.id.dataBindingBaseAdapterTag, holder);
        } else {
            holder = (Holder) convertView.getTag(R.id.dataBindingBaseAdapterTag);
        }
        holder.binding.setVariable(itemVariableId, items.get(position));

        if (callBack != null) {
            callBack.onGetView(position, convertView, viewGroup);
        }
        return convertView;
    }

    public class Holder<ViewBinding extends ViewDataBinding> {
        ViewBinding binding;
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
}
