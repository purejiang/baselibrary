package com.nice.baselibrary.widget.dialog


import android.content.DialogInterface
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nice.baselibrary.R
import com.nice.baselibrary.base.adapter.NiceAdapter

/**
 * 自定义dialog的工具类
 * @author JPlus
 * @date 2019/4/17.
 */
class JDialogController private constructor() {
    companion object {
        fun create(): JDialogController {
            return JDialogController()
        }
    }

    private var mTag = ""
    private var mGravity = 0
    private var mLayoutRes = 0
    private var mOrientation = 0
    var mDialogHeight = 0
    var mDialogWidth = 0
    private var mDialogWidthPercent = -1f
    private var mDialogHeightPercent = -1f

    private var mDimAmount = 0.0f
    private var mBackgroundRes = 0
    private var mListRecyclerId = 0
    private var mListOrientation = 0
    private var mIsCancelable = false

    private var mIds: IntArray? = null
    private var mAnimationRes:Int?=null
    private var mDialogView: View? = null
    private var mAdapter: NiceAdapter<*>? = null
    private var mFragmentManager: FragmentManager? = null
    private var mOnKeyListener: DialogInterface.OnKeyListener? = null
    private var mOnViewClickListener: JAlertDialog.OnViewClickListener? = null
    private var mOnBindViewListener: JAlertDialog.OnBindViewListener? = null
    private var mOnDismissListener: DialogInterface.OnDismissListener? = null
    private var mListItemClickListener: NiceAdapter.ItemClickListener? = null


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
    fun getOnViewClickListener(): JAlertDialog.OnViewClickListener? {
        return mOnViewClickListener
    }

    fun getOnBindViewListener(): JAlertDialog.OnBindViewListener? {
        return mOnBindViewListener
    }

    fun getOnDismissListener():DialogInterface.OnDismissListener?{
        return  mOnDismissListener
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
        var mLayoutRes = R.layout.view_dialog
        var mBackgroundRes = R.drawable.bg_circle_view
        var mDialogWidthPercent = -1f
        var mDialogHeightPercent = -1f
        var mDialogHeight = 0
        var mDialogWidth = 0
        var mTag = ""
        var mDimAmount = 0.0f
        var mOrientation = 0
        var mListRecyclerId = 0
        var mListOrientation = 0
        var mIsCancelable = false
        var mGravity = Gravity.CENTER

        var mIds: IntArray? = null
        var mAnimationRes:Int?=null
        var mDialogView: View? = null
        var mAdapter: NiceAdapter<*>? = null
        var mFragmentManager: FragmentManager? = null
        var mOnKeyListener: DialogInterface.OnKeyListener? = null
        var mOnViewClickListener: JAlertDialog.OnViewClickListener? = null
        var mOnBindViewListener: JAlertDialog.OnBindViewListener? = null
        var mOnDismissListener: DialogInterface.OnDismissListener? = null
        var mListItemClickListener: NiceAdapter.ItemClickListener? = null


        fun apply(jDialogController: JDialogController?) {
            jDialogController?.mFragmentManager = mFragmentManager
            jDialogController?.mLayoutRes = mLayoutRes
            jDialogController?.mDialogHeightPercent = mDialogHeightPercent
            jDialogController?.mDialogWidthPercent = mDialogWidthPercent
            jDialogController?.mDimAmount = mDimAmount
            jDialogController?.mGravity = mGravity
            jDialogController?.mTag = mTag
            jDialogController?.mIds = mIds
            jDialogController?.mIsCancelable = mIsCancelable
            jDialogController?.mOrientation = mOrientation
            jDialogController?.mAnimationRes = mAnimationRes
            jDialogController?.mDialogView = mDialogView
            jDialogController?.mOnViewClickListener = mOnViewClickListener
            jDialogController?.mOnBindViewListener = mOnBindViewListener
            jDialogController?.mOnDismissListener = mOnDismissListener
            jDialogController?.mOnKeyListener = mOnKeyListener
            jDialogController?.mBackgroundRes = mBackgroundRes

            jDialogController?.mAdapter = mAdapter
            jDialogController?.mListItemClickListener = mListItemClickListener
            jDialogController?.mListOrientation = mListOrientation
            jDialogController?.mListRecyclerId = mListRecyclerId

        }

    }


}
