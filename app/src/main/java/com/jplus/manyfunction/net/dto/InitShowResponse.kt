package com.jplus.manyfunction.net.dto

/**
 * @author JPlus
 * @date 2020/1/3.
 */
data class InitShowResponse( val id:Int,
                             val is_h5:Boolean,
                             val url:String,
                             val title:String,
                             val message:String,
                             val is_cancel:Boolean,
                             val confirm_msg:String,
                             val cancel_msg:String,
                             val middle_msg:String,
                             val sign:String)