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
object RequestTool {


    /**
     * 文件上传，表单的方式
     */
    fun uploadFile(url: String, form: MultipartBody.Part, callBack: Callback<ResponseBody>, okHttpClient: OkHttpClient? = null) {
        (okHttpClient ?: OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build()).let {
            JRetrofitHelper("http://www.google.com", it)
                    .getService()
                    .upload(form, url)
                    .enqueue(callBack)
        }
    }

    fun doPost(url: String, any: Any, callBack: Callback<ResponseBody>, okHttpClient: OkHttpClient? = null) {
        (okHttpClient ?: OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build())?.let {
            JRetrofitHelper("http://www.google.com", it)
                    .getService()
                    .post(any, url)
                    .enqueue(callBack)
        }
    }

    fun doPostForm(url: String, map: MutableMap<String, String>, callBack: Callback<ResponseBody>, okHttpClient: OkHttpClient? = null) {
        (okHttpClient ?: OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build())?.let {
            JRetrofitHelper("http://www.google.com", it)
                    .getService()
                    .postForm(map, url)
                    .enqueue(callBack)
        }
    }

    fun doGet(url: String, callBack: Callback<ResponseBody>, okHttpClient: OkHttpClient? = null) {
        (okHttpClient ?: OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build())?.let {
            JRetrofitHelper("http://www.google.com", it)
                    .getService()
                    .get(url)
                    .enqueue(callBack)
        }
    }
    /**
     * 获取url是否可达
     * @param  url 地址
     * @param waitMilliSecond 等待超时时间，以ms为单位
     * @return 是否可达
     */
    fun canConnected(url:String, waitMilliSecond:Int):Boolean{
        try{
            val conn = URL(url).openConnection() as HttpURLConnection
            conn.useCaches = false
            conn.instanceFollowRedirects = true
            conn.connectTimeout = waitMilliSecond
            conn.readTimeout = waitMilliSecond

            //HTTP connect
            try {
                conn.connect()
            } catch(e:Exception) {
                e.printStackTrace()
                return false
            }

            val code = conn.responseCode
            if ((code >= 100) && (code < 400)){
                return true
            }

            return false
        }catch (e:Exception){
            e.printStackTrace()
            return false
        }
    }
}