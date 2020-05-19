package com.nice.baselibrary.widget

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import com.nice.baselibrary.R

/**
 * 可清空的输入框
 * @author JPlus
 * @date 2019/2/19.
 */
class JEditTextView(context: Context, attrs: AttributeSet?) : AppCompatEditText(context, attrs) {
    private var mRightPic: Drawable? = null
    private var mCallBack:(()->Unit)?=null
    private val mTypeArray by lazy {
        context.obtainStyledAttributes(attrs, R.styleable.JEditTextView)
    }

    init {
        mRightPic = mTypeArray.getDrawable(R.styleable.JEditTextView_right_pic)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if(length()==0) mCallBack?.let{it()}

                //设置editText右图
                mRightPic?.let {
                    //使用setCompoundDrawables()是没有效果的
                    setCompoundDrawablesWithIntrinsicBounds(null, null, if (length() > 0) it else null, null)
                }
            }
        })
    }

    fun setEditCallBack(callBack:(()->Unit)){
        mCallBack = callBack
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action){
            MotionEvent.ACTION_UP->{
                if (mRightPic != null) {
                    val eventX = event.rawX.toInt()
                    val eventY = event.rawY.toInt()
                    val rect = Rect()
                    this.getGlobalVisibleRect(rect)
                    rect.left = rect.right - 100
                    if (rect.contains(eventX, eventY)) {
                        this.text?.clear()
                    }
                }
                performClick()
            }
        }
        return super.onTouchEvent(event)
    }
}