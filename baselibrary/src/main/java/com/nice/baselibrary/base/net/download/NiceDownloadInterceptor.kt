package com.nice.baselibrary.base.net.download

import com.nice.baselibrary.base.utils.LogUtils
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 下载进度拦截器
 * @author JPlus
 * @date 2019/2/22.
 */
class NiceDownloadInterceptor(niceDownloadListener: NiceDownloadProgressListener) : Interceptor {
    private var mNiceDownloadListener: NiceDownloadProgressListener? = null

    init {
        mNiceDownloadListener = niceDownloadListener
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.proceed(chain.request())
        LogUtils.instance.d("responseHeaders:\n" + original.headers().toString())
        return original.newBuilder().body(NiceDownloadResponseBody(original.body()!!, mNiceDownloadListener!!)).build()
    }
}