package com.nice.baselibrary.base.utils

import android.content.Context

/**
 * 资源管理类
 * @author JPlus
 * @date 2019/3/28.
 */
class ResourceUtils private constructor(){
    companion object {
    val instance:ResourceUtils by lazy (mode = LazyThreadSafetyMode.SYNCHRONIZED){
        ResourceUtils()
    }
    }
    private var mContext:Context?=null

    fun init(context: Context){
        mContext =context
    }
}