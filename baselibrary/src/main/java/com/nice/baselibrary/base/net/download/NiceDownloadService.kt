package com.nice.baselibrary.base.net.download

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * 文件下载接口
 * @author JPlus
 * @date 2019/2/21.
 */
interface NiceDownloadService {
    @Streaming//大文件注解，防止出现OOM
    @GET
    fun downloadFile(@Header("RANGE") start:String, @Url fileUrl: String): Observable<ResponseBody>    //start断点续传的初始下载位置 fileUrl就是文件的下载地址，通过参数形式传进来
}
