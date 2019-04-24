package com.nice.baselibrary.base.ui.view.dialog


import android.content.DialogInterface
import android.support.v4.app.FragmentTransaction
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.nice.baselibrary.R
import com.nice.baselibrary.base.adapter.NiceAdapter

/**
 * 自定义dialog的工具类
 * @author JPlus
 * @date 2019/4/17.
 */
class NiceDialogController private constructor() {
    companion object {
        fun create(): NiceDialogController {
            return NiceDialogController()
        }
    }

    private var mFragmentTransaction: FragmentTransaction? = null
    private var mLayoutRes = 0
    private var mBackgroundRes = 0
    private var mDialogHeight = 0
    private var mDialogWidth = 0
    private var mDimAmount = 0.0f
    private var mGravity = 0
    private var mTag = ""
    private var mIds: IntArray? = null
    private var mIsCancelable = false
    private var mOrientation = 0
    private var mAnimationRes = 0
    private var mDialogView: View? = null
    private var mOnViewClickListener: NiceAlertDialog.OnViewClickListener? = null
    private var mOnBindViewListener: NiceAlertDialog.OnBindViewListener? = null
    private var mOnDismissListener: DialogInterface.OnDismissListener? = null
    private var mOnKeyListener: DialogInterface.OnKeyListener? = null


    private var mAdapter: NiceAdapter<*>? = null
    private var mListItemClickListener: NiceAdapter.ItemClickListener? = null
    private var mListRecyclerId = 0
    private var mListOrientation = 0

    /*
       Dialog通常用到的方法
    */
    fun getFragmentTransaction(): FragmentTransaction? {
        return mFragmentTransaction
    }

    fun getLayoutRes(): Int {
        return mLayoutRes
    }

    fun getTag(): String {
        return mTag
    }

    fun getGravity(): Int {
        return mGravity
    }

    fun getCancelable(): Boolean {
        return mIsCancelable
    }

    fun getAnimationRes(): Int {
        return mAnimationRes
    }

    fun getDialogHeight(): Int {
        return mDialogHeight
    }

    fun getDialogWidth(): Int {
        return mDialogWidth
    }

    fun getDimAmount(): Float {
        return mDimAmount
    }

    fun getBackgroundRes(): Int {
        return mBackgroundRes
    }

    fun getKeyListener(): DialogInterface.OnKeyListener? {
        return mOnKeyListener
    }

    fun getIds(): IntArray? {
        return mIds
    }

    /*
       普通Dialog用到的方法
    */
    fun getOnViewClickListener(): NiceAlertDialog.OnViewClickListener? {
        return mOnViewClickListener
    }

    fun getOnBindViewListener(): NiceAlertDialog.OnBindViewListener? {
        return mOnBindViewListener
    }

    /*
        列表Dialog用到的方法
    */

    fun getAdapter(): NiceAdapter<*>? {
        return mAdapter
    }

    fun getListItemClickListener(): NiceAdapter.ItemClickListener? {
        return mListItemClickListener
    }


    fun getListRecyclerId(): Int {
        return mListRecyclerId
    }

    fun getListOrientation(): Int {
        return mListOrientation
    }

    /**
     * 自定义dialog的参数类
     */
    class Params {
        var mFragmentTransaction: FragmentTransaction? = null
        var mLayoutRes = R.layout.view_dialog
        var mBackgroundRes = R.drawable.bg_circle_view
        var mDialogHeight = WindowManager.LayoutParams.WRAP_CONTENT
        var mDialogWidth = WindowManager.LayoutParams.WRAP_CONTENT
        var mDimAmount = 0.0f
        var mGravity = Gravity.CENTER
        var mTag = ""
        var mIds: IntArray? = null
        var mIsCancelable = false
        var mOrientation = 0
        var mAnimationRes = R.style.NiceDialogAnim
        var mDialogView: View? = null
        var mOnViewClickListener: NiceAlertDialog.OnViewClickListener? = null
        var mOnBindViewListener: NiceAlertDialog.OnBindViewListener? = null
        var mOnDismissListener: DialogInterface.OnDismissListener? = null
        var mOnKeyListener: DialogInterface.OnKeyListener? = null

        var mAdapter: NiceAdapter<*>? = null
        var mListItemClickListener: NiceAdapter.ItemClickListener? = null
        var mListRecyclerId = 0
        var mListOrientation = 0

        fun apply(niceDialogController: NiceDialogController?) {
            niceDialogController?.mFragmentTransaction = mFragmentTransaction
            niceDialogController?.mLayoutRes = mLayoutRes
            niceDialogController?.mDialogHeight = mDialogHeight
            niceDialogController?.mDialogWidth = mDialogWidth
            niceDialogController?.mDimAmount = mDimAmount
            niceDialogController?.mGravity = mGravity
            niceDialogController?.mTag = mTag
            niceDialogController?.mIds = mIds
            niceDialogController?.mIsCancelable = mIsCancelable
            niceDialogController?.mOrientation = mOrientation
            niceDialogController?.mAnimationRes = mAnimationRes
            niceDialogController?.mDialogView = mDialogView
            niceDialogController?.mOnViewClickListener = mOnViewClickListener
            niceDialogController?.mOnBindViewListener = mOnBindViewListener
            niceDialogController?.mOnDismissListener = mOnDismissListener
            niceDialogController?.mOnKeyListener = mOnKeyListener
            niceDialogController?.mBackgroundRes = mBackgroundRes

            niceDialogController?.mAdapter = mAdapter
            niceDialogController?.mListItemClickListener = mListItemClickListener
            niceDialogController?.mListOrientation = mListOrientation
            niceDialogController?.mListRecyclerId = mListRecyclerId

        }

    }


}
