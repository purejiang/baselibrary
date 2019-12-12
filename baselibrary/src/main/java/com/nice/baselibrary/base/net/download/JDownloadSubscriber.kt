package com.nice.baselibrary.base.net.download

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import okhttp3.ResponseBody

/**
 *
 * @author JPlus
 * @date 2019/3/6.
 */
class JDownloadSubscriber(private val jDownloadInfo: JDownloadInfo, private val jDownloadCallback: JDownloadCallback, private val jDownloadDataSource: JDownloadDataSource?) : DisposableObserver<ResponseBody>(), JDownloadProgressListener {

    @SuppressLint("CheckResult")
    override fun update(read: Long, count: Long, done: Boolean) {
        //更新下载信息实例的内容
        var reads = read
        if (jDownloadInfo.count > count) {
            reads += (jDownloadInfo.count - count)
        } else {
            jDownloadInfo.count = count
        }
        jDownloadInfo.read = reads

        //回到主线程更新ui
        Observable.just(read).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    //如果暂停或者停止状态延迟，不需要继续发送回调，影响显示
                    when(jDownloadInfo.status) {
                         JDownloadState.DOWNLOAD_PAUSE -> {

                        }
                        JDownloadState.DOWNLOAD_CANCEL, JDownloadState.DOWNLOAD_FAILED, JDownloadState.DOWNLOAD_SUCCESS -> {
                            //销毁
                            this.dispose()
                        }
                        JDownloadState.DOWNLOAD_ING -> {
                            //这里注意要回传downloadInfo里的信息，不然会出现ui进度显示与下载进度不符的情况
                            jDownloadCallback.update(jDownloadInfo.read, jDownloadInfo.count, done)
                        }
                    }
                }
    }

    override fun onNext(responseBody: ResponseBody) {
//       LogUtils.d("onNext:"+responseBody.string())
    }

    override fun onError(e: Throwable) {
        //同步下载进度数据
        jDownloadInfo.status = JDownloadState.DOWNLOAD_FAILED
        jDownloadDataSource?.modifyData(jDownloadInfo)
        jDownloadCallback.downloadFailed(e)
    }

    override fun onComplete() {
        //同步下载进度数据
        jDownloadInfo.status = JDownloadState.DOWNLOAD_SUCCESS
        jDownloadDataSource?.modifyData(jDownloadInfo)
        //通知下载成功
        jDownloadCallback.downloadSuccess()
        this.dispose()
    }
}