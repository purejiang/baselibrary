package com.nice.baselibrary.base.ui.view

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.EditText
import com.nice.baselibrary.R
import com.nice.baselibrary.base.utils.LogUtils

/**
 * 可清空的输入框
 * @author JPlus
 * @date 2019/2/19.
 */
class NiceEditText :AppCompatEditText {
    private var mRightPic:Drawable?=null
    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    /**
     * 初始化
     * @param context
     * @param attrs
     */
    private fun init(context: Context, attrs: AttributeSet?) {
        this.background = ContextCompat.getDrawable(context, R.drawable.edit_base_circle_background)
        mRightPic = if(this.inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            //密码格式则显示眼睛
            ContextCompat.getDrawable(context, R.mipmap.ic_del_grey)
        }else{
            //其他则显示清除
            ContextCompat.getDrawable(context, R.mipmap.ic_del_grey)
        }

            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable) {
                    setDrawable(mRightPic)
                }
            })
            setDrawable(mRightPic)
        }

    /**
     * 设置editText右图
     * @param rightPic
     */
    private fun setDrawable(rightPic:Drawable?) {
        var drawable:Drawable?=null
        if (length()>0){
            drawable =rightPic
        }
        //使用setCompoundDrawables()是没有效果的
        setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)

        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            if (mRightPic != null && event.action == MotionEvent.ACTION_UP) {
                val eventX = event.rawX.toInt()
                val eventY = event.rawY.toInt()
                val rect = Rect()
                this.getGlobalVisibleRect(rect)
                rect.left = rect.right - 100
                if (rect.contains(eventX, eventY)) {
//                    if(aaa) {
//                        setText("")
//                    }else{
//
//                    }
                }
            }
            return super.onTouchEvent(event)

        }

}