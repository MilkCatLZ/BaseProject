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

package com.base.base;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.base.R;
import com.base.app.BaseApplicationInterface;
import com.base.delegate.BaseDelegate;
import com.base.interfaces.BaseInterface;
import com.base.util.Log;

import org.greenrobot.eventbus.EventBus;


/**
 * 所有Fragment基类
 * Created by LZ on 15-12-7.
 */
public abstract class BaseFragment<App extends BaseApplicationInterface> extends Fragment implements BaseInterface {
    protected String tag = getClass().getSimpleName();

    private BaseDelegate tcBaseDelegate = new BaseDelegate();
    protected App app;

    /**
     * 判断View是否被创建
     */
    private boolean isViewCreated;
    /**
     * 判断是否是第一次展示在界面上
     */
    private boolean isFirstVisible = true;
    private boolean isResumed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fragment默认不覆盖ActionBar的标题
        tcBaseDelegate.setOverWriteTitle(false);
        try {
            app = getBaseApplicationInterface();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tcBaseDelegate.onCreate(this, savedInstanceState, app);
    }

    protected abstract App getBaseApplicationInterface();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        if (isFirstVisible && getUserVisibleHint()) {
            onVisibleToUser();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        tcBaseDelegate.onResume();
        if (isViewCreated && getUserVisibleHint()) {
            onViewCreatedResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        tcBaseDelegate.onPause();
        if (isViewCreated) {
            onRealPauseMothod();
        }
    }

    @Override
    public void onDestroy() {
        tcBaseDelegate.onDestroy(this);
        super.onDestroy();
    }

    public BaseFragment() {
        setArguments(new Bundle(0));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        tcBaseDelegate.onOptionsItemSelected(item, this);
        return super.onOptionsItemSelected(item);
    }


    public CharSequence getFragmentName() {
        return "";
    }

    @Override
    public CharSequence getTitleText() {
        return getFragmentName();
    }

    @Keep
    public ViewDataBinding getViewDataBind() {
        return null;
    }

    /**
     * 为了兼容现有代码做出的妥协写法
     *
     * @return
     */
    @Override
    public ViewDataBinding getViewDataBinds() {
        return getViewDataBind();
    }

    @Override
    public EventBus getEventBusDefault() {
        return tcBaseDelegate.getEventBusDefault();
    }

    @Override
    public void register(Object o) {
        tcBaseDelegate.register(o);
    }

    @Override
    public boolean isActive() {
        return tcBaseDelegate.isActive();
    }

    @Override
    public void postSticky(Object o) {
        tcBaseDelegate.postSticky(o);
    }

    @Override
    @Nullable
    public ActionBar getSupportActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        tcBaseDelegate.setSupportActionBar(this);
    }

    @Override
    @Nullable
    public View findViewById(@IdRes int id) {
        if (getView() != null) {
            return getView().findViewById(id);
        }
        return null;
    }

    @Override
    public void finish() {
        if (getActivity() != null)
            getActivity().finish();
    }

    @Override
    public Application getApplication() {
        return getActivity().getApplication();
    }

    @Override
    public void setStatusBarColor(@ColorRes int color) {
        tcBaseDelegate.setStatusBarColor(getActivity(), color);
    }


    @Keep
    public void setSupportActionBar(@IdRes int toolBarID) {
        if (getView() != null) {
            Toolbar toolbar = (Toolbar) findViewById(toolBarID);
            setSupportActionBar(toolbar);
            tcBaseDelegate.setSupportActionBar(this);
        }
    }

    @Override
    public void hideSoftKeyboard() {
        try {
            if (getActivity() != null && !getActivity().isFinishing())
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0)
                                        .getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception ignored) {

        }
    }

    @Override
    public void setHomeIndicator(@DrawableRes int homeIndicator) {
        tcBaseDelegate.setHomeIndicator(homeIndicator);
    }

    public final <A extends Activity> void startActivity(@NonNull Class<A> target) {
        startActivity(target, 0, true);
    }

    public final <A extends Activity> void startActivity(@NonNull Class<A> target, Boolean needAnim) {
        startActivity(target, 0, needAnim);
    }

    public final <A extends Activity> void startActivity(@NonNull Class<A> target, int flags, Boolean needAnim) {
        startActivity(new Intent(getContext(), target).addFlags(flags));
        if (getActivity() != null)
            if (needAnim != null && needAnim)
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            else {
                getActivity().overridePendingTransition(-1, -1);
            }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isViewCreated && getUserVisibleHint()) {
            onVisibleToUser();
        } else if (isViewCreated && !getUserVisibleHint()) {
            onRealPauseMothod();
        }
    }

    private void onRealPauseMothod() {
        if (isResumed) {
            isResumed = false;
            onRealPause();
            Log.d(getClass().getSimpleName(), "onRealPause");
        }
    }

    /**
     * View创建完成，并且每次从不可用变为可用时调用
     * {@link #onViewCreatedResume} 两个同时使用才能得到onActivityResume的效果
     * 如果不需要每次可见都调用，请用其他方法
     */
    private void onVisibleToUser() {
        isFirstVisible = false;
        if (!isResumed) {
            onRealResume();
            isResumed = true;
            Log.d(getClass().getSimpleName(), "onRealResume");
        }
    }

    /**
     * 在view创建完成后，再次调用Resume时的回调
     * {@link #onVisibleToUser} 两个同时使用才能得到onActivityResume的效果
     */
    private void onViewCreatedResume() {
        if (!isResumed) {
            onRealResume();
            isResumed = true;
            Log.d(getClass().getSimpleName(), "onRealResume");
        }
    }

    /**
     * 下面这个方法为Fragment的真Resume效果
     */
    protected void onRealResume() {

    }

    /**
     * 下面这个方法为Fragment的真onPause，每次从界面消失时触发
     */
    protected void onRealPause() {

    }

    protected void tintHomeAsUp(@ColorRes int color) {
        tcBaseDelegate.tintHomeAsUp(this, color);
    }

    protected void tintHomeAsUpWithColor(@ColorInt int color) {
        tcBaseDelegate.tintHomeAsUpWithColor(this, color);
    }
}
