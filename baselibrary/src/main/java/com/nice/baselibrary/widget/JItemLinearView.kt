package com.nice.baselibrary.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.Switch
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.nice.baselibrary.R

/**
 * @author JPlus
 * @date 2020/4/23.
 */
class JItemLinearView : RelativeLayout {
    companion object {
        const val SWITCH_TYPE = 0
        const val NORMAL_TYPE = 1
    }

    private var mLeftTextView: TextView? = null
    private var mRightTextView: TextView? = null
    private var mMiddleTextView: TextView? = null
    private var mLeftImgBtn: ImageButton? = null
    private var mRightImgBtn: ImageButton? = null
    private var mSwitch: Switch? = null

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attr: AttributeSet?) : super(context, attr) {
        initView(context, attr)
    }

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        initView(context, attributeSet)
    }

    private fun initView(context: Context, attributeSet: AttributeSet?) {

        val typeArray = context.obtainStyledAttributes(attributeSet, R.styleable.JItemLinearView)

        val leftText = typeArray.getString(R.styleable.JItemLinearView_left_text)
        val rightText = typeArray.getString(R.styleable.JItemLinearView_right_text)
        val middleText = typeArray.getString(R.styleable.JItemLinearView_middle_text)

        val leftImg = typeArray.getDrawable(R.styleable.JItemLinearView_left_img)
        val rightImg = typeArray.getDrawable(R.styleable.JItemLinearView_right_img)
        val type = typeArray.getInt(R.styleable.JItemLinearView_view_type, NORMAL_TYPE)

        val leftColor = typeArray.getColor(R.styleable.JItemLinearView_left_text_color, ContextCompat.getColor(context, R.color.black))
        val middleColor = typeArray.getColor(R.styleable.JItemLinearView_middle_text_color, ContextCompat.getColor(context, R.color.black))
        val rightColor = typeArray.getColor(R.styleable.JItemLinearView_right_text_color, ContextCompat.getColor(context, R.color.black))

        LayoutInflater.from(context).inflate(R.layout.layout_item_line, this)?.let {
            mLeftTextView = it.findViewById(R.id.tv_left_linear_view)
            mMiddleTextView = it.findViewById(R.id.tv_middle_linear_view)
            mRightTextView = it.findViewById(R.id.tv_right_linear_view)
            mLeftImgBtn = it.findViewById(R.id.img_left_linear_view)
            mRightImgBtn = it.findViewById(R.id.img_right_linear_view)
            mSwitch = it.findViewById(R.id.sw_right_linear_view)
        }
        when (type) {
            NORMAL_TYPE -> {
                mRightImgBtn?.visibility = View.VISIBLE
                mRightTextView?.visibility = View.VISIBLE
                mSwitch?.visibility = View.GONE
            }
            SWITCH_TYPE -> {
                mSwitch?.visibility = View.VISIBLE
                mRightImgBtn?.visibility = View.GONE
                mRightTextView?.visibility = View.GONE
            }
        }
        setLeftTitle(leftText, leftColor)
        setMiddleTitle(middleText, middleColor)
        setRightTitle(rightText, rightColor)

        setLeftImg(leftImg)
        setRightImg(rightImg)

    }


    fun setLeftImg(drawable: Drawable?) {
        drawable?.let {
            mLeftImgBtn?.setImageDrawable(it)
        }
    }

    fun setRightImg(drawable: Drawable?) {
        drawable?.let {
            mRightImgBtn?.setImageDrawable(it)
        }
    }

    /**
     * 设置中文本
     * @param title
     */
    fun setMiddleTitle(title: String?, color: Int? = null) {
        mMiddleTextView?.visibility = View.VISIBLE
        mMiddleTextView?.gravity = Gravity.CENTER
        mMiddleTextView?.text = title
        color?.let {
            mMiddleTextView?.setTextColor(it)
        }
    }

    /**
     * 设置左文本
     * @param title
     */
    fun setLeftTitle(title: String?, color: Int? = null) {
        mLeftTextView?.visibility = View.VISIBLE
        mLeftTextView?.gravity = Gravity.CENTER
        mLeftTextView?.text = title
        color?.let {
            mLeftTextView?.setTextColor(it)
        }
    }

    /**
     * 设置右文本
     * @param title
     */
    fun setRightTitle(title: String?, color: Int? = null) {
        mRightTextView?.visibility = View.VISIBLE
        mRightTextView?.gravity = Gravity.CENTER
        mRightTextView?.text = title
        color?.let {
            mRightTextView?.setTextColor(it)
        }
    }

    fun getLeftTextView(): TextView? {
        return mLeftTextView
    }

    fun geRightTextView(): TextView? {
        return mRightTextView
    }

    fun getMiddleTextView(): TextView? {
        return mMiddleTextView
    }

    fun getLeftImgBtn(): ImageButton? {
        return mLeftImgBtn
    }

    fun getRightImgBtn(): ImageButton? {
        return mRightImgBtn
    }

    fun getSwitch(): Switch? {
        return mSwitch
    }
}