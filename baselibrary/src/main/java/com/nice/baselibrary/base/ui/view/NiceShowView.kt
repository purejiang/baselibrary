package com.nice.baselibrary.base.ui.view

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import com.nice.baselibrary.R
import com.nice.baselibrary.base.ui.view.dialog.NiceDialog

/**
 * 弹出控件类
 * @author JPlus
 * @date 2019/2/12.
 */
class NiceShowView {
    companion object {
        private var mNiceShowView: NiceShowView? = null

        @Synchronized
        fun getInstance(): NiceShowView {
            if (mNiceShowView == null) {
                mNiceShowView = NiceShowView()
            }
            return mNiceShowView!!
        }
    }

    private var mContext: Context? = null

    fun init(context: Context) {
        mContext = context
    }

    /**
     * 创建一个弹出框
     * @param context
     * @param resInt
     * @param resIntArray
     * @param styleable
     */
    fun createDialog(context: Context,  resInt: Int ,resIntArray: Array<Int>, styleable: Int): NiceDialog {
        return NiceDialog(context, resInt, resIntArray, styleable)
    }
    /**
     * 创建一个弹出框
     * @param context
     * @param resInt
     * @param resIntArray
     * @param size dialog大小样式
     */
    fun createDialog(context: Context,  resInt: Int ,resIntArray: Array<Int>, size: String): NiceDialog {
        var styleable = R.style.NormalNiceDialog
        when(size){
            "normal"-> styleable = R.style.NormalNiceDialog
            "small"-> styleable = R.style.SmallNiceDialog
            "big" -> styleable = R.style.BigNiceDialog
            "smaller" -> styleable = R.style.SmallerNiceDialog
        }
        return NiceDialog(context, resInt, resIntArray, styleable)
    }

    /**
     * 创建一个弹出框，使用默认布局
     * @param context
     * @param size
     */
    fun createDialog(context: Context = mContext!!, size:String): NiceDialog {
        val resInt = R.layout.view_dialog
        val resIntArray = arrayOf(R.id.bt_dialog_title, R.id.btv_dialog_message, R.id.btv_dialog_cancel, R.id.btv_dialog_confirm, R.id.cp_loading)
        return createDialog(context, resInt, resIntArray, size)
    }

    fun NormalToast(message: String, context: Context = mContext!!): Toast {
        return Toast.makeText(context, message, Toast.LENGTH_SHORT)
    }

    fun GravityToast(message: String, context: Context = mContext!!): Toast {
        val toast = NormalToast(message, context)
        toast.setGravity(Gravity.CENTER, 0, 0)
        return toast
    }

    fun showProgressDialog() {


    }
}