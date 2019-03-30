package com.nice.baselibrary.base.view

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import com.nice.baselibrary.R

/**
 * @author JPlus
 * @date 2019/3/18.
 */
class NiceDialog : AlertDialog {
    private var mTitle: NiceTitleBar? = null
    private var mMessage: NiceTextView? = null
    private var mCancel: NiceTextView? = null
    private var mConfirm: NiceTextView? = null

    constructor(context: Context, themeResId: Int) : super(context) {
        init(context, themeResId)
    }

    private fun init(context: Context,themeResId:Int) {
        val layout: LayoutInflater = LayoutInflater.from(context)
        val view: View = layout.inflate(themeResId, null)
        setView(view)
        //去掉圆角四周的默认背景色
        window.setBackgroundDrawableResource(R.drawable.bg_circle_view)
        //去掉默认dialog弹出后的半透明背景
        window.setDimAmount(0f)

        mTitle = view.findViewById(R.id.bt_dialog_title)
        mMessage = view.findViewById(R.id.btv_dialog_message)
        mCancel = view.findViewById(R.id.btv_dialog_cancel)
        mConfirm = view.findViewById(R.id.btv_dialog_confirm)
        mCancel?.visibility = View.INVISIBLE
        mConfirm?.visibility = View.VISIBLE
    }

    fun setTitle(title: String): NiceDialog {
        mTitle?.setMainTitle(title)
        return this
    }

    fun setMessage(message: String): NiceDialog {
        mMessage?.text = message
        return this
    }

    fun setCancel(cancel: String, dialogCancelClickListener: DialogClickListener): NiceDialog {
        mCancel?.visibility = View.VISIBLE
        mCancel?.text = cancel
        mCancel?.setOnClickListener {
            dialogCancelClickListener.onClick()
            dismiss()
        }
        return this
    }
    fun setCanceled(cancel: Boolean):NiceDialog{
        this.setCancelable(cancel)
        return this
    }

    fun setConfirm(confirm: String, dialogConfirmClickListener: DialogClickListener): NiceDialog {
        mConfirm?.visibility = View.VISIBLE
        mConfirm?.text = confirm
        mConfirm?.setOnClickListener {
            dialogConfirmClickListener.onClick()
            dismiss()
        }
        return this
    }

    /**
     * 点击事件
     */
    interface DialogClickListener {
        fun onClick()
    }


}