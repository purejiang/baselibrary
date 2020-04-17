package com.nice.baselibrary.widget.loading

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.fragment.app.Fragment


/**
 * @author JPlus
 * @date 2020/4/17.
 */
class LoadingManager(private val container: Any, private val listener: LoadingListener) {
    companion object {
        val NO_LAYOUT_ID = -1
        var DEFAULT_LOADING_RES = NO_LAYOUT_ID
        var DEFAULT_RETRY_RES = NO_LAYOUT_ID
        var DEFAULT_EMPTY_RES = NO_LAYOUT_ID
    }

    var mLoadingLayout: LoadingLayout? = null

    init {
        when (container) {
            is Activity -> {
                setStartView(container, container.findViewById<View>(android.R.id.content) as ViewGroup)
            }
            is Fragment -> {
                container.activity?.let {
                    setStartView(it, container.view?.parent as ViewGroup)
                }
            }
            is View -> {
                setStartView(container.context, container.parent as ViewGroup)
            }
            else -> {
                throw IllegalArgumentException("the argument's type must be Fragment or Activity: init(context)")
            }
        }
    }

    private fun setStartView(context: Context, contentParent: ViewGroup) {
        var index = 0
        val oldContent: View
        val layout = LoadingLayout(context)
        if (container is View) {
            oldContent = container
            contentParent.forEach {
                if (it === oldContent) return@forEach
                index++
            }
        } else {
            oldContent = contentParent.getChildAt(0)
        }
        //先移除旧的视图
        contentParent.removeView(oldContent)
        //设置loadingLayout到旧的视图位置，使用旧的视图的参数
        contentParent.addView(layout, index, oldContent.layoutParams)
        layout.setContentView(oldContent)
        // 设置
        setupLoadingLayout(listener, layout)
        setupRetryLayout(listener, layout)
        setupEmptyLayout(listener, layout)
        //callback
        listener.setRetryEvent(layout.getRetryView())
        listener.setLoadingEvent(layout.getLoadingView())
        listener.setEmptyEvent(layout.getEmptyView())
        mLoadingLayout = layout
    }

    private fun setupEmptyLayout(listener: LoadingListener, loadingLayout: LoadingLayout) {
        listener.generateEmptyLayoutId().let {
            if (it != NO_LAYOUT_ID) {
                loadingLayout.setLoadingView(it)
            } else {
                loadingLayout.setRetryView(listener.generateEmptyView())
            }
        }
    }

    private fun setupLoadingLayout(listener: LoadingListener, loadingLayout: LoadingLayout) {
        listener.generateLoadingLayoutId().let {
            if (it != NO_LAYOUT_ID) {
                loadingLayout.setLoadingView(it)
            } else {
                loadingLayout.setRetryView(listener.generateLoadingView())
            }
        }
    }

    private fun setupRetryLayout(listener: LoadingListener, loadingLayout: LoadingLayout) {
        listener.generateRetryLayoutId().let {
            if (it != NO_LAYOUT_ID) {
                loadingLayout.setRetryView(it)
            } else {
                loadingLayout.setRetryView(listener.generateRetryView())
            }
        }
    }


    fun showLoading() {
        mLoadingLayout?.showLoading()
    }

    fun showRetry() {
        mLoadingLayout?.showRetry()
    }

    fun showContent() {
        mLoadingLayout?.showContent()
    }

    fun showEmpty() {
        mLoadingLayout?.showEmpty()
    }
}