package com.nice.baselibrary.base.utils

import android.content.Context

/**
 * 资源管理类
 * @author JPlus
 * @date 2019/3/28.
 */
class ResourceUtils private constructor(){
    companion object {
        private var mResourceUtils:ResourceUtils?=null
        fun getInstance():ResourceUtils{
            if(mResourceUtils==null){
                synchronized(ResourceUtils::class.java) {
                    mResourceUtils = ResourceUtils()
                }
            }
            return mResourceUtils!!
        }
    }
    private var mContext:Context?=null

    fun init(context: Context){
        mContext =context
    }
}