package com.jplus.manyfunction.net.dto

import com.google.gson.JsonObject

/**
 * @author JPlus
 * @date 2019/7/2.
 */
data class BaseResponse(val result_code:Int,
                        val result_msg:String,
                        val result_data:JsonObject){
    companion object{
        const val SUCCESS = 1
        const val FAILED = 0
    }
}