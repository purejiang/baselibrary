package com.nice.baselibrary.base.view

import android.content.Context
import com.nice.baselibrary.R

/**
 * 弹出控件类
 * @author JPlus
 * @date 2019/2/12.
 */
class BaseShowView {
    companion object {
        private var mBaseShowView: BaseShowView?=null

        @Synchronized fun getInstance(): BaseShowView{
            if(mBaseShowView==null){
                mBaseShowView = BaseShowView()
            }
            return mBaseShowView!!
        }
    }

    /**
     * 创建一个弹出框
     * @param context
     */
    fun baseDialog(context: Context): NiceDialog {
       return  NiceDialog(context, R.layout.view_dialog).setCancel("取消", object : NiceDialog.DialogClickListener{
           override fun onClick() {

           }
       })
    }

}