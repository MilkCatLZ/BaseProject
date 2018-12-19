package com.base.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.*
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.base.R
import com.base.app.BaseApplicationInterface
import com.base.delegate.BaseDelegate
import com.base.interfaces.BaseInterface
import com.base.util.Log
import org.greenrobot.eventbus.EventBus
import java.lang.ClassCastException

abstract class BaseLazyFragment<App : BaseApplicationInterface> : Fragment(),BaseInterface{


    private val tcBaseDelegate = BaseDelegate()
    lateinit var app: App

    /**
     * 判断View是否被创建
     */
    private var isViewCreated: Boolean = false
    /**
     * 判断是否是第一次展示在界面上
     */
    private var isFirstVisible = true
    private var resumed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Fragment默认不覆盖ActionBar的标题
        tcBaseDelegate.setOverWriteTitle(false)
        try {
            app = getBaseApplicationInterface()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        tcBaseDelegate.onCreate(this, savedInstanceState, app)
    }

    protected abstract fun getBaseApplicationInterface(): App

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
        if (isFirstVisible && getUserVisibleHint()) {
            onVisibleToUser()
        }
    }

    override fun onResume() {
        super.onResume()
        tcBaseDelegate.onResume()
        if (isViewCreated && getUserVisibleHint()) {
            onViewCreatedResume()
        }
    }

    override fun onPause() {
        super.onPause()
        tcBaseDelegate.onPause()
        if (isViewCreated) {
            onRealPauseMothod()
        }
    }

    override fun onDestroy() {
        tcBaseDelegate.onDestroy(this)
        super.onDestroy()
    }

    fun BaseFragment() {
        arguments = Bundle(0)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        tcBaseDelegate.onOptionsItemSelected(item, this)
        return super.onOptionsItemSelected(item)
    }


    fun getFragmentName(): CharSequence {
        return ""
    }

    override fun getTitleText(): CharSequence {
        return getFragmentName()
    }

    @Keep
    fun getViewDataBind(): ViewDataBinding? {
        return null
    }

    /**
     * 为了兼容现有代码做出的妥协写法
     *
     * @return
     */
    override fun getViewDataBinds(): ViewDataBinding? {
        return getViewDataBind()
    }

    override fun getEventBusDefault(): EventBus {
        return tcBaseDelegate.eventBusDefault
    }

    override fun register(o: Any) {
        tcBaseDelegate.register(o)
    }

    override fun isActive(): Boolean {
        return tcBaseDelegate.isActive
    }

    override fun postSticky(o: Any) {
        tcBaseDelegate.postSticky(o)
    }

    override fun getSupportActionBar(): ActionBar? {
        return (activity as AppCompatActivity).supportActionBar
    }

    override fun setSupportActionBar(toolbar: Toolbar) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        tcBaseDelegate.setSupportActionBar(this)
    }


    override fun <T : View?> findViewById(id: Int): T? {
        return if (view != null) {
            val views = view!!.findViewById<View>(id)
            try {
                views as T
            } catch (e: ClassCastException) {
                null
            }
        } else null
    }

    override fun finish() {
        if (activity != null)
            activity!!.finish()
    }

    override fun getApplication(): Application {
        return activity!!.application
    }

    override fun setStatusBarColor(@ColorRes color: Int) {
        tcBaseDelegate.setStatusBarColor(activity!!, color)
    }


    @Keep
    fun setSupportActionBar(@IdRes toolBarID: Int) {
        if (view != null) {
            val toolbar = findViewById<Toolbar>(toolBarID)
            setSupportActionBar(toolbar!!)
            tcBaseDelegate.setSupportActionBar(this)
        }
    }

    override fun hideSoftKeyboard() {
        try {
            if (activity != null && !activity!!.isFinishing())
                (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                        .hideSoftInputFromWindow((activity!!.findViewById(android.R.id.content) as ViewGroup).getChildAt(0)
                                .windowToken,
                                InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (ignored: Exception) {

        }

    }

    override fun setHomeIndicator(@DrawableRes homeIndicator: Int) {
        tcBaseDelegate.setHomeIndicator(homeIndicator)
    }

    fun <A : Activity> startActivity(target: Class<A>) {
        startActivity(target, 0, true)
    }

    fun <A : Activity> startActivity(target: Class<A>, needAnim: Boolean?) {
        startActivity(target, 0, needAnim)
    }

    fun <A : Activity> startActivity(target: Class<A>, flags: Int, needAnim: Boolean?) {
        startActivity(Intent(getContext(), target).addFlags(flags))
        if (activity != null)
            if (needAnim != null && needAnim)
                activity!!.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left)
            else {
                activity!!.overridePendingTransition(-1, -1)
            }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isViewCreated && getUserVisibleHint()) {
            onVisibleToUser()
        } else if (isViewCreated && !getUserVisibleHint()) {
            onRealPauseMothod()
        }
    }

    private fun onRealPauseMothod() {
        if (isResumed) {
            resumed = false
            onRealPause()
            Log.d(javaClass.simpleName, "onRealPause")
        }
    }

    /**
     * View创建完成，并且每次从不可用变为可用时调用
     * [.onViewCreatedResume] 两个同时使用才能得到onActivityResume的效果
     * 如果不需要每次可见都调用，请用其他方法
     */
    private fun onVisibleToUser() {
        isFirstVisible = false
        if (!isResumed) {
            onRealResume()
            resumed = true
            Log.d(javaClass.simpleName, "onRealResume")
        }
    }

    /**
     * 在view创建完成后，再次调用Resume时的回调
     * [.onVisibleToUser] 两个同时使用才能得到onActivityResume的效果
     */
    private fun onViewCreatedResume() {
        if (!isResumed) {
            onRealResume()
            resumed = true
            Log.d(javaClass.simpleName, "onRealResume")
        }
    }

    /**
     * 下面这个方法为Fragment的真Resume效果
     */
    protected fun onRealResume() {

    }

    /**
     * 下面这个方法为Fragment的真onPause，每次从界面消失时触发
     */
    protected fun onRealPause() {

    }

    protected fun tintHomeAsUp(@ColorRes color: Int) {
        tcBaseDelegate.tintHomeAsUp(this, color)
    }

    protected fun tintHomeAsUpWithColor(@ColorInt color: Int) {
        tcBaseDelegate.tintHomeAsUpWithColor(this, color)
    }

}