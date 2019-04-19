package com.nice.baselibrary.base.ui.view.dialog


import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.View
import com.nice.baselibrary.base.adapter.NiceAdapter


/**
 * @author JPlus
 * @date 2019/4/17.
 */

class NiceAlertDialog : NiceDialogFragment() {
    private var mController: NiceDialogController? = null

    init {
        mController = NiceDialogController.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun bindView(view: View) {
        val viewHolder = NiceAdapter.VH(view)
        mController!!.getIds()?.forEach {
            val view2 = viewHolder.getView<View>(it)
            view2.isClickable =true
            view2.setOnClickListener {
                this.getViewClickListener()?.onClick(viewHolder, view2, this)
            }
            }
        this.getBindViewListener()?.onBindView(viewHolder)

    }

    override fun getLayoutRes(): Int {
        return mController!!.getLayoutRes()
    }

    override fun getGravity(): Int {
        return mController!!.getGravity()
    }

    override fun getCancelable(): Boolean {
        return mController!!.getCancelable()
    }

    override fun getAnimationRes(): Int {
        return mController!!.getAnimationRes()
    }

    override fun getDialogHeight(): Int {
        return mController!!.getDialogHeight()
    }

    override fun getDialogWidth(): Int {
        return mController!!.getDialogWidth()
    }

    override fun getDimAmount(): Float {
        return mController!!.getDimAmount()
    }

    override fun getKeyListener(): DialogInterface.OnKeyListener? {
        return mController!!.getKeyListener()
    }
    override fun getBackgroundDrawableRes():Int{
        return mController!!.getBackgroundRes()
    }

    private fun getViewClickListener(): NiceAlertDialog.OnViewClickListener? {
        return mController!!.getOnViewClickListener()
    }
    private fun getBindViewListener(): NiceAlertDialog.OnBindViewListener? {
        return mController!!.getOnBindViewListener()
    }


    fun show(): NiceAlertDialog {
        try {
            mController?.getFragmentTransaction()?.let {
                it.add(this, mController?.getTag())
                it.commitAllowingStateLoss()
            }

        } catch (e: Exception) {

        }
        return this
    }

    interface OnViewClickListener {
        fun onClick(viewHolder: NiceAdapter.VH, view: View, dialog: NiceAlertDialog)
    }
    interface OnBindViewListener {
        fun onBindView(viewHolder: NiceAdapter.VH)
    }


    class Builder(mFragmentTransaction: FragmentTransaction) {

        private var params: NiceDialogController.Params? = null

        init {
            params = NiceDialogController.Params()
            params?.mFragmentTransaction = mFragmentTransaction
        }

        fun setLayoutRes(layoutRes: Int): Builder {
            params?.mLayoutRes = layoutRes
            return this
        }

        fun setGravity(gravity: Int): Builder {
            params?.mGravity = gravity
            return this
        }

        fun setCancelable(isCancelable: Boolean): Builder {
            params?.mIsCancelable = isCancelable
            return this
        }

        fun setTag(tag: String): Builder {
            params?.mTag = tag
            return this
        }

        fun setAnimationRes(animationRes: Int): Builder {
            params?.mAnimationRes = animationRes
            return this
        }

        fun setScreenWidthPercent(context: Context, percent: Float): Builder {
            params?.mDialogWidth = (getScreenWidth(context) * percent).toInt()
            return this
        }

        fun setScreenHeightPercent(context: Context, percent: Float): Builder {
            params?.mDialogHeight = (getScreenHeight(context) * percent).toInt()
            return this
        }
        fun setBackgroundRes(backgroundRes: Int): Builder {
            params?.mBackgroundRes = backgroundRes
            return this
        }

        fun setDialogHeight(height: Int): Builder {
            params?.mDialogHeight = height
            return this
        }

        fun setDialogWidth(width: Int): Builder {
            params?.mDialogWidth = width
            return this
        }

        fun setDimAmount(amount: Float): Builder {
            params?.mDimAmount = amount
            return this
        }

        fun setKeyListener(onKeyListener: DialogInterface.OnKeyListener): Builder {
            params?.mOnKeyListener = onKeyListener
            return this
        }

        fun addClickedId(vararg ids: Int): Builder {
            params?.mIds = ids
            return this
        }

        fun setViewClickListener(clickListener: NiceAlertDialog.OnViewClickListener): Builder {
            params?.mOnViewClickListener = clickListener
            return this
        }
        fun setBindViewListener(bindViewListener: NiceAlertDialog.OnBindViewListener): Builder {
            params?.mOnBindViewListener = bindViewListener
            return this
        }

        fun create(): NiceAlertDialog {
            val dialog = NiceAlertDialog()
            //将数据从Builder的Params中传递到Dialog中
            params?.apply(dialog.mController)
            return dialog
        }
    }



}