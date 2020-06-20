package com.nice.baselibrary.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.nice.baselibrary.R


/**
 * @author JPlus
 * @date 2020/6/19.
 */
class BaseDivider : View {
    companion object {
        //分割线宽度
        const val HEAD_AND_FOOT = 0
        const val NO_HEAD = 1
        const val NO_FOOT = 2
        const val NONE_DIVIDER = 3

        //分割线类型
        const val NORMAL_TYPE = 4
        const val DOWN_SHADOW_TYPE = 5
    }

    private var mDefaultPaint: Paint? = null
    private var mPath: Path? = null
    private var mShadowColor: Int? = null
    private var mType: Int? = null
    private var mWidth = 0
    private var mHeight = 0
    private var mRealMin = 0f
    private val mDefaultWidth: Int by lazy {
        2
    }
    private val mShadowWidth: Int by lazy {
        20
    }

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attr: AttributeSet?) : super(context, attr) {
        initView(context, attr)
    }

    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(context, attr, defStyleAttr) {
        initView(context, attr)
    }

    private fun initView(context: Context, attributeSet: AttributeSet?) {
        val typeArray = context.obtainStyledAttributes(attributeSet, R.styleable.BaseDivider)
        val dividerType = typeArray.getInt(R.styleable.BaseDivider_divider_width, HEAD_AND_FOOT)
        mShadowColor = typeArray.getInt(R.styleable.BaseDivider_shadow_color, ContextCompat.getColor(context, R.color.black))
        mType = typeArray.getInt(R.styleable.BaseDivider_divider_type, NORMAL_TYPE)
        val color = typeArray.getColor(R.styleable.BaseDivider_color, ContextCompat.getColor(context, R.color.gray_cc))
        typeArray.recycle()

        mDefaultPaint = Paint()
        mDefaultPaint?.color = color
        mDefaultPaint?.isAntiAlias = true
        mDefaultPaint?.style = Paint.Style.FILL_AND_STROKE
        mPath = Path()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = measureRealSize(0, widthMeasureSpec)
        mHeight = measureRealSize(if (mType == DOWN_SHADOW_TYPE) mShadowWidth else mDefaultWidth, heightMeasureSpec)
        setMeasuredDimension(mWidth, mHeight)
        mRealMin = if (mHeight > mWidth) mWidth.toFloat() else mHeight.toFloat()
    }

    private fun measureRealSize(default: Int, measureSpec: Int): Int {
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)

        return if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY) {
            size
        } else {
            default
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mPath?.moveTo(paddingStart.toFloat(),0f)
        mPath?.lineTo(mWidth.toFloat() - paddingEnd.toFloat(), 0f)

        when (mType) {
            NORMAL_TYPE -> {
                mDefaultPaint?.strokeWidth = mRealMin
            }
            DOWN_SHADOW_TYPE -> {
                mDefaultPaint?.strokeWidth = 1f
                mDefaultPaint?.setShadowLayer(if (mRealMin < mShadowWidth * 1.5) mRealMin / 1.5f else mShadowWidth.toFloat(), 0f,  0f, mShadowColor!!)
            }
        }

        mDefaultPaint?.let {
            canvas?.drawPath(mPath!!, it)
        }
    }
}