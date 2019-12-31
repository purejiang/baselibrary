package com.nice.baselibrary.widget

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.nice.baselibrary.R

/**
 * 可清空的输入框
 * @author JPlus
 * @date 2019/2/19.
 */
class JEditTextView(context: Context, attrs: AttributeSet?) : AppCompatEditText(context, attrs) {
    private var mRightPic:Drawable?=null

    init {
        init(context, attrs)
    }

    /**
     * 初始化
     * @param context
     * @param attrs
     */
    private fun init(context: Context, attrs: AttributeSet?) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.JEditTextView)
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
//            drawable =rightPic
        }
        //使用setCompoundDrawables()是没有效果的
//        setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)

        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            if (mRightPic != null && event.action == MotionEvent.ACTION_UP) {
                val eventX = event.rawX.toInt()
                val eventY = event.rawY.toInt()
                val rect = Rect()
                this.getGlobalVisibleRect(rect)
                rect.left = rect.right - 100
                if (rect.contains(eventX, eventY)) {
                }
            }
            return super.onTouchEvent(event)

        }

}