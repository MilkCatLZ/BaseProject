package com.base.base;


import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.base.app.BaseApplicationInterface;
import com.base.databinding.DataBindingAdapter;
import com.base.databinding.DataBindingAdapter.FooterState;
import com.base.widget.UltimateRecyclerView;
import com.base.widget.UltimateRecyclerView.OnLoadMoreListener;
import com.marshalchen.ultimaterecyclerview.uiUtils.RecyclerViewPositionHelper;

import jp.wasabeef.recyclerview.animators.FadeInAnimator;


/**
 * Created by liuz on 16-1-11.
 * 用于在使用SuperRecyclerView的Fragment统一处理刷新和下一页的逻辑，继承的子fragment就不用重新写这些代码了
 */
public abstract class BaseUltimateRecyclerViewFragment<App extends BaseApplicationInterface> extends BaseFragment<App> implements OnRefreshListener, OnLoadMoreListener {


    public static final int MAX_LOAD_MORE_COUNT = 0;
    //    public static final int MAX = 2;
    private boolean isGettingMore = false;
    private RecyclerViewPositionHelper mRecyclerViewHelper;
//    private ViewNetworkErrorBinding errorBinding;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getUltimateRecyclerView() == null) {
            throw new NullPointerException("no SuperRecycler found!!!");
        }
        initRecyclerView();
        mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(getUltimateRecyclerView().mRecyclerView);
        getUltimateRecyclerView().setOnLoadMoreListener(this);
        getUltimateRecyclerView().mSwipeRefreshLayout.setDistanceToTriggerSync(220);
        getUltimateRecyclerView().setDefaultOnRefreshListener(this);
    }

    @Keep
    protected void initRecyclerView() {

        getUltimateRecyclerView().setLayoutManager(getLayoutManager());

        getUltimateRecyclerView().setItemAnimator(new FadeInAnimator(new OvershootInterpolator(0.5f)));
        getUltimateRecyclerView().getItemAnimator()
                .setAddDuration(350);
        getUltimateRecyclerView().getItemAnimator()
                .setRemoveDuration(0);
        //加载更多
        getUltimateRecyclerView().addOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                DataBindingAdapter adapter = (DataBindingAdapter) recyclerView.getAdapter();

                LayoutManager layoutManager = recyclerView.getLayoutManager();
                int mTotalItemCount = layoutManager.getItemCount();
                int mVisibleItemCount = layoutManager.getChildCount();
                int mFirstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();

                boolean bottomEdgeHit = (mTotalItemCount - mVisibleItemCount) <= mFirstVisibleItem;

                if (bottomEdgeHit) {
                    if (adapter != null && adapter.isEnableLoadMore() && adapter.getFooterState()) {
                        if (!isGettingMore) {
                            loadMore(adapter.getAdapterItemCount(), mVisibleItemCount);
                            isGettingMore = true;
                        }
                    }
                }
            }
        });

    }

    LinearLayoutManager linearLayoutManager;

    /**
     * 获取LayoutManager，子类需要其他Manager的重写这个方法就可以
     *
     * @return LayoutManager
     */
    protected LayoutManager getLayoutManager() {
//        if (linearLayoutManager == null) {
        return linearLayoutManager = new LinearLayoutManager(getActivity());
//        }
//        return linearLayoutManager;
    }


    //region 刷新和加载更多,以及Footer实现

    /**
     * 子类刷新/加载完成时调用，隐藏loading进度
     * 加载更多完成后也一定要调用这个
     * 加载更多完成后也一定要调用这个
     * 加载更多完成后也一定要调用这个
     * 重要的事情说三遍
     * 否则会出现底部状态不对的问题！！
     */
    protected void refreshOrLoadMoreComplete() {
        try {
            if (getUltimateRecyclerView() != null) {
                getUltimateRecyclerView().setRefreshing(false);
                if (getAdapter() == null || getAdapter().getAdapterItemCount() == 0) {
                    getUltimateRecyclerView().showEmptyView();
                } else {
                    getUltimateRecyclerView().hideEmptyView();
                }
            }
            isGettingMore = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用这个方法可以触发头部的刷新效果
     * 建议使用这个方法刷新
     */
    @Override
    public void onRefresh() {
        try {
            if (!getUltimateRecyclerView().mSwipeRefreshLayout.isRefreshing()) {
                getUltimateRecyclerView().mSwipeRefreshLayout.setRefreshing(true);
            }
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * RecyclerView滑动到底部，并且EnableFooter的情况下，触发
     *
     * @param itemsCount
     * @param maxLastVisiblePosition
     */
    @Override
    public void loadMore(int itemsCount, int maxLastVisiblePosition) {
        try {
            if (itemsCount < getTotal()) {
                nextPage();
                setFooterLoading();
            } else {
                setFooterNoMore();
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    /**
     * 显示footer，打开后footer有loading和到底了两种情况
     */
    protected void enableLoadMore() {
        try {
            if (getAdapter() != null && !getAdapter().isEnableLoadMore()) {
                getAdapter().enableFooter(getActivity());
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    /**
     * 隐藏footer
     */
    protected void disableLoadMore() {

        try {
            if (getAdapter() != null) {
                getAdapter().disableFooter();
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    /**
     * footer设置成loading状态
     */
    protected void setFooterLoading() {

        try {
            if (getAdapter() != null) {
                if (!getAdapter().isEnableLoadMore()) {
                    enableLoadMore();
                }
                if (getAdapter().getAdapterItemCount() <= MAX_LOAD_MORE_COUNT) {
                    disableLoadMore();
                } else {
                    getAdapter().setFooterState(FooterState.Loading);
                }
            }
            refreshOrLoadMoreComplete();
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    /**
     * footer设置成到底了，无数据
     */
    protected void setFooterNoMore() {
        try {
            if (getAdapter() != null) {
                if (getAdapter().getAdapterItemCount() <= MAX_LOAD_MORE_COUNT) {
                    disableLoadMore();
                } else {
                    if (!getAdapter().isEnableLoadMore()) {
                        enableLoadMore();
                    }
                    getAdapter().setFooterState(FooterState.NoMore);
                }
            }
            refreshOrLoadMoreComplete();
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    /**
     * @param hasMore true:footer设置为loading状态；false:没有更多
     */
    protected void refreshFooterState(boolean hasMore) {
        if (hasMore) {
            setFooterNoMore();
        } else {
            setFooterLoading();
        }
        refreshOrLoadMoreComplete();
    }
    //endregion

    //region 子类实现

    /**
     * 下拉刷新回调
     */
    protected abstract void refresh();

    /**
     * 获得列表总数，用于判断是否要加载下一页
     *
     * @return 总数
     */
    protected abstract int getTotal();

    /**
     * 滑动到底部，加载下一页回调
     */
    protected abstract void nextPage();

    /**
     * 获取SuperRecyclerView实例
     *
     * @return 返回界面的SuperRecyclerView的实例
     */
    protected abstract UltimateRecyclerView getUltimateRecyclerView();

    /**
     * @return 返回用来设置footer的Adapter
     */
    protected abstract DataBindingAdapter getAdapter();
    //endregion
}
