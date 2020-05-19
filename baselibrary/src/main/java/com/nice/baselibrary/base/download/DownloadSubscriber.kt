package com.nice.baselibrary.base.download

import com.nice.baselibrary.base.entity.vo.DownloadInfo
import io.reactivex.observers.DisposableObserver
import okhttp3.ResponseBody

/**
 * 下载任务
 * @author JPlus
 * @date 2019/3/6.
 */
class DownloadSubscriber(private var mDownloadInfo: DownloadInfo, private var mListener: SubscriberListener) : DisposableObserver<ResponseBody>(), DownloadProgressListener {

    private var mIndex = 0

    fun setCallBack(listener: SubscriberListener) {
        mListener = listener
    }

    fun pause() {
        this.dispose()
        mDownloadInfo.status = DownloadState.DOWNLOAD_PAUSE
        mListener.onPause(mDownloadInfo)
    }

    fun cancel(){
        this.dispose()
        mDownloadInfo.status = DownloadState.DOWNLOAD_CANCEL
        mListener.onCancel(mDownloadInfo)
    }

    fun getDownloadInfo(): DownloadInfo {
        return mDownloadInfo
    }


    override fun update(read: Long, count: Long, done: Boolean) {
        //更新下载信息的内容
        var reads = read
        if (mDownloadInfo.count > count) {
            reads += (mDownloadInfo.count - count)
        } else {
            mDownloadInfo.count = count
        }
        mDownloadInfo.read = reads

        //这里注意要回传downloadInfo里的信息，不然会出现ui进度显示与下载进度不符的情况
        if (mIndex < 1) {
            mDownloadInfo.status = DownloadState.DOWNLOAD_ING
            mIndex++
            mListener.onStart(mDownloadInfo)
        }
        mListener.onUpdate(mDownloadInfo, done)

    }

    override fun onNext(responseBody: ResponseBody) {
        mListener.onNext(mDownloadInfo, responseBody)
    }

    override fun onError(e: Throwable) {
        mDownloadInfo.status = DownloadState.DOWNLOAD_FAILED
        //同步下载进度数据
        mListener.onError(mDownloadInfo, e)
        this.dispose()
    }

    override fun onComplete() {
        mDownloadInfo.status = DownloadState.DOWNLOAD_SUCCESS
        //通知下载成功
        mListener.onComplete(mDownloadInfo)
        this.dispose()
    }

    /**
     * 下载监听接口，请注意在回调中在主线程中更新UI
     */
    interface SubscriberListener {
        /**
         * 返回下载结果
         * @param info 下载信息
         * @param body 请求结果
         */
        fun onNext(info: DownloadInfo, body: ResponseBody)

        /**
         * 下载开始
         * @param info 下载信息
         */
        fun onStart(info: DownloadInfo)

        /**
         * 下载中
         * @param info 下载信息
         * @param done 是否下载完成
         */
        fun onUpdate(info: DownloadInfo, done: Boolean)

        /**
         * 下载错误
         * @param info 下载信息
         * @param e 下载异常
         */
        fun onError(info: DownloadInfo, e: Throwable)
        /**
         * 下载取消
         * @param info 下载信息
         */
        fun onCancel(info: DownloadInfo)

        /**
         * 下载完成
         * @param info 下载信息
         */
        fun onComplete(info: DownloadInfo)

        /**
         * 下载暂停
         * @param info 下载信息
         */
        fun onPause(info: DownloadInfo)
    }

}