package com.nice.baselibrary.base.download

import com.nice.baselibrary.base.utils.LogUtils
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 下载进度拦截器
 * @author JPlus
 * @date 2019/2/22.
 */
class DownloadInterceptor(private val downloadListener: DownloadProgressListener) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.proceed(chain.request())
        LogUtils.d("download[responseHeaders]:\n${original.headers()}")
        return original.newBuilder().body(DownloadResponseBody(original.body(), downloadListener)).build()
    }
}