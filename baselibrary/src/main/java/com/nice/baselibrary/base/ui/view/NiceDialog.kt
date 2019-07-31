package com.nice.baselibrary.base.ui.view

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import com.nice.baselibrary.R
import com.nice.baselibrary.base.utils.LogUtils

/**
 * 可修改弹出框
 * @author JPlus
 * @date 2019/3/18.
 */
class NiceDialog : AlertDialog {
    companion object {

        val DIALOG_NORMAL = "normal"
        val DIALOG_SMALLER = "smaller"
        val DIALOG_SMALL = "small"
        val DIALOG_BIG = "big"

    }
    private var mTitle: NiceTitleBar? = null
    private var mMessage: NiceTextView? = null
    private var mCancel: NiceTextView? = null
    private var mConfirm: NiceTextView? = null
    private var mProgress: NiceCircleProgress?=null

    /**
     * 构造函数
     * @param context 上下文
     * @param resInt dialog使用的布局
     * @param resIntArray 控件的id
     */
    constructor(context: Context, resInt:Int, resIntArray:Array<Int>, styleable:Int) : super(context, styleable) {
        init(context, resInt, resIntArray)
    }

    /**
     * 初始化
     * @param context
     * @param resInt
     * @param resIntArray
     */
    private fun init(context: Context, resInt:Int, resIntArray:Array<Int>) {
        val layout: LayoutInflater = LayoutInflater.from(context)
        val view: View = layout.inflate(resInt, null)
        setView(view)
        //去掉圆角四周的默认背景色
        window.setBackgroundDrawableResource(R.drawable.bg_circle_view)
        //去掉默认dialog弹出后的半透明背景
        window.setDimAmount(0f)

        mTitle = view.findViewById(resIntArray[0])
        mMessage = view.findViewById(resIntArray[1])
        mCancel = view.findViewById(resIntArray[2])
        mConfirm = view.findViewById(resIntArray[3])
        mProgress = view.findViewById(resIntArray[4])
    }

    /**
     * 设置圆形加载条
     * @param text 显示的加载提示文字
     * @return
     */
    fun setCircleProgress(text:String): NiceDialog {
        mProgress?.visibility = View.VISIBLE
        setConfirm(text, null)
        return this
    }
    /**
     * 设置弹出框标题
     * @param title 显示的标题文字
     * @return
     */
    fun setTitle(title: String): NiceDialog {
        mTitle?.visibility = View.VISIBLE
        mTitle?.setMainTitle(title)
        return this
    }
    /**
     * 设置弹出框消息
     * @param message 显示的加载提示文字
     * @return
     */
    fun setMessage(message: String): NiceDialog {
        mMessage?.visibility = View.VISIBLE
        mMessage?.text = message
        return this
    }
    /**
     * 设置弹出框取消按钮
     * @param cancel 显示的取消提示文字
     * @param dialogCancelClickListener 取消按钮的点击事件
     * @return
     */
    fun setCancel(cancel: String, dialogCancelClickListener: DialogClickListener?): NiceDialog {
        mCancel?.visibility = View.VISIBLE
        mCancel?.text = cancel
        mCancel?.setOnClickListener {
            dialogCancelClickListener?.onClick()
            dismiss()
        }
        return this
    }
    /**
     * 设置弹出框确认按钮
     * @param confirm 显示的确认提示文字
     * @param dialogConfirmClickListener 确认按钮的点击事件
     * @return
     */
    fun setConfirm(confirm: String, dialogConfirmClickListener: DialogClickListener?): NiceDialog {
        mConfirm?.visibility = View.VISIBLE
        mConfirm?.text = confirm
        mConfirm?.setOnClickListener {
            dialogConfirmClickListener?.onClick()
            dismiss()
        }
        return this
    }

    /**
     * 设置是否可取消
     * @param cancel 是否可以取消
     * @return
     */
    fun setCanceled(cancel: Boolean): NiceDialog {
        this.setCancelable(cancel)
        return this
    }

    override fun cancel() {
        super.cancel()
        LogUtils.getInstance().e("cancel")
        mProgress?.close()
    }

    override fun dismiss() {
        super.dismiss()
        LogUtils.getInstance().e("dismiss")
        mProgress?.close()
    }

    /**
     * 点击事件
     */
    interface DialogClickListener {
        fun onClick()
    }


}