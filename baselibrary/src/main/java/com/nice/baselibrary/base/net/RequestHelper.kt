package com.nice.baselibrary.base.net



import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * 网络请求帮助类
 * @author JPlus
 * @date 2019/5/13.
 */

/**
 * 文件上传，表单的方式
 * @param url
 * @param form
 * @param observer
 * @param baseUrl
 * @param client
 */
fun uploadFile(url: String, form: MultipartBody.Part, observer: DisposableObserver<ResponseBody>, baseUrl: String, client: OkHttpClient? = null) {
    (client ?: OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()).let {
        RetrofitHelper(baseUrl, it)
                .getService()
                .upload(form, url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
    }
}

/**
 * post请求
 * @param url
 * @param any
 * @param observer
 * @param baseUrl
 * @param client
 */
fun doPost(url: String, any: Any, observer: DisposableObserver<ResponseBody>, baseUrl: String, client: OkHttpClient? = null) {
    (client ?: OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .retryOnConnectionFailure(true)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()).let {
        RetrofitHelper(baseUrl, it)
                .getService()
                .post(any, url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
    }
}
/**
 * post请求，表单形式
 * @param url
 * @param map
 * @param observer
 * @param baseUrl
 * @param client
 */
fun doPostForm(url: String, map: MutableMap<String, String>, observer: DisposableObserver<ResponseBody>, baseUrl: String, client: OkHttpClient? = null) {
    (client ?: OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .retryOnConnectionFailure(true)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()).let {
        RetrofitHelper(baseUrl, it)
                .getService()
                .postForm(map, url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
    }
}
/**
 *  get请求
 * @param url
 * @param observer
 * @param baseUrl
 * @param client
 */
fun doGet(url: String, observer: DisposableObserver<ResponseBody>, baseUrl: String, client: OkHttpClient? = null) {
    (client ?: OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .retryOnConnectionFailure(true)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()).let {
        RetrofitHelper(baseUrl, it)
                .getService()
                .get(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
    }
}
