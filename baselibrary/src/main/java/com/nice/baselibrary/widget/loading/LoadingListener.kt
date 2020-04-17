package com.nice.baselibrary.widget.loading

import android.view.View

/**
 * @author JPlus
 * @date 2020/4/17.
 */
abstract class LoadingListener {
    open fun setRetryEvent(retryView: View?) {}
    open fun setLoadingEvent(loadingView: View?) {}
    open fun setEmptyEvent(emptyView: View?) {}

    open fun generateLoadingLayoutId(): Int {
        return LoadingManager.DEFAULT_LOADING_RES
    }

    open fun generateRetryLayoutId(): Int {
        return LoadingManager.DEFAULT_RETRY_RES
    }

    open fun generateEmptyLayoutId(): Int {
        return LoadingManager.DEFAULT_EMPTY_RES
    }

    open fun generateLoadingView(): View? {
        return null
    }

    open fun generateRetryView(): View? {
        return null
    }

    open fun generateEmptyView(): View? {
        return null
    }

    fun isSetLoadingLayout(): Boolean {
        return generateLoadingLayoutId() != LoadingManager.NO_LAYOUT_ID || generateLoadingView() != null
    }

    fun isSetRetryLayout(): Boolean {
        return generateRetryLayoutId() != LoadingManager.NO_LAYOUT_ID || generateRetryView() != null
    }

    fun isSetEmptyLayout(): Boolean {
        return generateEmptyLayoutId() != LoadingManager.NO_LAYOUT_ID || generateEmptyView() != null
    }
}