package com.nice.baselibrary.widget.dialog


import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import android.view.*
import com.nice.baselibrary.base.utils.LogUtils

/**
 * @author JPlus
 * @date 2019/4/16.
 */
abstract class NiceDialogFragment : DialogFragment() {
    companion object {
        private const val AMOUNT_DEFAULT = 0.2f
        private const val DIALOG_TAG = "niceDialog"


    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        LogUtils.getInstance().e("====onCreateDialog====")
        return super.onCreateDialog(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(getLayoutRes(), container, false)
        bindView(view)
        LogUtils.getInstance().e("====onCreateView====")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        LogUtils.getInstance().e("====onViewCreated====")
        super.onViewCreated(view, savedInstanceState)
        dialog?.run {
            requestWindowFeature(Window.FEATURE_NO_TITLE) // 去除标题栏
            setCanceledOnTouchOutside(getCancelable())//点击外部是否可取消
            getKeyListener()?.let {
                setOnKeyListener(it)
            }
            if (getAnimationRes() > 0) {
                window.setWindowAnimations(getAnimationRes())
            }
        }

    }

    override fun onStart() {
        LogUtils.getInstance().e("====onStart====")
        super.onStart()
        dialog?.window?.run {
            //设置窗体背景
            setBackgroundDrawableResource(getBackgroundDrawableRes())
        }
        dialog?.window?.attributes = dialog?.window?.attributes?.apply {
            width = getDialogWidth()
            height = getDialogHeight()
            dimAmount = getDimAmount()
            gravity = getGravity()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LogUtils.getInstance().e("====onConfigurationChanged====")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.getInstance().e("====onCreate====")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        LogUtils.getInstance().e("====onAttach====")
    }

    override fun onPause() {
        super.onPause()
        LogUtils.getInstance().e("====onPause====")
    }

    override fun onResume() {
        super.onResume()
        LogUtils.getInstance().e("====onResume====")
    }

    override fun onStop() {
        super.onStop()
        LogUtils.getInstance().e("====onStop====")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.getInstance().e("====onDestroy====")
    }

    override fun onDetach() {
        super.onDetach()
        LogUtils.getInstance().e("====onDetach====")
    }

    /**
     * 返回布局
     * @return
     */
    abstract fun getLayoutRes(): Int

    /**
     * 获得view
     * @param view
     */
    abstract fun bindView(view: View)


    /**
     * 获取dialog在屏幕中的位置
     * @return
     */
    open fun getGravity(): Int {
        return Gravity.CENTER
    }

    /**
     * 获取dialog是否可以点击外部取消
     * @return
     */
    open fun getCancelable(): Boolean {
        return true
    }

    /**
     * 获取dialog弹出后的虚拟按键点击事件
     * @return
     */
    open fun getKeyListener(): DialogInterface.OnKeyListener? {
        return null
    }

    /**
     * 获取dialog背景的样式
     * @return
     */
    open fun getBackgroundDrawableRes(): Int {
        return 0
    }
    /**
     * 获取dialog的动画
     * @return
     */
    open fun getAnimationRes(): Int {
        return 0
    }
    /**
     * 获取dialog背景阴影的透明度
     * @return
     */
    open fun getDimAmount(): Float {
        return AMOUNT_DEFAULT
    }

    /**
     * 获取dialog的宽
     * @return
     */
    open fun getDialogWidth(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }
    /**
     * 获取dialog的高
     * @return
     */
    open fun getDialogHeight(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

    /**
     * 获取dialog的tag
     * @return
     */
    open fun getDialogTag():String{
        return DIALOG_TAG
    }

    fun show(fragmentTransaction: FragmentTransaction) {
        show(fragmentTransaction, getDialogTag())
    }

}