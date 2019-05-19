package com.nice.baselibrary.upload

import android.util.ArrayMap
import com.nice.baselibrary.base.utils.LogUtils
import com.nice.baselibrary.download.NiceDownloadManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Multipart
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author JPlus
 * @date 2019/5/13.
 */
class UploadManager {

    companion object{
        fun uploadFile(url:String, photo: MultipartBody.Part, callBack:Callback<ResponseBody>){
            val okhttp = OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .retryOnConnectionFailure(true)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build()

            UploadReHelper(okhttp, "http://www.google.com")
                    .getService()
                    ?.uploadFiles(photo, url)
                    ?.enqueue(callBack)

        }
    }
}