package com.nice.baselibrary.base.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory

import retrofit2.converter.gson.GsonConverterFactory

/**
 *
 * @author JPlus
 * @date 2019/2/22.
 */
class RetrofitHelper(baseUrl:String, okHttpClient: OkHttpClient) {

    private val httpRetrofit:Retrofit by lazy {
        Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
    }

    /**
     * 创建新的service
     * @return
     */
    fun getService(): HttpService {
        return httpRetrofit.create(HttpService::class.java)
    }
}