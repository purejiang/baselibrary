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
class NiceDownloadSubscriber(private val niceDownloadInfo: NiceDownloadInfo, private val niceDownloadListener: NiceDownloadListener, private val niceDownloadDataSource: NiceDownloadDataSource?) : DisposableObserver<ResponseBody>(), NiceDownloadProgressListener {

    @SuppressLint("CheckResult")
    override fun update(read: Long, count: Long, done: Boolean) {
        //更新下载信息实例的内容
        var reads = read
        if (niceDownloadInfo.count > count) {
            reads += (niceDownloadInfo.count - count)
        } else {
            niceDownloadInfo.count = count
        }
        niceDownloadInfo.read = reads

        //回到主线程更新ui
        Observable.just(read).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    //如果暂停或者停止状态延迟，不需要继续发送回调，影响显示
                    if (niceDownloadInfo.status == NiceDownloadManager.DownloadStatus.DOWNLOAD_PAUSE || niceDownloadInfo.status == NiceDownloadManager.DownloadStatus.DOWNLOAD_CANCEL) {

                    } else if (niceDownloadInfo.status == NiceDownloadManager.DownloadStatus.DOWNLOAD_ING) {
                        //这里注意要回传downloadInfo里的信息，不然会出现ui进度显示与下载进度不符的情况
                        niceDownloadListener.update(niceDownloadInfo.read, niceDownloadInfo.count, done)
                    }
                }
    }

    override fun onNext(responseBody: ResponseBody) {
//       LogUtils.d("onNext:"+responseBody.string())
    }

    override fun onError(e: Throwable) {
        niceDownloadListener.downloadFailed(e)
    }

    override fun onComplete() {
        //同步下载进度数据
        niceDownloadDataSource?.modifyData(niceDownloadInfo)
        //通知下载成功
        niceDownloadListener.downloadSuccess()
    }


}