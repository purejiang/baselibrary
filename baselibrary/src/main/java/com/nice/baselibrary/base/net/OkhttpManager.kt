package com.nice.baselibrary.base.net


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
        fun uploadFile(url:String, photo: MultipartBody.Part, timeOut:Long, callBack:Callback<ResponseBody>){
            val okhttp = OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .retryOnConnectionFailure(true)
                    .connectTimeout(timeOut, TimeUnit.SECONDS)
                    .build()

            JRetrofitHelper("http://www.google.com", okhttp)
                    .getService()
                    .upload(photo, url)
                    .enqueue(callBack)
        }

        fun doPost(url:String, any: Any, timeOut:Long, callBack:Callback<ResponseBody>){
            val okhttpClient = OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .retryOnConnectionFailure(true)
                    .connectTimeout(timeOut, TimeUnit.SECONDS)
                    .build()

            JRetrofitHelper( "http://www.google.com", okhttpClient)
                    .getService()
                    .post(any, url)
                    .enqueue(callBack)
        }
    }
}