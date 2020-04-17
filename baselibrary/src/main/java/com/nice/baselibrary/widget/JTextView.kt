package com.nice.baselibrary.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView
import kotlin.math.ceil


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
         const val DRAWABL_BOTTOM = 3
    }

    private var mDrawableListener: DrawableListener? = null

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
                mDrawableListener?.let{
                    val leftDrawable = compoundDrawables[DRAWABLE_LEFT] //获取左边图片
                    if (leftDrawable != null && event.rawX <= (left + leftDrawable.bounds.width())) {//判断点击是否在图片区域中
                        it.onDrawableListener(DRAWABLE_LEFT)
                        return true //消费点击事件
                    }
                }
                mDrawableListener?.let{
                    val leftDrawable = compoundDrawables[DRAWABLE_RIGHT]
                    if (leftDrawable != null && event.rawX >= (right - leftDrawable.bounds.width())) {
                        it.onDrawableListener(DRAWABLE_RIGHT)
                        return true
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas?) {
        val drawablePadding = compoundDrawablePadding
        when {
            compoundDrawables[DRAWABLE_LEFT] != null -> { // 左
                val drawableWidth: Int =  compoundDrawables[DRAWABLE_LEFT].intrinsicWidth
                val bodyWidth: Float
                bodyWidth = if (TextUtils.isEmpty(text)) {
                    drawableWidth.toFloat()
                } else {
                    val textWidth = paint.measureText(text.toString())
                    textWidth + drawableWidth + drawablePadding
                }
                canvas?.translate((width - bodyWidth) / 2, 0f)
            }
            compoundDrawables[DRAWABLE_RIGHT]  != null -> { // 右
                val drawableWidth: Int = compoundDrawables[DRAWABLE_RIGHT].intrinsicWidth
                val bodyWidth: Float
                bodyWidth = if (TextUtils.isEmpty(text)) {
                    drawableWidth.toFloat()
                } else {
                    val textWidth = paint.measureText(text.toString())
                    textWidth + drawableWidth + drawablePadding
                }
                canvas?.translate((bodyWidth - width) / 2, 0f)
            }
            compoundDrawables[DRAWABLE_TOP]  != null -> { // 上
                val drawableHeight: Int =   compoundDrawables[DRAWABLE_TOP] .intrinsicHeight
                val bodyHeight: Float
                bodyHeight = if (TextUtils.isEmpty(text)) {
                    drawableHeight.toFloat()
                } else {
                    val fm: Paint.FontMetrics = paint.fontMetrics
                    val fontHeight = ceil((fm.descent - fm.ascent).toDouble()).toFloat()
                    fontHeight + drawableHeight + drawablePadding
                }
                canvas?.translate(0f, (height - bodyHeight) / 2)
            }
            compoundDrawables[DRAWABL_BOTTOM]!= null -> { // 下
                val drawableHeight: Int =   compoundDrawables[DRAWABL_BOTTOM].getIntrinsicHeight()
                val bodyHeight: Float
                bodyHeight = if (TextUtils.isEmpty(text)) {
                    drawableHeight.toFloat()
                } else {
                    val fm: Paint.FontMetrics = paint.fontMetrics
                    val fontHeight = ceil((fm.descent - fm.ascent).toDouble()).toFloat()
                    fontHeight + drawableHeight + drawablePadding
                }
                canvas?.translate(0f, (bodyHeight - height) / 2)
            }
        }
        super.onDraw(canvas)
    }

    /**
     * 图片的点击监听
     * @param drawableListener
     */
    fun setDrawableListener(drawableListener: DrawableListener) {
        this.mDrawableListener = mDrawableListener
    }


    interface DrawableListener {
        /**
         * 图片的点击事件
         * @param 图片的位置
         */
        fun onDrawableListener(type:Int)
    }


    /**
     * 设置Drawable的padding值
     */
    override fun setCompoundDrawablePadding(pad: Int) {
        super.setCompoundDrawablePadding(8)//padding设为8
    }
}