package com.nice.baselibrary.base.net

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * 网络请求接口
 * @author JPlus
 * @date 2019/2/21.
 */
interface JHttpService {
    @Streaming//大文件注解，防止出现OOM
    @GET
    fun download(@Header("RANGE") start:String, @Url fileUrl: String): Observable<ResponseBody>    //start断点续传的初始下载位置 fileUrl就是文件的下载地址，通过参数形式传进来

    @POST
    fun post(@Body any:Any, @Url url:String): Call<ResponseBody> //post请求

    @GET
    fun get(@Url url:String): Call<ResponseBody> //get请求

    @Multipart
    @POST
    fun upload(@Part photo: MultipartBody.Part, @Url url:String):Call<ResponseBody> // 文件上传
}
