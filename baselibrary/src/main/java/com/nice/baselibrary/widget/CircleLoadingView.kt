package com.nice.baselibrary.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.nice.baselibrary.R
import com.nice.baselibrary.base.utils.LogUtils


/**
 * @author JPlus
 * @date 2019/10/29.
 */
class CircleLoadingView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var mCountTimer: CountDownTimer? = null
    private var mOutRect: RectF? = null
    private var mInRect: RectF? = null
    private var mOutProgressPaint: Paint? = null
    private var mInProgressPaint: Paint? = null

    private var mStrokeWidth = 0f
    private var mOutAngle = 0f
    private var mInAngle = 0f
    private var mStartAngle = 0f
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mIsShowInProgress = false

    private val mMinWidth by lazy {
        150
    }
    private val mMinHeight by lazy {
        mMinWidth
    }
    private val mTypeArray by lazy {
        context.obtainStyledAttributes(attrs, R.styleable.CircleLoadingView)
    }

    init {
        initLoadingView(context)
    }

    private fun initLoadingView(context: Context) {

        val inProgressColor = mTypeArray.getColor(
            R.styleable.CircleLoadingView_in_progress_color,
            ContextCompat.getColor(context, R.color.white)
        )

        val outProgressColor = mTypeArray.getColor(R.styleable.CircleLoadingView_out_progress_color,
                ContextCompat.getColor(context, R.color.white))

        mIsShowInProgress = mTypeArray.getBoolean(R.styleable.CircleLoadingView_has_in_progress, false)
        mStrokeWidth = mTypeArray.getFloat(R.styleable.CircleLoadingView_width, 0f)
        mOutAngle = mTypeArray.getFloat(R.styleable.CircleLoadingView_out_angle, 270f)
        mInAngle = mTypeArray.getFloat(R.styleable.CircleLoadingView_in_angle, 270f)
        mOutProgressPaint = Paint()

        //第一个进度条画笔
        mOutProgressPaint?.let {
            it.color = outProgressColor
            it.isAntiAlias = true
            it.style = Paint.Style.STROKE
            it.strokeWidth = mStrokeWidth
            it.strokeJoin = Paint.Join.ROUND  //画笔结合处为圆弧
            it.strokeCap = Paint.Cap.ROUND  //画笔始末端（线帽）样式为圆形
        }
        if (mIsShowInProgress) {
            mInProgressPaint = Paint()
        }
        mInProgressPaint?.let {
            //第二个进度条画笔
            it.color = inProgressColor
            it.isAntiAlias = true
            it.style = Paint.Style.STROKE
            it.strokeWidth = mStrokeWidth
            it.strokeJoin = Paint.Join.ROUND  //画笔结合处为圆弧
            it.strokeCap = Paint.Cap.ROUND  //画笔始末端（线帽）样式为圆形
        }

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        //处理wrap_contentde情况
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            mWidth = mMinWidth
            mHeight = mMinHeight
            setMeasuredDimension(mMinWidth, mMinHeight)
        } else if (widthMode == MeasureSpec.AT_MOST) {
            mWidth = mMinWidth
            mHeight = heightSize
            setMeasuredDimension(mMinWidth, heightSize)
        } else if (heightMode == MeasureSpec.AT_MOST) {
            mWidth = widthSize
            mHeight = mMinHeight
            setMeasuredDimension(widthSize, mMinHeight)
        }else{
            mWidth = widthSize
            mHeight = heightSize
        }
      LogUtils.d("mWidth:$mWidth, mHeight:$mHeight")
        if (mStrokeWidth == 0f) {
            mStrokeWidth = mWidth / 10f
            mOutProgressPaint?.strokeWidth = mStrokeWidth
            mInProgressPaint?.strokeWidth = mStrokeWidth
        }
        mOutRect = RectF(
            mWidth / 2f - mWidth / 2f + mStrokeWidth / 2f,
            mHeight / 2f - mHeight / 2f + mStrokeWidth / 2f,
            mWidth / 2f + mWidth / 2f - mStrokeWidth / 2f,
            mHeight / 2f + mHeight / 2f - mStrokeWidth / 2f
        )
        mInRect = RectF(
            mWidth / 2f - mWidth / 2f + mStrokeWidth / 2f + mWidth / 4,
            mHeight / 2f - mHeight / 2f + mStrokeWidth / 2f + mHeight / 4,
            mWidth / 2f + mWidth / 2f - mStrokeWidth / 2f - mWidth / 4,
            mHeight / 2f + mHeight / 2f - mStrokeWidth / 2f - mHeight / 4
        )
        start()
    }

    fun start(){
        //倒计时，第一个参数为总时长，第二个为每次执行onTick的间隔时间
        mCountTimer = object : CountDownTimer(3000, 10) {
            override fun onTick(millisUntilFinished: Long) {
                val radio = millisUntilFinished / 3000f
                mStartAngle = (360 - 360 * radio - 50) //
                invalidate()
            }

            override fun onFinish() {
                if (mCountTimer != null) {
                    mCountTimer?.start()
                }
            }
        }.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mOutProgressPaint?.let { paint ->
            mOutRect?.let {
                canvas?.drawArc(it, mStartAngle, mOutAngle, false, paint)
            }
        }
        mInProgressPaint?.let { paint ->
            mInRect?.let {
                canvas?.drawArc(it, 360f - mStartAngle, mInAngle, false, paint)
            }
        }
    }


    /**
     * 避免长时间重绘导致内存泄漏
     */
    fun close() {
        //资源回收
        mTypeArray.recycle()
        if (mCountTimer != null) {
            mCountTimer?.cancel()
            mCountTimer?.onFinish()
            mCountTimer = null
        }
    }

}

