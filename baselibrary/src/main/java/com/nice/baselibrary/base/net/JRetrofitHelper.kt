package com.nice.baselibrary.base.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 *
 * @author JPlus
 * @date 2019/2/22.
 */
class JRetrofitHelper( baseUrl:String, okHttpClient: OkHttpClient) {

    private val httpRetrofit:Retrofit by lazy {
        Retrofit.Builder()
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
    fun getService(): JHttpService {
        return httpRetrofit.create(JHttpService::class.java)
    }
}