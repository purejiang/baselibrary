package com.nice.baselibrary.download

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 *
 * @author JPlus
 * @date 2019/2/22.
 */
class NiceDownloadRetrofitHelper {
    companion object {
        val baseUrl = "http://www.google.com/"
    }
    private var downloadRetrofit:Retrofit?=null

    constructor(okHttpClient: OkHttpClient){
             downloadRetrofit = Retrofit.Builder()
                     .client(okHttpClient)
                     .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
    }

    /**
     * 创建新的service
     * @return
     */
    fun getService(): NiceDownloadService?{
        return downloadRetrofit?.create(NiceDownloadService::class.java)
    }
}