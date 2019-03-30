package com.nice.baselibrary.base.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.nice.baselibrary.R
import com.nice.baselibrary.base.NiceLoadingView

/**
 * 带下载进度的圆形进度条
 * @author JPlus
 * @date 2018\1\11
 */

class NiceCircleProgress : NiceLoadingView {
    companion object {
        private val DOWNLOAD_SUCCESS = 100.0
        private val DOWNLOAD_FAILED = -2.0
        private val DOWNLOAD_NORMAL = -1.0
        private val DOWNLOAD_PAUSE = -3.0
        private val DOWNLOAD_CANCEL = -4.0
        private val DOWNLOAD_TEXT_ING = "下载"
        private val DOWNLOAD_TEXT_FAILED = "下载失败"
        private val DOWNLOAD_TEXT_PAUSE = "暂停"
        private val DOWNLOAD_TEXT_SUCCESS = "下载完成"
        private val DOWNLOAD_TEXT_CANCEL = "下载取消"
    }

    private var progressPaint: Paint? = null
    private var circlePaint: Paint? = null
    private var textPaint: Paint? = null
    private val mStrokeWidth = 8f
    private val mHalfStrokeWidth = mStrokeWidth / 2
    private val mRadius = 40f
    private var mRect: RectF? = null
    private var mProgress = -1.0


    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var centerHeight:Float = 0f

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.NiceCircleProgress)
        val circleColor = typeArray.getInt(R.styleable.NiceCircleProgress_circleColor, R.color.colorGrey_dark)
        val progressColor = typeArray.getInt(R.styleable.NiceCircleProgress_progressColor, R.color.colorGrey_black)
        val textSize = typeArray.getFloat(R.styleable.NiceCircleProgress_textSize, 21f)
        val textColor = typeArray.getInt(R.styleable.NiceCircleProgress_textColor, R.color.colorGrey_black)

        //背景画笔
        circlePaint = Paint()
        circlePaint?.color = ContextCompat.getColor(context, circleColor)
        circlePaint?.isAntiAlias = true //抗锯齿
        circlePaint?.style = Paint.Style.STROKE //设置画笔风格为描边
        circlePaint?.strokeWidth = mStrokeWidth //设置画笔粗细

        //进度条画笔
        progressPaint = Paint()
        progressPaint?.color = ContextCompat.getColor(context, progressColor)
        progressPaint?.isAntiAlias = true
        progressPaint?.style = Paint.Style.STROKE
        progressPaint?.strokeWidth = mStrokeWidth

        //文字画笔
        textPaint = Paint()
        textPaint?.color = ContextCompat.getColor(context, textColor)
        textPaint?.isAntiAlias = true
        textPaint?.textSize = textSize
        textPaint?.textAlign = Paint.Align.CENTER
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = getRealSize(widthMeasureSpec)
        mHeight = getRealSize(heightMeasureSpec)
        setMeasuredDimension(mWidth, mHeight) //储存测量的宽度
        centerHeight = mHeight/2.0f + ( textPaint?.fontMetrics!!.descent -  textPaint?.fontMetrics!!.ascent)/2.0f -  textPaint?.fontMetrics!!.descent //调整text居中显示
        initRectF()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when (mProgress) {
            DOWNLOAD_NORMAL -> {
                //未下载
                canvas.drawText(DOWNLOAD_TEXT_ING, mWidth/2.0f, centerHeight, textPaint)
                canvas.drawArc(mRect, -90f, 360f, false, circlePaint)
            }
            DOWNLOAD_SUCCESS -> {
                //下载成功
                canvas.drawText(DOWNLOAD_TEXT_SUCCESS, mWidth / 2.0f, centerHeight, textPaint)
            }
            DOWNLOAD_FAILED -> {
                //下载失败
                canvas.drawText(DOWNLOAD_TEXT_FAILED, mWidth/2.0f, centerHeight, textPaint)
            }
            DOWNLOAD_CANCEL -> {
                //下载取消
                canvas.drawArc(mRect, -90f, 360f, false, circlePaint)
                canvas.drawText(DOWNLOAD_TEXT_CANCEL, mWidth/2.0f, centerHeight, textPaint)
            }
            else -> {
                //下载中
                val angle = (mProgress / 100 * 360).toFloat()
                canvas.drawArc(mRect, -90f, 360f, false, circlePaint)
                canvas.drawArc(mRect, -90f, angle, false, progressPaint)
                canvas.drawText(mProgress.toString() + "%", mWidth/2.0f, centerHeight, textPaint)
            }
        }
    }

    /**
     * 获取真实宽高
     * @param measureSpec
     */
    private fun getRealSize(measureSpec: Int): Int {
        val result: Int
        val mode = View.MeasureSpec.getMode(measureSpec)
        val size = View.MeasureSpec.getSize(measureSpec)
        result = if (mode == View.MeasureSpec.AT_MOST || mode == View.MeasureSpec.UNSPECIFIED) {
            (mRadius * 2 + mStrokeWidth).toInt()
        } else {
            size
        }
        return result
    }

    /**
     * 初始化RectF
     */
    private fun initRectF() {
        if (mRect == null) {
            mRect = RectF()
            val viewSize = (mRadius * 2).toInt()
            val left = (mWidth - viewSize) / 2
            val top = (mHeight - viewSize) / 2
            val right = left + viewSize
            val bottom = top + viewSize
            mRect?.set(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
        }
    }

    override fun success() {
        mProgress = DOWNLOAD_SUCCESS
        invalidate()
    }

    override fun failed() {
        mProgress = DOWNLOAD_FAILED
        invalidate()
    }

    override fun cancel() {
        mProgress = DOWNLOAD_CANCEL
        invalidate()
    }

    override fun pause() {
        mProgress = DOWNLOAD_NORMAL
        invalidate()
    }

    /**
     * 下载更新ui
     * @param progress
     */
    override fun loading(progress: Double) {
        if (progress <= 100.0) {
            mProgress = progress
        }
        invalidate()
    }


}
