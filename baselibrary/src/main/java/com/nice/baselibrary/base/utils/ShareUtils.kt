package com.nice.baselibrary.base.utils

import android.app.Activity
import android.content.Intent
import java.io.File

/**
 * @author JPlus
 * @date 2019/11/23.
 */
object ShareUtils {
    fun shareFile(activity: Activity, path:String, title:String, authority:String){
        val uri = activity.path2Uri(path, authority)
        var shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        //其中imgUri为图片的标识符
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.type = "*/*"
        //切记需要使用Intent.createChooser，否则会出现别样的应用选择框，您可以试试
        shareIntent = Intent.createChooser(shareIntent, title)
        activity.startActivity(shareIntent)
    }
}