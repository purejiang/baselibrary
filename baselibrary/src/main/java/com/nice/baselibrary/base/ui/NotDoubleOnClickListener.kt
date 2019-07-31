package com.nice.baselibrary.base.ui

import android.view.View

/**
 * @author JPlus
 * @date 2019/5/31.
 */
abstract class NotDoubleOnClickListener:View.OnClickListener {
    companion object {
    const val MIN_CLICK_DELAY_TIME = 2000
}
    private var lastClickTime = 0L

    override fun onClick(view: View) {
        if (System.currentTimeMillis() - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = System.currentTimeMillis()
            notDoubleOnClick(view)
        }
    }

    /**
     * 避免重复点击
     * @param view
     */
     abstract fun notDoubleOnClick(view:View)
}