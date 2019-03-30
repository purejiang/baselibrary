package com.nice.baselibrary.base.view

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import com.nice.baselibrary.R

/**
 * 弹出控件类
 * @author JPlus
 * @date 2019/2/12.
 */
class NiceShowView {
    companion object {
        private var mNiceShowView: NiceShowView?=null

        @Synchronized fun getInstance(): NiceShowView {
            if(mNiceShowView ==null){
                mNiceShowView = NiceShowView()
            }
            return mNiceShowView!!
        }
    }
    private var mContext:Context?=null

    fun init(context: Context){
        mContext = context
    }

    /**
     * 创建一个弹出框
     * @param context
     */
    fun baseDialog(context: Context = mContext!!): NiceDialog {
       return  NiceDialog(context, R.layout.view_dialog)
    }
    fun showNormalToast(message:String, context: Context= mContext!!):Toast{
        return Toast.makeText(context, message, Toast.LENGTH_SHORT)
    }

    fun showGravityToast(message:String, context: Context= mContext!!):Toast{
        val toast = showNormalToast(message, context)
        toast.setGravity(Gravity.CENTER, 0, 0)
        return toast
    }
}