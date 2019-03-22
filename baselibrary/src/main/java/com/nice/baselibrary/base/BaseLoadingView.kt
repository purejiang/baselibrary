package com.nice.baselibrary.base

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * @author Administrator
 * @date 2019/2/18.
 */
abstract class BaseLoadingView: View {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    abstract fun success()

    abstract fun failed()

    abstract fun pause()

    abstract fun cancel()

    abstract fun loading(progress:Double)



}