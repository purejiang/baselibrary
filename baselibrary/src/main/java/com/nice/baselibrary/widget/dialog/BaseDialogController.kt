package com.nice.baselibrary.widget.dialog


import android.content.DialogInterface
import android.view.Gravity
import android.view.View
import androidx.fragment.app.FragmentManager
import com.nice.baselibrary.R
import com.nice.baselibrary.base.adapter.BaseAdapter

/**
 * 自定义dialog的工具类
 * @author JPlus
 * @date 2019/4/17.
 */
class BaseDialogController private constructor() {
    companion object {
        fun create(): BaseDialogController {
            return BaseDialogController()
        }
    }

    private var mTag = ""
    private var mGravity = 0
    private var mLayoutRes = 0
    private var mOrientation = 0
    private var mDialogHeight = 0
    private var mDialogWidth = 0
    private var mDialogWidthPercent = -1f
    private var mDialogHeightPercent = -1f

    private var mDimAmount = 0.0f
    private var mBackgroundRes = 0
    private var mIsCancelable = false

    private var mIds: IntArray? = null
    private var mAnimationRes:Int?=null
    private var mDialogView: View? = null
    private var mAdapter: BaseAdapter<*>? = null
    private var mFragmentManager: FragmentManager? = null
    private var mOnKeyListener: DialogInterface.OnKeyListener? = null
    private var mOnViewClickListener: BaseAlertDialog.OnViewClickListener? = null
    private var mOnBindViewListener: BaseAlertDialog.OnBindViewListener? = null
    private var mOnDismissListener: DialogInterface.OnDismissListener? = null


    /*
       Dialog通常用到的方法
    */
    fun getFragmentManager(): FragmentManager? {
        return mFragmentManager
    }

    fun getLayoutRes(): Int {
        return mLayoutRes
    }

    fun getTag(): String {
        return mTag
    }
    fun getDialogView():View?{
        return mDialogView
    }

    fun getGravity(): Int {
        return mGravity
    }

    fun getCancelable(): Boolean {
        return mIsCancelable
    }

    fun getAnimationRes(): Int? {
        return mAnimationRes
    }

    fun getDialogHeightPercent(): Float {
        return mDialogHeightPercent
    }

    fun getDialogWidthPercent(): Float {
        return mDialogWidthPercent
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
    fun getOnViewClickListener(): BaseAlertDialog.OnViewClickListener? {
        return mOnViewClickListener
    }

    fun getOnBindViewListener(): BaseAlertDialog.OnBindViewListener? {
        return mOnBindViewListener
    }

    fun getOnDismissListener():DialogInterface.OnDismissListener?{
        return  mOnDismissListener
    }
    /**
     * 自定义dialog的参数类
     */
    class Params {
        var mLayoutRes = R.layout.view_dialog
        var mBackgroundRes = R.drawable.bg_circle_view
        var mDialogWidthPercent = -1f
        var mDialogHeightPercent = -1f
        var mDialogHeight = 0
        var mDialogWidth = 0
        var mTag = ""
        var mDimAmount = 0.0f
        var mOrientation = 0
        var mIsCancelable = false
        var mGravity = Gravity.CENTER

        var mIds: IntArray? = null
        var mAnimationRes:Int?=null
        var mDialogView: View? = null
        var mAdapter: BaseAdapter<*>? = null
        var mFragmentManager: FragmentManager? = null
        var mOnKeyListener: DialogInterface.OnKeyListener? = null
        var mOnViewClickListener: BaseAlertDialog.OnViewClickListener? = null
        var mOnBindViewListener: BaseAlertDialog.OnBindViewListener? = null
        var mOnDismissListener: DialogInterface.OnDismissListener? = null


        fun apply(baseDialogController: BaseDialogController?) {
            baseDialogController?.mFragmentManager = mFragmentManager
            baseDialogController?.mLayoutRes = mLayoutRes
            baseDialogController?.mDialogHeightPercent = mDialogHeightPercent
            baseDialogController?.mDialogWidthPercent = mDialogWidthPercent
            baseDialogController?.mDimAmount = mDimAmount
            baseDialogController?.mGravity = mGravity
            baseDialogController?.mTag = mTag
            baseDialogController?.mIds = mIds
            baseDialogController?.mIsCancelable = mIsCancelable
            baseDialogController?.mOrientation = mOrientation
            baseDialogController?.mAnimationRes = mAnimationRes
            baseDialogController?.mDialogView = mDialogView
            baseDialogController?.mOnViewClickListener = mOnViewClickListener
            baseDialogController?.mOnBindViewListener = mOnBindViewListener
            baseDialogController?.mOnDismissListener = mOnDismissListener
            baseDialogController?.mOnKeyListener = mOnKeyListener
            baseDialogController?.mBackgroundRes = mBackgroundRes

            baseDialogController?.mAdapter = mAdapter

        }

    }


}
