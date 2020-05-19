package com.nice.baselibrary.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView
import com.nice.baselibrary.base.utils.LogUtils


/**
 * 可扩展的TextView
 * @author JPlus
 * @date 2019/1/23.
 */
class JTextView : AppCompatTextView {

    companion object {
        const val DRAWABLE_LEFT = 0
        const val DRAWABLE_TOP = 1
        const val DRAWABLE_RIGHT = 2
        const val DRAWABLE_BOTTOM = 3
    }

    private var mDrawableListener: DrawableClickListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        this.isSelected = true
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_UP -> {
                LogUtils.d(" MotionEvent.ACTION_DOWN")
                mDrawableListener?.let {
                    val leftDrawable = compoundDrawables[DRAWABLE_LEFT] //获取左边图片
                    if (leftDrawable != null && event.rawX <= (left + leftDrawable.bounds.width())) {//判断点击是否在图片区域中
                        it.onClickListener(DRAWABLE_LEFT)
                        return true //消费点击事件
                    }
                    val rightDrawable = compoundDrawables[DRAWABLE_RIGHT]
                    if (rightDrawable != null && event.rawX >= (right - rightDrawable.bounds.width())) {
                        it.onClickListener(DRAWABLE_RIGHT)
                        return true
                    }
                    val topDrawable = compoundDrawables[DRAWABLE_TOP]
                    if (topDrawable != null && event.rawY >= (top - topDrawable.bounds.height())) {
                        it.onClickListener(DRAWABLE_TOP)
                        return true
                    }
                    val bottomDrawable = compoundDrawables[DRAWABLE_BOTTOM]
                    if (bottomDrawable != null && event.rawY <= (right + bottomDrawable.bounds.height())) {
                        it.onClickListener(DRAWABLE_BOTTOM)
                        return true
                    }
                }
                performClick()
            }
        }
        return super.onTouchEvent(event)
    }

//    override fun onDraw(canvas: Canvas?) {
//        val drawablePadding = compoundDrawablePadding
//        when {
//            compoundDrawables[DRAWABLE_LEFT] != null -> { // 左
//                val drawableWidth: Int = compoundDrawables[DRAWABLE_LEFT].intrinsicWidth
//                val bodyWidth: Float
//                bodyWidth = if (TextUtils.isEmpty(text)) {
//                    drawableWidth.toFloat()
//                } else {
//                    val textWidth = paint.measureText(text.toString())
//                    textWidth + drawableWidth + drawablePadding
//                }
//                canvas?.translate((width - bodyWidth) / 2, 0f)
//            }
//            compoundDrawables[DRAWABLE_RIGHT] != null -> { // 右
//                val drawableWidth: Int = compoundDrawables[DRAWABLE_RIGHT].intrinsicWidth
//                val bodyWidth: Float
//                bodyWidth = if (TextUtils.isEmpty(text)) {
//                    drawableWidth.toFloat()
//                } else {
//                    val textWidth = paint.measureText(text.toString())
//                    textWidth + drawableWidth + drawablePadding
//                }
//                canvas?.translate((bodyWidth - width) / 2, 0f)
//            }
//            compoundDrawables[DRAWABLE_TOP] != null -> { // 上
//                val drawableHeight: Int = compoundDrawables[DRAWABLE_TOP].intrinsicHeight
//                val bodyHeight: Float
//                bodyHeight = if (TextUtils.isEmpty(text)) {
//                    drawableHeight.toFloat()
//                } else {
//                    val fm: Paint.FontMetrics = paint.fontMetrics
//                    val fontHeight = ceil((fm.descent - fm.ascent).toDouble()).toFloat()
//                    fontHeight + drawableHeight + drawablePadding
//                }
//                canvas?.translate(0f, (height - bodyHeight) / 2)
//            }
//            compoundDrawables[DRAWABLE_BOTTOM] != null -> { // 下
//                val drawableHeight: Int = compoundDrawables[DRAWABLE_BOTTOM].intrinsicHeight
//                val bodyHeight: Float
//                bodyHeight = if (TextUtils.isEmpty(text)) {
//                    drawableHeight.toFloat()
//                } else {
//                    val fm: Paint.FontMetrics = paint.fontMetrics
//                    val fontHeight = ceil((fm.descent - fm.ascent).toDouble()).toFloat()
//                    fontHeight + drawableHeight + drawablePadding
//                }
//                canvas?.translate(0f, (bodyHeight - height) / 2)
//            }
//        }
//        super.onDraw(canvas)
//    }

    /**
     * 图片的点击监听
     * @param drawableListener
     */
    fun setDrawableClickListener(drawableListener: DrawableClickListener) {
        mDrawableListener = drawableListener
    }


    interface DrawableClickListener {
        /**
         * 图片的点击事件
         * @param type 图片的位置
         */
        fun onClickListener(type: Int)
    }

    /**
     * 设置Drawable的padding值
     */
    override fun setCompoundDrawablePadding(pad: Int) {
        super.setCompoundDrawablePadding(8)//padding设为8
    }
}