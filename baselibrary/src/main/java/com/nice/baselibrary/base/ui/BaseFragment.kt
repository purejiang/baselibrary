package com.nice.baselibrary.base.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nice.baselibrary.base.utils.LogUtils

/**
 * @author JPlus
 * @date 2019/1/16.
 */
abstract class BaseFragment : Fragment() {
    /**
     * 获取Fragment的布局
     * @param view
     * @param bundle
     */
    abstract fun getInitView(view: View?, bundle:Bundle?)

    /**
     * 获取布局
     * @return
     */
    abstract fun getLayoutId():Int

    /**
     * 点击事件
     *
     */
    abstract fun bindListener()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        LogUtils.d("Fragment:[${this.javaClass.simpleName}] --onAttach()")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.d("Fragment:[${this.javaClass.simpleName}] --onCreate()")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayoutId(), container, false)
        LogUtils.d("Fragment:[${this.javaClass.simpleName}] --onCreateView()")
        getInitView(view, savedInstanceState)

        return view
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindListener()//如果写在onCreateView中会因为view还没有返回导致不能直接使用布局id，或者可以直接用findViewById则不会出现此问题
        LogUtils.d("Fragment:[${this.javaClass.simpleName}] --onActivityCreated()")
    }

    override fun onStart() {
        super.onStart()
        LogUtils.d("Fragment:[${this.javaClass.simpleName}] --onStart()")
    }

    override fun onResume() {
        super.onResume()
        LogUtils.d("Fragment:[${this.javaClass.simpleName}] --onResume()")
    }

    override fun onPause() {
        super.onPause()
        LogUtils.d("Fragment:[${this.javaClass.simpleName}] --onPause()")
    }


    override fun onStop() {
        super.onStop()
        LogUtils.d("Fragment:[${this.javaClass.simpleName}] --onStop()")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LogUtils.d("Fragment:[${this.javaClass.simpleName}] --onDestroyView()")
    }
    override fun onDestroy() {
        super.onDestroy()
        LogUtils.d("Fragment:[${this.javaClass.simpleName}] --onDestroy()")
    }

    override fun onDetach() {
        super.onDetach()
        LogUtils.d("Fragment:[${this.javaClass.simpleName}] --onDetach()")
    }


}