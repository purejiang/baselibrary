package com.nice.baselibrary.base.ui.view

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * 可扩展的TextView
 * @author JPlus
 * @date 2019/1/23.
 */
class NiceTextView : AppCompatTextView {

    companion object {
        private const val DRAWABLE_LEFT = 0
        private const val DRAWABLE_TOP = 1
        private const val DRAWABLE_RIGHT = 2
        private const val DRAWABL_BOTTOM = 3
    }

    private var mLeftDrawableListener: LeftDrawableListener? = null
    private var mRightDrawableListener: RightDrawableListener? = null

    constructor(context: Context) : super(context){
        init()
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        init()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init()
    }
    private fun init(){
        this.isSelected = true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_UP -> {
                mLeftDrawableListener?.let{
                    val leftDrawable = compoundDrawables[DRAWABLE_LEFT] //获取左边图片
                    if (leftDrawable != null && event.rawX <= (left + leftDrawable.bounds.width())) {//判断点击是否在图片区域中
                        it.onLeftDrawableListener()
                        return true //消费点击事件
                    }
                }
                mRightDrawableListener?.let{
                    val leftDrawable = compoundDrawables[DRAWABLE_RIGHT]
                    if (leftDrawable != null && event.rawX >= (right - leftDrawable.bounds.width())) {
                        it.onRightDrawableListener()
                        return true
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 左图片的点击监听
     * @param leftDrawableListener
     */
    fun setLeftDrawableListener(leftDrawableListener: LeftDrawableListener) {
        this.mLeftDrawableListener = leftDrawableListener
    }

    /**
     * 右图片的点击监听
     * @param rightDrawableListener
     */
    fun setRightDrawableListener(rightDrawableListener: RightDrawableListener) {
        this.mRightDrawableListener = rightDrawableListener
    }


    interface LeftDrawableListener {
        /**
         * 左图片的点击事件
         */
        fun onLeftDrawableListener()
    }

    interface RightDrawableListener {
        /**
         * 右图片的点击时间
         */
        fun onRightDrawableListener()
    }

    /**
     * 设置Drawable的padding值
     */
    override fun setCompoundDrawablePadding(pad: Int) {
        super.setCompoundDrawablePadding(8)//padding设为8
    }
}