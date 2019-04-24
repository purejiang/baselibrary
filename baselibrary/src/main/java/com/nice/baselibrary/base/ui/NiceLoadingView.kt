package com.nice.baselibrary.base.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * 加载型view基类
 * @author JPlus
 * @date 2019/2/18.
 */
abstract class NiceLoadingView : View {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * 加载成功
     */
    abstract fun success()
    /**
     * 加载失败
     */
    abstract fun failed()
    /**
     * 加载暂停
     */
    abstract fun pause()
    /**
     * 加载取消
     */
    abstract fun cancel()
    /**
     * 加载中
     * @param progress
     */
    abstract fun loading(progress:Double)



}