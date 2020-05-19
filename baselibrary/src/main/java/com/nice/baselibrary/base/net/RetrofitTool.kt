package com.nice.baselibrary.base.net


import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

/**
 * @author JPlus
 * @date 2019/5/13.
 */
class RetrofitTool {
    companion object{
        /**
         * 文件上传，表单的方式
         */
        fun uploadFile(url: String, form: MultipartBody.Part, callBack: Callback<ResponseBody>, baseUrl:String = "http://www.google.com", okHttpClient: OkHttpClient? = null) {
            (okHttpClient ?: OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .retryOnConnectionFailure(true)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .build()).let {
                RetrofitHelper(baseUrl, it)
                        .getService()
                        .upload(form, url)
                        .enqueue(callBack)
            }
        }

        fun doPost(url: String, any: Any, callBack: Callback<ResponseBody>, baseUrl:String = "http://www.google.com", okHttpClient: OkHttpClient? = null) {
            (okHttpClient ?: OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .retryOnConnectionFailure(true)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .build())?.let {
                RetrofitHelper(baseUrl, it)
                        .getService()
                        .post(any, url)
                        .enqueue(callBack)
            }
        }

        fun doPostForm(url: String, map: MutableMap<String, String>, callBack: Callback<ResponseBody>, baseUrl:String = "http://www.google.com", okHttpClient: OkHttpClient? = null) {
            (okHttpClient ?: OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .retryOnConnectionFailure(true)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .build())?.let {
                RetrofitHelper(baseUrl, it)
                        .getService()
                        .postForm(map, url)
                        .enqueue(callBack)
            }
        }

        fun doGet(url: String, callBack: Callback<ResponseBody>, baseUrl:String = "http://www.google.com", okHttpClient: OkHttpClient? = null) {
            (okHttpClient ?: OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .retryOnConnectionFailure(true)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .build())?.let {
                RetrofitHelper(baseUrl, it)
                        .getService()
                        .get(url)
                        .enqueue(callBack)
            }
        }
    }
}