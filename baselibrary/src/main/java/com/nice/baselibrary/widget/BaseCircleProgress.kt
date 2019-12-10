package com.nice.baselibrary.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.CountDownTimer
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.nice.baselibrary.R
import com.nice.baselibrary.base.ui.BaseLoadingView
import com.nice.baselibrary.base.utils.LogUtils

/**
 * 带下载进度的圆形进度条
 * @author JPlus
 * @date 2018\1\11
 */

class BaseCircleProgress(context: Context, attrs: AttributeSet?) : BaseLoadingView(context, attrs) {
    companion object {
        private val LOADING_SUCCESS = 100.0
        private val LOADING_FAILED = -2.0
        private val LOADING_NORMAL = -1.0
        private val LOADING_PAUSE = -3.0
        private val LOADING_CANCEL = -4.0

    }

    private var progressPaint: Paint? = null
    private var circlePaint: Paint? = null
    private var textPaint: Paint? = null
    private val mStrokeWidth = 8f
    private val mHalfStrokeWidth = mStrokeWidth / 2
    private val mRadius = 40f
    private var mProgress = -1.0

    private var mRect: RectF? = null

    private var mStartText = "加载"
    private var mLoadingText = "下载中"
    private var mFailedText = "下载失败"
    private var mPauseText = "暂停"
    private var mSuccessText = "下载完成"
    private var mCancelText = "取消"

    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mCenterHeight: Float = 0f

    private var mShowNumProgress = true
    private var mIsNoProgress = false

    private var mCountTimer: CountDownTimer? = null
    private var mDefaultAngle = 0f
    private var mStartAngle = 0f


    init {
        init(context, attrs)
    }

    @SuppressLint("Recycle")
    private fun init(context: Context, attrs: AttributeSet?) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.BaseCircleProgress)
        val circleColor = typeArray.getColor(R.styleable.BaseCircleProgress_circleColor, ContextCompat.getColor(context, R.color.colorGrey_light))
        val progressColor = typeArray.getColor(R.styleable.BaseCircleProgress_progressColor, ContextCompat.getColor(context, R.color.black))
        val textSize = typeArray.getFloat(R.styleable.BaseCircleProgress_textSize, 21f)
        val textColor = typeArray.getColor(R.styleable.BaseCircleProgress_textColor, ContextCompat.getColor(context, R.color.colorGrey_light))

        mShowNumProgress = typeArray.getBoolean(R.styleable.BaseCircleProgress_showNumProgress, true)
        mIsNoProgress = typeArray.getBoolean(R.styleable.BaseCircleProgress_isNoProgress, false)

        //背景画笔
        circlePaint = Paint()
        circlePaint?.color = circleColor
        circlePaint?.isAntiAlias = true //抗锯齿
        circlePaint?.style = Paint.Style.STROKE //设置画笔风格为描边
        circlePaint?.strokeWidth = mStrokeWidth //设置画笔粗细


        //进度条画笔
        progressPaint = Paint()
        progressPaint?.color = progressColor
        progressPaint?.isAntiAlias = true
        progressPaint?.style = Paint.Style.STROKE
        progressPaint?.strokeWidth = mStrokeWidth
        progressPaint?.strokeJoin = Paint.Join.ROUND  //画笔结合处为圆弧
        progressPaint?.strokeCap = Paint.Cap.ROUND  //画笔始末端（线帽）样式为圆形

        //文字画笔
        textPaint = Paint()
        textPaint?.color = textColor
        textPaint?.isAntiAlias = true
        textPaint?.textSize = textSize
        textPaint?.textAlign = Paint.Align.CENTER

        if (mIsNoProgress) {
            //倒计时，第一个参数为总时长，第二个为每次执行onTick的间隔时间
            mCountTimer = object : CountDownTimer(3000, 10) {
                override fun onTick(millisUntilFinished: Long) {
                    val radio = millisUntilFinished / 1500f
                    mStartAngle = mDefaultAngle
                    mStartAngle = mDefaultAngle + (360 - 360 * radio) //
                    invalidate()
                }

                override fun onFinish() {
                    if (mCountTimer != null) {
                        mCountTimer?.start()
                    }
                }
            }.start()
        }
    }

    /**
     * 避免长时间重绘，导致的内存泄漏
     */
    fun close() {
        if (mCountTimer != null) {
            mCountTimer?.cancel()
            mCountTimer?.onFinish()
            mCountTimer = null
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = getRealSize(widthMeasureSpec)
        mHeight = getRealSize(heightMeasureSpec)
        setMeasuredDimension(mWidth, mHeight) //储存测量的宽度
        textPaint?.let{
            mCenterHeight = mHeight / 2.0f + (it.fontMetrics!!.descent - it.fontMetrics!!.ascent) / 2.0f - it.fontMetrics!!.descent //调整text居中显示
        }
        initRectF()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!mIsNoProgress) {
            //绘制带进度的进度条，通过传入mProgress作为进度
            drawByProgress(mProgress, canvas, mShowNumProgress)
        } else {
            //绘制无进度的loading条
            drawNoProgress(canvas)
        }
    }

    /**
     * loading模式
     * @param canvas
     */
    private fun drawNoProgress(canvas: Canvas) {
        mRect?.let {
            progressPaint?.let { paint ->
                canvas.drawArc(it, mStartAngle, mStartAngle, false, paint)
            }
        }
    }


    /**
     * 带进度的模式
     * @param progress
     * @param canvas
     * @param isShowNum
     */
    private fun drawByProgress(progress: Double, canvas: Canvas, isShowNum: Boolean) {
        when (progress) {
            LOADING_NORMAL -> {
                //未下载
                canvas.drawText(mStartText, mWidth / 2.0f, mCenterHeight, textPaint!!)
                canvas.drawArc(mRect!!, -90f, 360f, false, circlePaint!!)
            }
            LOADING_SUCCESS -> {
                //下载成功
                canvas.drawText(mSuccessText, mWidth / 2.0f, mCenterHeight, textPaint!!)
            }
            LOADING_FAILED -> {
                //下载失败
                canvas.drawText(mFailedText, mWidth / 2.0f, mCenterHeight, textPaint!!)
            }
            LOADING_CANCEL -> {
                //下载取消
                canvas.drawArc(mRect!!, -90f, 360f, false, circlePaint!!)
                canvas.drawText(mCancelText, mWidth / 2.0f, mCenterHeight, textPaint!!)
            }
            else -> {
                //下载中
                val angle = (progress / 100 * 360).toFloat()
                canvas.drawArc(mRect!!, -90f, 360f, false, circlePaint!!)
                canvas.drawArc(mRect!!, -90f, angle, false, progressPaint!!)
                if (isShowNum) {
                    canvas.drawText("$progress%", mWidth / 2.0f, mCenterHeight, textPaint!!)
                } else {
                    canvas.drawText(mLoadingText, mWidth / 2.0f, mCenterHeight, textPaint!!)
                }
            }
        }
    }

    /**
     * 获取真实宽高
     * @param measureSpec
     */
    private fun getRealSize(measureSpec: Int): Int {
        val result: Int
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        result = if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.UNSPECIFIED) {
            (mRadius * 2 + mStrokeWidth).toInt()
        } else {
            size
        }
        return result
    }

    /**
     * 初始化RectF,用于定义弧的形状和大小
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
        mProgress = LOADING_SUCCESS
        invalidate()
    }

    override fun failed() {
        mProgress = LOADING_FAILED
        invalidate()
    }

    override fun cancel() {
        mProgress = LOADING_CANCEL
        invalidate()
    }

    override fun pause() {
        mProgress = LOADING_NORMAL
        invalidate()
    }

    /**
     * 下载更新ui
     * @param progress
     */
    override fun loading(progress: Double) {
        LogUtils.d("progress:$progress")
        if (progress <= 100.0) {
            mProgress = progress
        }
        invalidate()
    }


}
