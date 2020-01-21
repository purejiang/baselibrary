package com.jplus.manyfunction.net


/**
 * @author JPlus
 * @date 2020/1/3.
 */
class HostList {
    companion object{
        //(默认)
        val BASE_HOST = mutableListOf(Host("http://192.168.11.184:8000", 15))
    }

    data class Host(val url:String,  val timeOut:Long)
}