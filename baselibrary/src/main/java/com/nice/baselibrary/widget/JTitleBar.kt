package com.nice.baselibrary.widget

import android.content.Context
import android.util.AttributeSet
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.nice.baselibrary.R


/**
 * 可扩展的标题栏
 * @author JPlus
 * @date 2019/1/21.
 */
class JTitleBar :RelativeLayout,View.OnClickListener {
    private var mBackJTextView: JTextView? = null
    private var mTitleJTextView: JTextView? = null
    private var mMenuJTextView: JTextView? = null
    private var mContext:Context?=null

    constructor(context: Context):super(context){
        mContext = context
        initView()
        bindListener()
    }
    constructor(context: Context, attr:AttributeSet):super(context,attr){
        mContext = context
        initView()
        bindListener()
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr){
        mContext = context
        initView()
        bindListener()
    }

    private fun initView() {
        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_title_bar_base, this)
        view?.let {
            mBackJTextView = it.findViewById(R.id.tv_back_title_bar)
            mTitleJTextView = it.findViewById(R.id.tv_title_title_bar)
            mMenuJTextView = it.findViewById(R.id.tv_menu_title_bar)
        }

    }

    private fun bindListener() {

    }

    /**
     * 设置标题文字颜色
     * @param color
     */
     fun setTitleTextColor(color: Int) {
        mTitleJTextView?.setTextColor(ContextCompat.getColor(mContext!!, color))
    }
    /**
     * 设置左右标题文字颜色
     * @param color
     */
    fun setTextColor(color: Int) {
        mBackJTextView?.setTextColor(ContextCompat.getColor(mContext!!, color))
        mMenuJTextView?.setTextColor(ContextCompat.getColor(mContext!!, color))
    }

    /**
     * 设置标题栏颜色
     * @param color
     */
    fun setTitleBarColor(color: Int) {
        this.setBackgroundColor(ContextCompat.getColor(mContext!!, color))
    }

    /**
     * 设置主标题
     * @param title
     */
     fun setMainTitle(title: String) {
        mTitleJTextView?.gravity =Gravity.CENTER
        mTitleJTextView?.text = title
    }

    /**
     * 设置左标题
     * @param title
     */
     fun setLeftTitle(title: String) {
        mBackJTextView?.gravity =Gravity.CENTER
        mBackJTextView?.text = title
    }

    /**
     * 设置右标题
     * @param title
     */
     fun setRightTitle(title: String) {
        mMenuJTextView?.gravity =Gravity.CENTER
        mMenuJTextView?.text = title
    }

    /**
     * 设置左图以及点击事件
     * @param leftId
     * @param leftListener
     * @param left2Id
     * @param left2Listener
     */
     fun setLeftDrawable(leftId: Int?, leftListener: TitleClickListener?, left2Id: Int?, left2Listener: TitleClickListener?, iconSize:Int = 48) {
        var leftDrawable: Drawable? = null
        var left2Drawable: Drawable? = null
        if (leftId != null) {
            leftDrawable = ContextCompat.getDrawable(mContext!!, leftId)
//            leftDrawable.setBounds(0, 0, leftDrawable.minimumWidth, leftDrawable.minimumHeight)//根据icon的大小自动调整
            leftDrawable?.setBounds(0, 0, iconSize, iconSize) //限定icon的大小
        }
        if (left2Id != null) {
            left2Drawable = ContextCompat.getDrawable(mContext!!, left2Id)
//            left2Drawable.setBounds(0, 0, left2Drawable.minimumWidth, left2Drawable.minimumHeight)
            left2Drawable?.setBounds(0, 0, iconSize, iconSize)

        }
        mBackJTextView?.let {
            it.setCompoundDrawables(leftDrawable, null, left2Drawable, null)
            it.setLeftDrawableListener(object : JTextView.LeftDrawableListener {
                override fun onLeftDrawableListener() {
                    leftListener?.onClickListener()
                }
            })
            it.setRightDrawableListener(object : JTextView.RightDrawableListener {
                override fun onRightDrawableListener() {
                    left2Listener?.onClickListener()
                }
            })
        }



    }
    /**
     * 设置右图以及点击事件
     * @param rightId
     * @param rightListener
     * @param right2Id
     * @param right2Listener
     */
     fun setRightDrawable(rightId: Int?, rightListener: TitleClickListener?, right2Id: Int?, right2Listener: TitleClickListener?, iconSize:Int = 48) {
        var rightDrawable: Drawable? = null
        var right2Drawable: Drawable? = null
        if (rightId != null) {
            rightDrawable = ContextCompat.getDrawable(mContext!!, rightId)
//            rightDrawable.setBounds(0, 0, rightDrawable.minimumWidth, rightDrawable.minimumHeight)
            rightDrawable?.setBounds(0, 0, iconSize, iconSize)
        }

        if (right2Id != null) {
            right2Drawable = ContextCompat.getDrawable(mContext!!, right2Id)
//            right2Drawable.setBounds(0, 0, right2Drawable.minimumWidth, right2Drawable.minimumHeight)
            right2Drawable?.setBounds(0, 0, iconSize, iconSize)
        }
        mMenuJTextView?.let{
            it.setCompoundDrawables(rightDrawable, null, right2Drawable, null)

            it.setLeftDrawableListener(object : JTextView.LeftDrawableListener {
                override fun onLeftDrawableListener() {
                    rightListener?.onClickListener()
                }
            })
            it.setRightDrawableListener(object : JTextView.RightDrawableListener {
                override fun onRightDrawableListener() {
                    right2Listener?.onClickListener()
                }
            })
        }
    }


    override fun onClick(v: View?) {
        when(v?.id){

        }
    }

    interface TitleClickListener {
        fun onClickListener()
    }
}