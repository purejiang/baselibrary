package com.nice.baselibrary.base.utils

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.nice.baselibrary.R
import com.nice.baselibrary.widget.dialog.JAlertDialog
import com.nice.baselibrary.widget.dialog.JDialog

/**
 * 弹出控件方法集
 * @author JPlus
 * @date 2019/2/12.
 */

/**
 * 创建一个弹出框
 * @param resInt
 * @param resIntArray
 * @param styleable
 */
fun Context.createDialog(resInt: Int, resIntArray: Array<Int>, styleable: Int): JDialog {
    return JDialog(this, resInt, resIntArray, styleable)
}

/**
 * 返回一个弹出框Builder
 * @return Builder
 */
fun FragmentActivity.getAlertDialog(): JAlertDialog.Builder {
    return JAlertDialog.Builder(this.supportFragmentManager)
}

/**
 * 创建一个弹出框
 * @param resInt
 * @param resIntArray
 * @param size dialog大小样式
 */
fun Context.createDialog(resInt: Int, resIntArray: Array<Int>, size: String): JDialog {
    var styleable = R.style.NormalNiceDialog
    when (size) {
        "normal" -> styleable = R.style.NormalNiceDialog
        "small" -> styleable = R.style.SmallNiceDialog
        "big" -> styleable = R.style.BigNiceDialog
        "smaller" -> styleable = R.style.SmallerNiceDialog
    }
    return JDialog(this, resInt, resIntArray, styleable)
}

/**
 * 创建一个弹出框，使用默认布局
 * @param size
 */
fun Context.createDialog(size: String): JDialog {
    val resInt = R.layout.view_dialog
    val resIntArray = arrayOf(R.id.bt_dialog_title, R.id.btv_dialog_message, R.id.btv_dialog_cancel, R.id.btv_dialog_confirm, R.id.cp_loading)
    return createDialog(resInt, resIntArray, size)
}

fun Context.showNormalToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showGravityToast(message: String) {
    val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
}

fun showProgressDialog() {

}
