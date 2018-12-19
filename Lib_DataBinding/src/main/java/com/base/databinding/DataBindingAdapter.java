/*******************************************************************************
 * Copyright (c) 2015 $user, tcloudit.com
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software !!within tcloudit.com!! , including the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/

package com.base.databinding;


import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.support.annotation.Keep;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.databinding.databinding.FooterBinding;
import com.base.widget.UltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;

import java.lang.reflect.Method;
import java.util.List;


/**
 * 使用了DataBinding的{@link RecyclerView.Adapter}。
 * <p/>
 * Created by yk on 15/11/16.
 */
public class DataBindingAdapter<ItemType> extends UltimateViewAdapter<ItemViewHolder> {
    
    public interface CallBack {
        void onAfterBindViewHolder(ItemViewHolder holder, int position);
    }
    
    
    CallBack callBack;
    
    
    @LayoutRes
    private final int itemLayout;
    
    private final int itemVariableId;
    
    protected boolean hasNextPage = true;
    
    /**
     * @param itemViewID     item view 的 layout
     * @param itemVariableId item view 里面 item 所对应的ID
     *                       * @param onItemClickListener 设置方式为： 在布局中<variable name="onItemClickListener" type="android.view.View.OnClickListener"/>，并绑定到对应需要点击的位置。
     */
    public DataBindingAdapter(@LayoutRes int itemViewID, int itemVariableId, @Nullable CallBack callBack) {
        this.itemLayout = itemViewID;
        this.itemVariableId = itemVariableId;
        this.callBack = callBack;
    }
    
    /**
     *
     */
    @NonNull
    private static final String tag = "DataBindingAdapter";
    
    /**
     *
     */
    public DataBindingAdapter() {
        this.itemLayout = 0;
        this.itemVariableId = 0;
        //
        String getItemVariableId = "getItemVariableId";
        String getItemViewType = "getItemViewType";
        Class[] params = {int.class};
        boolean foundItemVariableId = false;
        boolean foundItemViewType = false;
        //
        for(Class<?> theClass = getClass();
            !DataBindingAdapter.class.equals(theClass);
            theClass = theClass.getSuperclass()) {
            //
            try {
                Method m = theClass.getDeclaredMethod(getItemViewType, params);
                if (int.class.equals(m.getReturnType())) {
                    foundItemViewType = true;
                }
            } catch (NoSuchMethodException e) {
                Log.w(tag, e);
            }
            //
            try {
                Method m = theClass.getDeclaredMethod(getItemVariableId, params);
                if (int.class.equals(m.getReturnType())) {
                    foundItemVariableId = true;
                }
            } catch (NoSuchMethodException e) {
                Log.w(tag, e);
            }
        }
        //
        if (foundItemVariableId && foundItemViewType) {
            return;
        }
        //
        StringBuilder msg = new StringBuilder(getClass().getName());
        msg.append(" must OVERRIDE ");
        if (!foundItemVariableId) {
            msg.append(getItemVariableId)
               .append("(int) method");
        }
        if (!foundItemVariableId && !foundItemViewType) {
            msg.append(" and ");
        }
        if (!foundItemViewType) {
            msg.append(getItemViewType)
               .append("(int) method");
        }
        msg.append(" for using default constructor");
        throw new RuntimeException(msg.toString());
    }
    
    /**
     * @param position position
     *
     * @return item view 里面 item 所对应的ID
     */
    @Keep
    public int getItemVariableId(int position) {
        return itemVariableId;
    }
    
    /**
     * @param position position
     *
     * @return itemView的LayoutID 传错会报错
     */
    @Keep
    @LayoutRes
    @Override
    public final int getItemViewType(int position) {
        
        if (position == getItemCount() - 1 && footerEnable) {
            return getFooterViewType();
        } else {
            if (super.getItemViewType(position) == VIEW_TYPES.NORMAL) {
                return getNormalItemViewType();
            } else {
                return super.getItemViewType(position);
            }
        }
    }
    
    
    /**
     * @return {@link LayoutRes}layout布局id，不是TypeID注意
     */
    protected int getNormalItemViewType() {
        return itemLayout;
    }
    
    /**
     * @param position position
     *
     * @return item 本身的 id
     */
    @Override
    public long getItemId(int position) {
        ItemType item;
        int count = getAdapterItemCount();
        if (position >= count || position < 0) {
            item = null;
            Log.w(tag, "getItemId" + ": invalid position " + position + " while item count is " + count);
        } else {
            item = items.get(position);
        }
        return item != null ? item.hashCode() : RecyclerView.NO_ID;
    }
    
    @Override
    public ItemViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        ItemViewHolder holder = (ItemViewHolder) realContentView.getTag(R.id.databinding_holder);
        //兼容SwipeMenu
        if (holder == null) {
            ViewGroup viewGroup = ((ViewGroup) realContentView.findViewById(R.id.swipe_content));
            
            holder = (ItemViewHolder) viewGroup.getChildAt(0)
                                               .getTag(R.id.databinding_holder);
            realContentView.setTag(R.id.databinding_binding, holder.binding);
            
            holder = new ItemViewHolder(realContentView);
            realContentView.setTag(R.id.databinding_holder, holder);
        }
        return holder;
    }
    
    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        ItemViewHolder holder = null;
        View rootView;
        if (viewType == FOOTER) {
            holder = new ItemViewHolder(footerBinding.getRoot());
            
            rootView = footerBinding.getRoot();
            rootView.setTag(R.id.databinding_holder, holder);
            return rootView;
        } else {
            if (viewType == VIEW_TYPES.FOOTER) {//自带的不要用，有bug并且不好控制
                ItemViewHolder viewHolder = newFooterHolder(customLoadMoreView);
                /**
                 * this is only for the first time rendering of the adapter
                 */
                customLoadMoreItemView = viewHolder.itemView;
                if (getAdapterItemCount() == 0) {
                    removeDispatchLoadMoreView();
                }
                if (enabled_custom_load_more_view && getAdapterItemCount() > 0) {
                    revealDispatchLoadMoreView();
                }
                rootView = viewHolder.binding.getRoot();
            } else if (viewType == VIEW_TYPES.HEADER) {
                holder = newHeaderHolder(customHeaderView);
                rootView = holder.binding.getRoot();
            } else if (viewType == VIEW_TYPES.ADVIEW) {
                holder = getAdViewHolder(customHeaderView);
                rootView = holder.binding.getRoot();
            } else if (viewType == VIEW_TYPES.CUSTOMVIEW) {
                holder = getAdViewHolder(customHeaderView);
                rootView = holder.binding.getRoot();
            } else if (viewType == VIEW_TYPES.NOVIEW) {
                holder = getNoViewHolder(customHeaderView);
                rootView = holder.binding.getRoot();
            } else {
                holder = new ItemViewHolder<>(parent, viewType);
                rootView = holder.binding.getRoot();
            }
            rootView.setTag(R.id.databinding_holder, holder);
            return rootView;
        }
        
    }
    
    @Override
    public final void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        int count = getAdapterItemCount();
        if (position >= count || position < 0) {
            Log.w(tag, "onAfterBindViewHolder" + ": invalid position " + position + " while item count is " + count);
            return;
        }
        holder.onBind(getItemVariableId(position), items.get(position));
        if (callBack != null) {
            callBack.onAfterBindViewHolder(holder, position);
        }
    }
    
    /**
     * 不要使用这个方法判断数量，用{@link #getAdapterItemCount()}
     *
     * @return
     */
    @Override
    @Deprecated
    public int getItemCount() {
        if (footerEnable) {
            return super.getItemCount() + 1;
        } else {
            return super.getItemCount();
        }
    }
    
    @NonNull
    public final ObservableArrayList<ItemType> items = new ObservableArrayList<>();
    
    @NonNull
    public final ObservableBoolean isEmpty = new ObservableBoolean(true);
    
    @NonNull
    private final ObservableList.OnListChangedCallback<ObservableArrayList<ItemType>>
        observer = new ObservableList.OnListChangedCallback<ObservableArrayList<ItemType>>() {
        {
            items.addOnListChangedCallback(this);
        }
        
        @Override
        public void onChanged(ObservableArrayList<ItemType> sender) {
            isEmpty.set(sender.isEmpty());
            notifyDataSetChanged();
        }
        
        @Override
        public void onItemRangeChanged(ObservableArrayList<ItemType> sender, int positionStart, int itemCount) {
            isEmpty.set(sender.isEmpty());
            notifyItemRangeChanged(positionStart, itemCount);
        }
        
        @Override
        public void onItemRangeInserted(ObservableArrayList<ItemType> sender, int positionStart, int itemCount) {
            isEmpty.set(sender.isEmpty());
            notifyItemRangeInserted(positionStart, itemCount);
        }
        
        @Override
        public void onItemRangeMoved(ObservableArrayList<ItemType> sender, int fromPosition, int toPosition, int itemCount) {
            isEmpty.set(sender.isEmpty());
            notifyItemRangeRemoved(fromPosition, itemCount);
        }
        
        @Override
        public void onItemRangeRemoved(ObservableArrayList<ItemType> sender, int positionStart, int itemCount) {
            isEmpty.set(sender.isEmpty());
            notifyItemRangeRemoved(positionStart, itemCount);
        }
    };
    
    /**
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        items.removeOnListChangedCallback(observer);
        super.finalize();
    }

//    protected int getPageCount() {
//        return 0;
//    }

//    protected int getPageSize() {
//        return DEFAULT_PAGE_SIZE;
//    }

//    protected boolean needNextPage(int position) {
//        if (!hasNextPage) {
//            // 如果已经没有下一页的数据，就不用去取了。
//            return false;
//        }
//        int pageSize = getPageSize();
//        int total = items.size();
//        if (position + pageSize <= total) {
//            // 如果还没有准备显示到列表末尾，就不用去取。
//            return false;
//        }
//        int requestedCount = getPageCount();
//        // 如果已经去取过了，就不用再去取了。
//        return requestedCount * pageSize < total;
//    }

//    /**
//     *
//     */
//    public static final int DEFAULT_PAGE_SIZE = 3;
    
    /**
     * 在列表整体变化时使用这个比较方便
     *
     * @param items      new items
     * @param pageNumber check if init
     */
    @Keep
    public void setItems(List<ItemType> items, int pageNumber) {
        if (pageNumber == 1) {
//            int size= this.items.size();
            this.items.clear();
//            notifyAfterRemoveAllData(size,size);
//            notifyDataSetChanged();
        }
        addItems(items);
    }
    
    @Keep
    public void addItems(List<ItemType> items) {
        int i = this.items.size();
        this.items.addAll(items);
        if (footerEnable) {
            notifyDataSetChanged();
        }
    }
    
    /**
     * 添加，无动画效果
     *
     * @param item
     */
    @Keep
    public void addItem(ItemType item) {
        this.items.add(item);
        notifyDataSetChanged();
    }
    
    /**
     * 移除，无动画效果
     *
     * @param item
     */
    @Keep
    public void removeItem(ItemType item) {
        this.items.remove(item);
        notifyDataSetChanged();
    }
    
    //region UltimateViewAdapter
    
    
    //region Sticky
    @Override
    public ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new UltimateRecyclerviewViewHolder(parent);
    }
    
    @Override
    public void onBindHeaderViewHolder(ViewHolder holder, int position) {
        
    }
    
    @Override
    public long generateHeaderId(int position) {
        return -1;
    }
    //endregion
    
    
    @Override
    public int getAdapterItemCount() {
        return items.size();
    }
    
    @Override
    public ItemViewHolder newFooterHolder(View view) {
        return new ItemViewHolder(view);
    }
    
    @Override
    public ItemViewHolder newHeaderHolder(View view) {
        return new ItemViewHolder(view);
    }
    
    //endregion
    
    
    //region footer
    
    
    private static final int FOOTER = 2002;
    
    
    public static class FooterState {
        public static final boolean Loading = true;
        public static final boolean NoMore = false;
    }
    
    
    private boolean footerEnable;
    private boolean footerState = FooterState.Loading;
    private FooterBinding footerBinding;
    
    public boolean isFooterEnable() {
        return footerEnable;
    }
    
    private int getFooterViewType() {
        return FOOTER;
    }
    
    public void enableFooter(Context context) {
        if (footerBinding == null) {
            footerBinding = FooterBinding.inflate(LayoutInflater.from(context));
        }
        reenableFooter();
    }
    
    public void reenableFooter() {
        footerEnable = true;
        footerState = FooterState.Loading;
        notifyItemInserted(getItemCount() - 1);
    }
    
    
    public void disableFooter() {
        footerEnable = false;
        notifyItemRemoved(getItemCount() - 1);
    }
    
    /**
     * @param state {{@link FooterState}}
     */
    public void setFooterState(boolean state) {
        footerBinding.setFooterState(footerState = state);
    }
    
    public boolean getFooterState() {
        return footerState;
    }
    
    
    public boolean isEnableLoadMore() {
        return footerEnable;
    }
    
    //endregion
    
}
