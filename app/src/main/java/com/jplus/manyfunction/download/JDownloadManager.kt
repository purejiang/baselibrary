package com.jplus.manyfunction.download


import com.nice.baselibrary.base.net.JRetrofitHelper
import com.nice.baselibrary.base.net.download.JDownloadInterceptor
import com.nice.baselibrary.base.net.download.JDownloadState
import com.nice.baselibrary.base.net.download.JDownloadCallback
import com.nice.baselibrary.base.net.download.JDownloadSubscriber
import com.nice.baselibrary.base.entity.vo.JDownloadInfo
import com.nice.baselibrary.base.utils.LogUtils
import com.nice.baselibrary.base.utils.writeRandomAccessFile
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


/**
 * 下载管理
 * @author JPlus
 * @date 2019/2/21.
 */
object JDownloadManager {
    //下载
    private val mDownloadQueue: MutableMap<Int, JDownloadSubscriber> by lazy {
        mutableMapOf<Int, JDownloadSubscriber>()
    }
    //最大下载数
    private val mMax: Int = 8
    //同时下载数
    private var mCount: Int = 1

    /**
     * 新增下载
     * @param oldDownloadInfo
     * @param downloadCallback
     * @param downloadDataSource
     */
    fun addNewDownload(oldDownloadInfo: JDownloadInfo, downloadCallback: JDownloadCallback, downloadDataSource: JDownloadDataSource?, isStart:Boolean) {
        LogUtils.d("download[add]--oldDownloadInfo:$oldDownloadInfo")

        //新建订阅，添加对应关系
        JDownloadSubscriber(oldDownloadInfo, getListener(downloadCallback, downloadDataSource)).let{
            //添加到下载队列
            mDownloadQueue[oldDownloadInfo.id] = it
            //添加到数据库
            downloadDataSource?.modifyData(it.getDownloadInfo())
            //开始下载
            if (isStart) runDownload(it, downloadDataSource)
            }
        }

    fun isInQueue(id: Int):Boolean {
        return mDownloadQueue.containsKey(id)
    }


    private fun getListener(downloadCallback: JDownloadCallback, downloadDataSource: JDownloadDataSource?): JDownloadSubscriber.SubscriberListener{
        return object : JDownloadSubscriber.SubscriberListener{
            override fun onStart(downloadInfo: JDownloadInfo) {
                LogUtils.d("onStart")
                downloadDataSource?.modifyData(downloadInfo)
            }

            //绑定监听回调
            override fun onNext(downloadInfo: JDownloadInfo, responseBody: ResponseBody) {
                LogUtils.d("onNext")
                downloadDataSource?.modifyData(downloadInfo)
                downloadCallback.next(responseBody)
            }

            override fun onUpdate(downloadInfo: JDownloadInfo, done:Boolean) {
                LogUtils.d("onUpdate")
                downloadCallback.update(downloadInfo.read, downloadInfo.count, done)
            }

            override fun onError(downloadInfo: JDownloadInfo, e: Throwable) {
                LogUtils.d("onError")
                downloadDataSource?.modifyData(downloadInfo)
                downloadCallback.downloadFailed(e)
            }

            override fun onComplete(downloadInfo: JDownloadInfo) {
                LogUtils.d("onComplete")
                downloadDataSource?.modifyData(downloadInfo)
                downloadCallback.downloadSuccess()
                mDownloadQueue.remove(downloadInfo.id)
            }

            override fun onPause(downloadInfo: JDownloadInfo) {
                LogUtils.d("onPause")
                downloadDataSource?.modifyData(downloadInfo)
                downloadCallback.pause()
            }
        }
    }

    /**
     * 继续下载
     * @param id
     * @param downloadCallback
     * @param downloadDataSource
     */
    fun reStartDownload(id: Int, downloadCallback: JDownloadCallback, downloadDataSource: JDownloadDataSource) {
        //包含id则复用原有的下载任务，不包含id则重新构建下载任务
        if (mDownloadQueue.containsKey(id)) {
            LogUtils.d("download[restart]: id:${id}")
            mDownloadQueue.remove(id)
        }
        //暂停后重新从数据取数据，新建订阅并绑定监听回调
        LogUtils.d("重新构建下载任务")
        val downloadInfo = downloadDataSource.getData(id)
        downloadInfo?.let { info ->
            addNewDownload(info, downloadCallback, downloadDataSource, true)
        }
    }
    /**
     * 重新绑定监听
     * @param id
     * @param downloadCallback
     * @param downloadDataSource
     */
    fun reBindListener(id: Int, downloadCallback: JDownloadCallback, downloadDataSource: JDownloadDataSource) {
        //包含id则复用原有的下载任务
        if (mDownloadQueue.containsKey(id)) {
            LogUtils.d("download[reBind]: id:${id}")
            mDownloadQueue[id]?.let {
                //下载中重新绑定监听回调
                if (it.getDownloadInfo().status == JDownloadState.DOWNLOAD_ING) {
                    LogUtils.d("下载中重新绑定监听回调")
                    it.setCallBack(getListener(downloadCallback, downloadDataSource))
                }
            }
        }
    }

    /**
     * 暂停下载
     * @param id
     * @param downloadDataSource
     */
    fun pauseDownload(id: Int) {
        LogUtils.d("download[pause]: id:${id}, ${mDownloadQueue.containsKey(id)}")
        mDownloadQueue[id]?.let {
            //保存到数据库
            it.pause()

        }
    }


    /**
     * 取消下载
     * @param jDownloadInfo
     */
    fun cancelDownload(jDownloadInfo: JDownloadInfo, jDownloadDataSource: JDownloadDataSource) {
//        LogUtils.d("download[cancel]: status:${jDownloadInfo.status}")

    }

    /**
     * 删除下载
     * @param downloadInfos
     */
    fun deleteDownloads(downloadInfos: MutableList<JDownloadInfo>) {
        LogUtils.d("deleteDownloads")
        for (download in downloadInfos) {
            mDownloadQueue.remove(download.id)
        }
    }

    /**
     * 开启下载
     * @param subscriber
     * @param downloadDataSource
     */
    private fun runDownload(subscriber: JDownloadSubscriber, downloadDataSource: JDownloadDataSource?) {
        //添加到数据库
        downloadDataSource?.modifyData(subscriber.getDownloadInfo().apply {
            status = JDownloadState.DOWNLOAD_ING
        })
        //开始下载
        LogUtils.d("download[run]: info:${subscriber.getDownloadInfo()}")
        //新建okhttp客户端，拦截器监听下载进度

        subscriber.getDownloadInfo().let {
            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(JDownloadInterceptor(subscriber))
                    .build()

            //新建下载服务，添加对应关系
            JRetrofitHelper("https://www.google.com", okHttpClient).getService()
                    .download("bytes=${it.read}-", it.url)
                    .subscribeOn(Schedulers.io())
                    ?.doOnNext { res: ResponseBody ->
                        File(it.path).writeRandomAccessFile(res, it.read, it.count)
                    }
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(subscriber)
        }

    }

    fun clear() {
        mDownloadQueue.clear()
    }

    /**
     * 获取下载信息
     * @param url
     * @param countCallback
     */
    fun getUrlContentLength(url: String, countCallback: ContentLengthCallBack) {
        //协程进行网络请求
        GlobalScope.launch {
            try {
                // 创建连接
                val onUrl = URL(url)
                val conn = onUrl.openConnection() as HttpURLConnection
                //处理下载读取长度为-1 问题
                countCallback.onCallback(conn.contentLength * 1L)
            } catch (e: IOException) {
                e.printStackTrace()
                countCallback.onCallback(-1L)
            }
        }
    }

    interface ContentLengthCallBack {
        /**
         * 测试网络联通情况，获取文件总大小
         * @param count 文件大小，返回 -1 则连接异常
         */
        fun onCallback(count: Long)
    }
}
