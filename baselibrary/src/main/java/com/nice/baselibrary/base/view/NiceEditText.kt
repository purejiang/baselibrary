package com.nice.baselibrary.base.view

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.EditText
import com.nice.baselibrary.R
import com.nice.baselibrary.base.utils.LogUtils

/**
 * 可删除的输入框
 * @author JPlus
 * @date 2019/2/19.
 */
class NiceEditText :AppCompatEditText {
    private var mDeleted:Drawable?=null
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
        this.background = ContextCompat.getDrawable(context, R.drawable.edit_base_circle_background)
            mDeleted = ContextCompat.getDrawable(context, R.mipmap.ic_del_grey)
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable) {
                    setDrawable()
                }
            })
            setDrawable()
        }

    /**
     * 设置删除图
     */
    private fun setDrawable() {
            if (length() < 1) {
                setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            } else {
                setCompoundDrawablesWithIntrinsicBounds(null, null, mDeleted, null)
            }
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            if (mDeleted != null && event.action == MotionEvent.ACTION_UP) {
                val eventX = event.rawX.toInt()
                val eventY = event.rawY.toInt()
                val rect = Rect()
                this.getGlobalVisibleRect(rect)
                rect.left = rect.right - 100
                if (rect.contains(eventX, eventY)) {
                    setText("")
                }
            }
            return super.onTouchEvent(event)

        }

}