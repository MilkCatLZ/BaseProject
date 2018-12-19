package com.base.base.view

import android.app.Dialog
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.*
import com.base.R

/**
 * Created by LZ on 2017/11/24.
 * 普通订单转预约订单时 用的世界选择器
 */

abstract class ShareDialog : DialogFragment() {

    interface ShareCallBack {
        fun onWXShareClick()
        fun onWXCircleShareClick()
        fun onQQShareClick()
    }

    lateinit var binding: ViewDataBinding

    var shareCallBack: ShareCallBack? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        var view: View = LayoutInflater.from(activity)
                .inflate(R.layout.layout_water_share_dialog, null)


        binding = DataBindingUtil.bind<ViewDataBinding>(view)!!
        binding.setVariable(getDialogBR(), this)


        val dialog = Dialog(activity, R.style.BottomDialog)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // 设置Content前设定
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(true) // 外部点击取消

        // 设置宽度为屏宽, 靠近屏幕底部。
        val window = dialog.window
        val lp = window!!.attributes
        lp.gravity = Gravity.BOTTOM // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT // 宽度持平
        window.attributes = lp

        return dialog
    }

    abstract fun getDialogBR(): Int


    fun onQQClick() {
        shareCallBack?.onQQShareClick()
        dismiss()
    }

    fun onWXClick() {
        shareCallBack?.onWXShareClick()
        dismiss()
    }

    fun onWXPClick() {
        shareCallBack?.onWXCircleShareClick()
        dismiss()
    }

//    inner class listener:UMShareListener
}