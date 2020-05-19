package com.nice.baselibrary.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.nice.baselibrary.R


/**
 * 可扩展的标题栏
 * @author JPlus
 * @date 2019/1/21.
 */
@Deprecated("Please to use JItemLinearView.")
class JTitleBar : LinearLayout {
    private var mRightTextView: JTextView? = null
    private var mMiddleTextView: JTextView? = null
    private var mLeftTextView: JTextView? = null
    private var mContext: Context? = null
    private var leftLeftImg: Drawable? = null
    private var rightLeftImg: Drawable? = null
    private var leftRightImg: Drawable? = null
    private var rightRightImg: Drawable? = null
    private var leftMiddleImg: Drawable? = null
    private var rightMiddleImg: Drawable? = null
    private var leftTxt: String? = null
    private var middleTxt: String? = null
    private var rightTxt: String? = null
    private var leftColor: Int? = null
    private var middleColor: Int? = null
    private var rightColor: Int? = null

    constructor(context: Context):super(context){
        initView(context)
    }
    constructor(context: Context, attr:AttributeSet?):super(context,attr){
        initView(context, attr)
    }
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int): super(context, attributeSet, defStyleAttr){
        initView(context, attributeSet)
    }

    private fun initView(context: Context, attrs: AttributeSet? = null) {
        mContext = context
        val view = LayoutInflater.from(context).inflate(R.layout.layout_title_bar_base, this)

        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.JTitleBar)
        leftLeftImg = typeArray.getDrawable(R.styleable.JTitleBar_left_lefttxt_img)
        rightLeftImg = typeArray.getDrawable(R.styleable.JTitleBar_right_lefttxt_img)
        leftRightImg = typeArray.getDrawable(R.styleable.JTitleBar_left_righttxt_img)
        rightRightImg = typeArray.getDrawable(R.styleable.JTitleBar_right_righttxt_img)
        leftMiddleImg = typeArray.getDrawable(R.styleable.JTitleBar_left_middletxt_img)
        rightMiddleImg = typeArray.getDrawable(R.styleable.JTitleBar_right_middletxt_img)
        leftTxt = typeArray.getString(R.styleable.JTitleBar_left_txt)
        middleTxt = typeArray.getString(R.styleable.JTitleBar_middle_txt)
        rightTxt = typeArray.getString(R.styleable.JTitleBar_right_txt)
        leftColor = typeArray.getColor(R.styleable.JTitleBar_left_txt_color, ContextCompat.getColor(context, R.color.black))
        middleColor = typeArray.getColor(R.styleable.JTitleBar_middle_txt_color, ContextCompat.getColor(context, R.color.black))
        rightColor = typeArray.getColor(R.styleable.JTitleBar_right_txt_color, ContextCompat.getColor(context, R.color.black))


        view?.let {
            mLeftTextView = it.findViewById(R.id.tv_back_title_bar)
            mMiddleTextView = it.findViewById(R.id.tv_title_title_bar)
            mRightTextView = it.findViewById(R.id.tv_menu_title_bar)

        }
        setLeftTitle(leftTxt, leftColor)
        setMiddleTitle(middleTxt, middleColor)
        setRightTitle(rightTxt, rightColor)
        //setCompoundDrawablesWithIntrinsicBounds是自动使用drawable的宽高，setCompoundDrawables需要在前面先调用setBounds()设置宽高
        mLeftTextView?.setCompoundDrawablesWithIntrinsicBounds(leftLeftImg, null, rightLeftImg, null)
        mRightTextView?.setCompoundDrawablesWithIntrinsicBounds(leftRightImg, null, rightRightImg, null)
        mMiddleTextView?.setCompoundDrawablesWithIntrinsicBounds(leftMiddleImg, null, rightMiddleImg, null)
    }


    /**
     * 设置主标题
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
     * 设置左标题
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
     * 设置右标题
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

    fun getLeftView(): JTextView? {
        return mLeftTextView
    }

    fun geRightView(): JTextView? {
        return mRightTextView
    }

    fun getMiddleView(): JTextView? {
        return mMiddleTextView
    }


}