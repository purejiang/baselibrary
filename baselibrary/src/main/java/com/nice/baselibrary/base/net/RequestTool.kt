package com.nice.baselibrary.base.net


import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Callback

/**
 * @author JPlus
 * @date 2019/5/13.
 */
object RequestTool {
    private var mDefaultClient: OkHttpClient? = null

    fun init(default: OkHttpClient) {
        mDefaultClient = default
    }

    /**
     * 文件上传，表单的方式
     */
    fun uploadFile(url: String, form: MultipartBody.Part, callBack: Callback<ResponseBody>, okHttpClient: OkHttpClient? = null) {
        mDefaultClient?.let {
            JRetrofitHelper("http://www.google.com", okHttpClient ?: it)
                    .getService()
                    .upload(form, url)
                    .enqueue(callBack)
        }
    }


    fun doPost(url: String, any: Any, callBack: Callback<ResponseBody>, okHttpClient: OkHttpClient? = null) {
        mDefaultClient?.let {
            JRetrofitHelper("http://www.google.com", okHttpClient ?: it)
                    .getService()
                    .post(any, url)
                    .enqueue(callBack)
        }
    }

    fun doPostForm(url: String, map: MutableMap<String, String>, callBack: Callback<ResponseBody>, okHttpClient: OkHttpClient? = null) {
        mDefaultClient?.let {
            JRetrofitHelper("http://www.google.com", okHttpClient ?: it)
                    .getService()
                    .postForm(map, url)
                    .enqueue(callBack)
        }
    }

    fun doGet(url: String, timeOut: Long, callBack: Callback<ResponseBody>, okHttpClient: OkHttpClient? = null) {
        mDefaultClient?.let {
            JRetrofitHelper("http://www.google.com", okHttpClient ?: it)
                    .getService()
                    .get(url)
                    .enqueue(callBack)
        }


    }
}