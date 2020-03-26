package com.nice.baselibrary.widget.dialog


import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import com.nice.baselibrary.base.utils.LogUtils
import com.nice.baselibrary.base.utils.getScreenHeight
import com.nice.baselibrary.base.utils.getScreenWidth

/**
 * @author JPlus
 * @date 2019/4/16.
 */
abstract class JDialogFragment : DialogFragment() {
    companion object {
        private const val AMOUNT_DEFAULT = 0.2f
        private const val DIALOG_TAG = "niceDialog"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        LogUtils.d("${this.javaClass.simpleName} --onCreateDialog")
        return super.onCreateDialog(savedInstanceState)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(getLayoutRes(), container, false)
        bindView(view)
        LogUtils.d("${this.javaClass.simpleName} --onCreateView")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        LogUtils.d("${this.javaClass.simpleName} --onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        this.dialog?.let {
            it.requestWindowFeature(Window.FEATURE_NO_TITLE) // 去除标题栏
            it.setCanceledOnTouchOutside(getCancelable())//点击外部是否可取消
            getKeyListener()?.let { listener ->
                it.setOnKeyListener(listener)
            }
            getAnimationRes()?.let { res ->
                if (res != 0) it.window?.attributes?.windowAnimations = res
            }
        }

    }


    override fun onStart() {
        LogUtils.d("${this.javaClass.simpleName} --onStart")
        super.onStart()
        this.dialog?.window?.run {
            //设置窗体背景
            setBackgroundDrawableResource(getBackgroundDrawableRes())
        }
        this.dialog?.window?.attributes = this.dialog?.window?.attributes?.let {params->
            params.apply {
                width = if (getDialogWidthPercent() == -1f) {
                    getDialogWidth()
                } else {
                    val width = context?.getScreenWidth()?:0
                    (getDialogWidthPercent() * width).toInt()
                }
                height = if (getDialogHeightPercent() == -1f) {
                    getDialogHeight()
                } else {
                    val height = context?.getScreenHeight()?:0
                    (getDialogHeightPercent() * height).toInt()
                }
                dimAmount = getDimAmount()
                gravity = getGravity()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LogUtils.d("${this.javaClass.simpleName} --onConfigurationChanged")
        //旋转屏幕后根据宽高比重新修改宽高
        this.dialog?.window?.attributes = this.dialog?.window?.attributes?.let {params->
            params.apply {
                width = if (getDialogWidthPercent() == -1f) {
                    getDialogWidth()
                } else {
                    val width = context?.getScreenWidth()?:0
                    (getDialogWidthPercent() * width).toInt()
                }
                height = if (getDialogHeightPercent() == -1f) {
                    getDialogHeight()
                } else {
                    val height = context?.getScreenHeight()?:0
                    (getDialogHeightPercent() * height).toInt()
                }
                dimAmount = getDimAmount()
                gravity = getGravity()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.d("${this.javaClass.simpleName} --onCreate")

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        LogUtils.d("${this.javaClass.simpleName} --onAttach")
    }

    override fun onPause() {
        super.onPause()
        LogUtils.d("${this.javaClass.simpleName} --onPause")
    }

    override fun onResume() {
        super.onResume()
        LogUtils.d("${this.javaClass.simpleName} --onResume")
    }

    override fun onStop() {
        super.onStop()
        LogUtils.d("${this.javaClass.simpleName} --onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.d("${this.javaClass.simpleName} --onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        LogUtils.d("${this.javaClass.simpleName} --onDetach")
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
    open fun getAnimationRes(): Int? {
        return null
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
     * 获取dialog的高比例
     * @return
     */
    open fun getDialogHeightPercent(): Float {
        return 0f
    }

    /**
     * 获取dialog的宽比例
     * @return
     */
    open fun getDialogWidthPercent(): Float {
        return 0f
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
    open fun getDialogTag(): String {
        return DIALOG_TAG
    }

    fun show(fragmentTransaction: FragmentTransaction) {
        show(fragmentTransaction, getDialogTag())
    }

}