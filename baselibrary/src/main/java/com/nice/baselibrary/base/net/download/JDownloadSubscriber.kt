package com.nice.baselibrary.base.net.download

import android.annotation.SuppressLint
import com.nice.baselibrary.base.entity.vo.JDownloadInfo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import okhttp3.ResponseBody

/**
 * 下载任务
 * @author JPlus
 * @date 2019/3/6.
 */
class JDownloadSubscriber(private var downloadInfo: JDownloadInfo, private var listener: SubscriberListener) : DisposableObserver<ResponseBody>(), JDownloadProgressListener {

    private var mIndex = 0

    fun setCallBack(ls: SubscriberListener) {
        listener = ls
    }

    fun setInfo(info: JDownloadInfo) {
        downloadInfo = info
    }

    fun pause() {
        this.dispose()
        downloadInfo.status = JDownloadState.DOWNLOAD_PAUSE
        listener.onPause(downloadInfo)
    }

    fun getDownloadInfo(): JDownloadInfo {
        return downloadInfo
    }

    @SuppressLint("CheckResult")
    override fun update(read: Long, count: Long, done: Boolean) {
        //更新下载信息实例的内容
        var reads = read
        if (downloadInfo.count > count) {
            reads += (downloadInfo.count - count)
        } else {
            downloadInfo.count = count
        }
        downloadInfo.read = reads

        //回到主线程更新ui
        Observable.just(read).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    //这里注意要回传downloadInfo里的信息，不然会出现ui进度显示与下载进度不符的情况
                    if (mIndex < 1) {
                        downloadInfo.status = JDownloadState.DOWNLOAD_ING
                        mIndex++
                        listener.onStart(downloadInfo)
                    }
                    listener.onUpdate(downloadInfo, done)
                }
    }

    override fun onNext(responseBody: ResponseBody) {
        listener.onNext(downloadInfo, responseBody)
    }

    override fun onError(e: Throwable) {
        downloadInfo.status = JDownloadState.DOWNLOAD_FAILED
        //同步下载进度数据
        listener.onError(downloadInfo, e)
        this.dispose()
    }

    override fun onComplete() {
        downloadInfo.status = JDownloadState.DOWNLOAD_SUCCESS
        //通知下载成功
        listener.onComplete(downloadInfo)
        this.dispose()
    }

    interface SubscriberListener {
        fun onNext(downloadInfo: JDownloadInfo, responseBody: ResponseBody)
        fun onStart(downloadInfo: JDownloadInfo)
        fun onUpdate(downloadInfo: JDownloadInfo, done: Boolean)
        fun onError(downloadInfo: JDownloadInfo, e: Throwable)
        fun onComplete(downloadInfo: JDownloadInfo)
        fun onPause(downloadInfo: JDownloadInfo)
    }

}