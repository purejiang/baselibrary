package com.nice.baselibrary.widget.loading

import android.content.Context
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.nice.baselibrary.base.utils.LogUtils

/**
 * @author JPlus
 * @date 2020/4/17.
 */
class LoadingLayout(context: Context) : FrameLayout(context) {
    private var mLoadingView: View? = null
    private var mRetryView: View? = null
    private var mContentView: View? = null
    private var mEmptyView: View? = null


    private fun isMainThread(): Boolean {
        return Looper.myLooper() == Looper.getMainLooper()
    }


    fun showLoading() {
        post {
            showView(mLoadingView);
        }
    }

    fun showRetry() {
        post {
            showView(mRetryView);
        }
    }

    fun showContent() {
        post {
            showView(mContentView);
        }
    }

    fun showEmpty() {
        post {
            showView(mEmptyView);
        }
    }

    private fun showView(view: View?) {
        if (view == null) return
        when (view) {
            mLoadingView -> {
                mLoadingView?.visibility = View.VISIBLE
                setVisibilityGone(mContentView, mRetryView, mEmptyView)
            }
            mRetryView -> {
                mRetryView?.visibility = View.VISIBLE
                setVisibilityGone(mLoadingView, mContentView, mEmptyView)
            }
            mContentView -> {
                mContentView?.visibility = View.VISIBLE
                setVisibilityGone(mLoadingView, mRetryView, mEmptyView)
            }
            mEmptyView -> {
                setVisibilityGone(mLoadingView, mRetryView, mContentView)
                mEmptyView?.visibility = View.VISIBLE
            }
        }

    }

    private fun setVisibilityGone(vararg view: View?) {
        view.forEach {
            it?.visibility = GONE
        }
    }

    fun setContentView(layoutId: Int): View? {
        return setContentView(LayoutInflater.from(context).inflate(layoutId, this, false))
    }

    fun setLoadingView(layoutId: Int): View? {
        return setLoadingView(LayoutInflater.from(context).inflate(layoutId, this, false));
    }

    fun setEmptyView(layoutId: Int): View? {
        return setEmptyView(LayoutInflater.from(context).inflate(layoutId, this, false));
    }

    fun setRetryView(layoutId: Int): View? {
        return setRetryView(LayoutInflater.from(context).inflate(layoutId, this, false));
    }

    fun setLoadingView(view: View?): View? {
        if (mLoadingView != null) {
            LogUtils.w("you have already set a loading view and would be instead of this new one.")
        }
        this.removeView(mLoadingView)
        this.addView(view)
        mLoadingView = view
        return view
    }

    fun setEmptyView(view: View?): View? {
        if (mEmptyView != null) {
            LogUtils.w("you have already set a empty view and would be instead of this new one.");
        }
        this.removeView(mEmptyView);
        this.addView(view);
        mEmptyView = view;
        return view;
    }

    fun setRetryView(view: View?): View? {
        if (mRetryView != null) {
            LogUtils.w("you have already set a retry view and would be instead of this new one.");
        }
        this.removeView(mRetryView)
        this.addView(view)
        mRetryView = view
        return view
    }

    fun setContentView(view: View?): View? {
        if (mContentView != null) {
            LogUtils.w("you have already set a retry view and would be instead of this new one.");
        }
        this.removeView(mContentView)
        this.addView(view)
        mContentView = view
        return view
    }

    fun getRetryView(): View? {
        return mRetryView
    }

    fun getLoadingView(): View? {
        return mLoadingView
    }

    fun getContentView(): View? {
        return mContentView
    }

    fun getEmptyView(): View? {
        return mEmptyView
    }
}