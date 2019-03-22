package com.nice.baselibrary.base.view

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
        private val DRAWABLE_LEFT = 0
        private val DRAWABLE_TOP = 1
        private val DRAWABLE_RIGHT = 2
        private val DRAWABL_BOTTOM = 3
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
//        this.gravity =  Gravity.CENTER
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_UP -> {
                if (mLeftDrawableListener != null) {
                    var leftDrawable = compoundDrawables[DRAWABLE_LEFT] //获取左图片实例
                    if (leftDrawable != null && event.rawX <= (left + leftDrawable.bounds.width())) {//判断点击是否在图片区域中
                        mLeftDrawableListener?.onLeftDrawableListener()
                        return true //消费点击事件
                    }
                }
                if (mRightDrawableListener != null) {
                    var leftDrawable = compoundDrawables[DRAWABLE_RIGHT]
                    if (leftDrawable != null && event.rawX >= (right - leftDrawable.bounds.width())) {
                        mRightDrawableListener?.onRightDrawableListener()
                        return true
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 左图片的点击监听
     */
    fun setLeftDrawableListener(leftDrawableListener: LeftDrawableListener) {
        this.mLeftDrawableListener = leftDrawableListener
    }

    /**
     * 右图片的点击监听
     */
    fun setRightDrawableListener(rightDrawableListener: RightDrawableListener) {
        this.mRightDrawableListener = rightDrawableListener
    }

    /**
     * 左图片的点击
     */
    interface LeftDrawableListener {
        fun onLeftDrawableListener()
    }
    /**
     * 右图片的点击
     */
    interface RightDrawableListener {
        fun onRightDrawableListener()
    }

    /**
     * 设置Drawable的padding值
     */
    override fun setCompoundDrawablePadding(pad: Int) {
        super.setCompoundDrawablePadding(8)//padding设为8
    }
}