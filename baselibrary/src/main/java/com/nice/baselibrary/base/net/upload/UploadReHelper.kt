package com.nice.baselibrary.base.net.upload

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author JPlus
 * @date 2019/5/13.
 */
class UploadReHelper {
    private var mRetrofit:Retrofit?=null

    constructor(okHttpClient: OkHttpClient,baseUrl:String){
        mRetrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

    }
    /**
     * 创建新的service
     * @return
     */
    fun getService(): UploadService?{
        return mRetrofit?.create(UploadService::class.java)
    }
}