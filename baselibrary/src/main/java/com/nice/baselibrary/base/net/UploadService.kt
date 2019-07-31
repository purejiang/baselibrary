package com.nice.baselibrary.base.net

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Url

/**
 * @author JPlus
 * @date 2019/5/13.
 */
interface UploadService {
    @Multipart
    @POST
    fun uploadFiles(@Part photo:MultipartBody.Part, @Url url:String):Call<ResponseBody>
}