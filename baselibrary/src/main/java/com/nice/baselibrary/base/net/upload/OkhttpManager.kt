package com.nice.baselibrary.base.net.upload

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import java.util.concurrent.TimeUnit

/**
 * @author JPlus
 * @date 2019/5/13.
 */
class OkhttpManager {

    companion object{
        fun uploadFile(url:String, photo: MultipartBody.Part, callBack:Callback<ResponseBody>){
            val okhttp = OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .retryOnConnectionFailure(true)
                    .connectTimeout(3, TimeUnit.SECONDS)
                    .build()

            UploadReHelper(okhttp, "http://www.google.com")
                    .getService()
                    ?.uploadFiles(photo, url)
                    ?.enqueue(callBack)

        }
        fun doPost(url:String, any: Any, callBack:Callback<ResponseBody>){
            val okhttp = OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .retryOnConnectionFailure(true)
                    .connectTimeout(3, TimeUnit.SECONDS)
                    .build()

            HttpReHelper(okhttp, "http://www.google.com")
                    .getService()
                    ?.sendHttp(any, url)
                    ?.enqueue(callBack)
        }
    }
}