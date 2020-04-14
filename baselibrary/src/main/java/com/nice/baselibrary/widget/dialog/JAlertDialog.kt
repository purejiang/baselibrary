package com.nice.baselibrary.widget.dialog


import android.content.DialogInterface
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nice.baselibrary.base.adapter.BaseAdapter


/**
 * 自定义DialogFragment
 * @author JPlus
 * @date 2019/4/17.
 */

open class JAlertDialog : JDialogFragment() {
    private val mController: JDialogController by lazy {
        JDialogController.create()
    }


    override fun bindView(view: View) {
        val viewHolder = BaseAdapter.VH(view)
        //设置是否可点击
        mController.getIds()?.forEach {
            val view2 = viewHolder.getView<View>(it)
            view2.isClickable =true
            view2.setOnClickListener {
                this.getViewClickListener()?.onClick(viewHolder, view2, this)
            }
            }
        this.getBindViewListener()?.onBindView(viewHolder)

        //adapter不为空则设置adapter和recyclerView
        getListAdapter()?.let{
//            it.setItemClickListener(getListItemClickListener())
            val layoutManager = LinearLayoutManager(view.context, getListOrientation(), false)
            view.findViewById<RecyclerView>(getListRecyclerId()).run{
                setLayoutManager(layoutManager)
                adapter = getListAdapter()
            }
            it.notifyDataSetChanged()
        }

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        getDismissListener()?.onDismiss(dialog)

    }

    override fun getLayoutRes(): Int {
        return mController.getLayoutRes()
    }

    override fun getGravity(): Int {
        return mController.getGravity()
    }

    override fun getCancelable(): Boolean {
        return mController.getCancelable()
    }

    override fun getAnimationRes(): Int? {
        return mController.getAnimationRes()
    }

    override fun getDialogHeight(): Int {
        return mController.getDialogHeight()
    }

    override fun getDialogWidth(): Int {
        return mController.getDialogWidth()
    }
    override fun getDialogHeightPercent(): Float {
        return mController.getDialogHeightPercent()
    }

    override fun getDialogWidthPercent(): Float {
        return mController.getDialogWidthPercent()
    }
    override fun getDimAmount(): Float {
        return mController.getDimAmount()
    }

    override fun getKeyListener(): DialogInterface.OnKeyListener? {
        return mController.getKeyListener()
    }
    override fun getBackgroundDrawableRes():Int{
        return mController.getBackgroundRes()
    }
    private fun getBindViewListener(): OnBindViewListener? {
        return mController.getOnBindViewListener()
    }

    private fun getDismissListener(): DialogInterface.OnDismissListener? {
        return mController.getOnDismissListener()
    }
    private fun getViewClickListener(): OnViewClickListener? {
        return mController.getOnViewClickListener()
    }

//    private fun getListItemClickListener(): NiceAdapter.ItemClickListener<*>? {
//        return mController.getListItemClickListener()
//    }

    private fun getListAdapter():BaseAdapter<*>?{
        return mController.getAdapter()
    }

    private fun getListRecyclerId():Int{
        return mController.getListRecyclerId()
    }
    private fun getListOrientation():Int{
        return mController.getListOrientation()
    }

    open fun show(): JAlertDialog {
            mController.getFragmentManager()?.let{
                this.show(it, mController.getTag())
            }
        return this
    }

    interface OnViewClickListener {
        fun onClick(viewHolder: BaseAdapter.VH, view: View, dialog: JAlertDialog)
    }
    interface OnBindViewListener {
        fun onBindView(viewHolder: BaseAdapter.VH)
    }

    class Builder(mFragmentManager: FragmentManager) {

        private val params: JDialogController.Params by lazy {
            JDialogController.Params()
        }

        init {
            params.mFragmentManager = mFragmentManager
        }

        /**
         * 设置布局
         * @param layoutRes
         * @return Builder
         */
        fun setLayoutRes(layoutRes: Int): Builder {
            params.mLayoutRes = layoutRes
            return this
        }
        /**
         * 设置dialog所处位置
         * @param gravity
         * @return Builder
         */
        fun setGravity(gravity: Int): Builder {
            params.mGravity = gravity
            return this
        }
        /**
         * 设置是否可取消
         * @param isCancelable
         * @return Builder
         */
        fun setCancelable(isCancelable: Boolean): Builder {
            params.mIsCancelable = isCancelable
            return this
        }
        /**
         * 设置Tag
         * @param tag
         * @return Builder
         */
        fun setTag(tag: String): Builder {
            params.mTag = tag
            return this
        }
        /**
         * 设置动画
         * @param animationRes
         * @return Builder
         */
        fun setAnimationRes(animationRes: Int): Builder {
            params.mAnimationRes = animationRes
            return this
        }
        /**
         * 设置百分比宽
         * @param percent
         * @return Builder
         */
        fun setScreenWidthPercent(percent: Float): Builder {
            params.mDialogWidthPercent =  percent
            return this
        }
        /**
         * 设置百分比高
         * @param percent
         * @return Builder
         */
        fun setScreenHeightPercent(percent: Float): Builder {
            params.mDialogHeightPercent = percent
            return this
        }
        /**
         * 设置dialog布局背景
         * @param backgroundRes
         * @return Builder
         */
        fun setBackgroundRes(backgroundRes: Int): Builder {
            params.mBackgroundRes = backgroundRes
            return this
        }
        /**
         * 设置dialog高度
         * @param height
         * @return Builder
         */
        fun setDialogHeight(height: Int): Builder {
            params.mDialogHeight = height
            return this
        }
        /**
         * 设置dialog宽度
         * @param width
         * @return Builder
         */
        fun setDialogWidth(width: Int): Builder {
            params.mDialogWidth = width
            return this
        }
        /**
         * 设置dialog阴影透明度
         * @param amount
         * @return Builder
         */
        fun setDimAmount(amount: Float): Builder {
            params.mDimAmount = amount
            return this
        }
        /**
         * 设置按键监听
         * @param onKeyListener
         * @return Builder
         */
        fun setKeyListener(onKeyListener: DialogInterface.OnKeyListener): Builder {
            params.mOnKeyListener = onKeyListener
            return this
        }
        /**
         * 添加可点击事件控件
         * @param ids
         * @return Builder
         */
        fun addClickedId(vararg ids: Int): Builder {
            params.mIds = ids
            return this
        }
        /**
         * 设置控件点击事件
         * @param clickListener
         * @return Builder
         */
        fun setViewClickListener(clickListener: OnViewClickListener): Builder {
            params.mOnViewClickListener = clickListener
            return this
        }
//        /**
//         * 设置列表条目点击事件
//         * @param clickListener
//         * @return Builder
//         */
//        fun setListItemClickListener(clickListener: NiceAdapter.ItemClickListener<*>): Builder {
//            params.mListItemClickListener = clickListener
//            return this
//        }
        /**
         * 设置dialog关闭的回调
         * @param dismissListener
         * @return Builder
         */
        fun setDismissListener(dismissListener: DialogInterface.OnDismissListener): Builder {
            params.mOnDismissListener = dismissListener
            return this
        }
        /**
         * 设置控件的其他方法监听
         * @param bindViewListener
         * @return Builder
         */
        fun setBindViewListener(bindViewListener: OnBindViewListener): Builder {
            params.mOnBindViewListener = bindViewListener
            return this
        }
        /**
         * 设置列表recyclerId和滚动方向
         * @param recyclerId
         * @param orientation
         * @return Builder
         */
        fun setListRes(recyclerId:Int, orientation:Int): Builder {
            params.mListRecyclerId =recyclerId
            params.mListOrientation = orientation
            return this
        }
        /**
         * 设置适配器
         * @param adapter
         * @return Builder
         */
        fun setAdapter(adapter: BaseAdapter<*>): Builder {
            params.mAdapter = adapter
            return this
        }
        /**
         * 创建NiceDialog
         * @return NiceAlertDialog
         */
        fun create(): JAlertDialog {
            val dialog = JAlertDialog()
            //将数据从Builder的Params中传递到Dialog中
            params.apply(dialog.mController)
            return dialog
        }
    }

}