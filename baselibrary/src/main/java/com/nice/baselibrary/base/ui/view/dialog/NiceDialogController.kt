package com.nice.baselibrary.base.ui.view.dialog



import android.content.DialogInterface
import android.support.v4.app.FragmentTransaction
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.nice.baselibrary.R

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
    private var mLayoutRes: Int = 0
    private var mBackgroundRes :Int =0
    private var mDialogHeight: Int = 0
    private var mDialogWidth: Int = 0
    private var mDimAmount: Float = 0.0f
    private var mGravity: Int = 0
    private var mTag: String = ""
    private var mIds: IntArray? = null
    private var mIsCancelable: Boolean = false
    private var mOrientation: Int = 0
    private var mAnimationRes: Int = 0
    private var mDialogView: View? = null
    private var mOnViewClickListener: NiceAlertDialog.OnViewClickListener? = null
    private var mOnBindViewListener:NiceAlertDialog.OnBindViewListener?=null
    private var mOnDismissListener: DialogInterface.OnDismissListener? = null
    private var mOnKeyListener: DialogInterface.OnKeyListener? = null

    fun getFragmentTransaction(): FragmentTransaction? {
        return mFragmentTransaction
    }

    fun getLayoutRes(): Int {
        return mLayoutRes
    }

    fun getTag():String{
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

    fun getOnViewClickListener():NiceAlertDialog.OnViewClickListener?{
        return mOnViewClickListener
    }
    fun getOnBindViewListener():NiceAlertDialog.OnBindViewListener?{
        return mOnBindViewListener
    }

    /**
     * 自定义dialog的参数类
     */
    class Params {
         var mFragmentTransaction: FragmentTransaction? = null
         var mLayoutRes = R.layout.view_dialog
         var mBackgroundRes :Int = R.drawable.bg_circle_view
         var mDialogHeight: Int = WindowManager.LayoutParams.WRAP_CONTENT
         var mDialogWidth: Int = WindowManager.LayoutParams.WRAP_CONTENT
         var mDimAmount: Float = 0.0f
         var mGravity: Int = Gravity.CENTER
         var mTag: String = ""
         var mIds: IntArray? = null
         var mIsCancelable: Boolean = false
         var mOrientation: Int = 0
         var mAnimationRes: Int = R.style.NiceDialogAnim
         var mDialogView: View? = null
         var mOnViewClickListener: NiceAlertDialog.OnViewClickListener? = null
         var mOnBindViewListener: NiceAlertDialog.OnBindViewListener? = null
         var mOnDismissListener: DialogInterface.OnDismissListener? = null
         var mOnKeyListener: DialogInterface.OnKeyListener? = null

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
            niceDialogController?.mBackgroundRes =mBackgroundRes
        }

    }

}