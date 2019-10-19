package com.nice.baselibrary.base.net.upload

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * @author JPlus
 * @date 2019/5/13.
 */
interface HttpService {
    @POST
    fun sendHttp(@Body any:Any, @Url url:String):Call<ResponseBody>
}