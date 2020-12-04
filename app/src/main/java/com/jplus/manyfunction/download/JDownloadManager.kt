package com.jplus.manyfunction.download


import com.nice.baselibrary.base.source.DataSource
import com.nice.baselibrary.base.entity.vo.DownloadInfo
import com.nice.baselibrary.base.net.RetrofitHelper
import com.nice.baselibrary.base.download.DownloadInterceptor
import com.nice.baselibrary.base.download.DownloadState
import com.nice.baselibrary.base.download.DownloadSubscriber
import com.nice.baselibrary.base.utils.LogUtils
import com.nice.baselibrary.base.utils.writeRandomAccessFile
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * 下载管理
 * @author JPlus
 * @date 2019/2/21.
 */
object JDownloadManager {
    //下载
    private val M_DOWNLOAD_QUEUE: MutableMap<Int, DownloadSubscriber> by lazy {
        mutableMapOf<Int, DownloadSubscriber>()
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
    fun addNewDownload(oldDownloadInfo: DownloadInfo, downloadCallback: DownloadCallback?, downloadDataSource: DownloadDataSource?, isStart: Boolean) {
        LogUtils.d("download[add]--oldDownloadInfo:$oldDownloadInfo", "pipa")

        //新建订阅，添加对应关系
        DownloadSubscriber(oldDownloadInfo, getListener(downloadCallback, downloadDataSource)).let {
            //添加到下载队列
            M_DOWNLOAD_QUEUE[oldDownloadInfo.id] = it
            LogUtils.d("download[add]--oldDownloadInfo:${oldDownloadInfo.id}", "pipa")
            //添加到数据库
            downloadDataSource?.modifyData(it.getDownloadInfo()){result->
                    //开始下载
                    if (result) runDownload(it, downloadDataSource)
                }
        }
    }

    fun isInQueue(id: Int): Boolean {
        return M_DOWNLOAD_QUEUE.containsKey(id)
    }

    private fun getListener(downloadCallback: DownloadCallback?, downloadDataSource: DownloadDataSource?): DownloadSubscriber.SubscriberListener {
        return object : DownloadSubscriber.SubscriberListener {
            override fun onStart(info: DownloadInfo) {
                LogUtils.d("onStart")
                downloadDataSource?.modifyData(info){
                }
            }

            //绑定监听回调
            override fun onNext(info: DownloadInfo, body: ResponseBody) {
                LogUtils.d("onNext")
                downloadDataSource?.modifyData(info){
                }
                downloadCallback?.next(body)
            }

            override fun onUpdate(info: DownloadInfo, done: Boolean) {
                LogUtils.d("onUpdate")
                downloadCallback?.update(info.read, info.count, done)
            }

            override fun onError(info: DownloadInfo, e: Throwable) {
                LogUtils.d("onError")
                downloadDataSource?.modifyData(info){
                }
                downloadCallback?.downloadFailed(e)
            }

            override fun onCancel(info: DownloadInfo) {

            }

            override fun onComplete(info: DownloadInfo) {
                LogUtils.d("onComplete")
                downloadDataSource?.modifyData(info){
                }
                downloadCallback?.downloadSuccess()
                M_DOWNLOAD_QUEUE.remove(info.id)
            }

            override fun onPause(info: DownloadInfo) {
                LogUtils.d("onPause")
                downloadDataSource?.modifyData(info){
                }
                downloadCallback?.pause()
            }
        }
    }

    /**
     * 继续下载
     * @param id
     * @param downloadCallback
     * @param downloadDataSource
     */
    fun reStartDownload(id: Int, downloadCallback: DownloadCallback, downloadDataSource: DownloadDataSource) {
        //包含id则复用原有的下载任务，不包含id则重新构建下载任务
        if (M_DOWNLOAD_QUEUE.containsKey(id)) {
            LogUtils.d("download[restart]: id:${id}")
            M_DOWNLOAD_QUEUE.remove(id)
        }
        //暂停后重新从数据取数据，新建订阅并绑定监听回调
        LogUtils.d("重新构建下载任务")
        downloadDataSource.getData(id,object :DataSource.LoadDataCallback<DownloadInfo>{
            override fun onDataLoaded(data: DownloadInfo?) {
                data?.let{
                    addNewDownload(it, downloadCallback, downloadDataSource, true)
                }
            }

            override fun onDataNotAvailable(throwable: Throwable) {

            }
        })
    }

    /**
     * 重新绑定监听
     * @param id
     * @param downloadCallback
     * @param downloadDataSource
     */
    fun reBindListener(id: Int, downloadCallback: DownloadCallback, downloadDataSource: DownloadDataSource) {
        LogUtils.d("download[reBind]: id:${id}", "pipa")
        //包含id则复用原有的下载任务
        if (M_DOWNLOAD_QUEUE.containsKey(id)) {
            LogUtils.d("download[reBind]:111111 id:${id}", "pipa")
            M_DOWNLOAD_QUEUE[id]?.let {
                //下载中重新绑定监听回调
                if (it.getDownloadInfo().status == DownloadState.DOWNLOAD_ING) {
                    LogUtils.d("下载中重新绑定监听回调")
                    it.setCallBack(getListener(downloadCallback, downloadDataSource))
                }
            }
        }
    }

    /**
     * 暂停下载
     * @param id
     */
    fun pauseDownload(id: Int) {
        LogUtils.d("download[pause]: id:${id}, ${M_DOWNLOAD_QUEUE.containsKey(id)}")
        M_DOWNLOAD_QUEUE[id]?.let {
            //保存到数据库
            it.pause()
        }
    }

    /**
     * 下载完成
     * @param id
     */
    fun finishDownload(id: Int) {
        M_DOWNLOAD_QUEUE.remove(id)

    }

    /**
     * 取消下载
     * @param downloadInfo
     */
    fun cancelDownload(downloadInfo: DownloadInfo, jDownloadDataSource: DownloadDataSource) {
//        LogUtils.d("download[cancel]: status:${jDownloadInfo.status}")

    }

    /**
     * 删除下载
     * @param downloadInfos
     */
    fun deleteDownloads(downloadInfos: MutableList<DownloadInfo>) {
        LogUtils.d("deleteDownloads")
        for (download in downloadInfos) {
            M_DOWNLOAD_QUEUE.remove(download.id)
        }
    }

    /**
     * 开启下载
     * @param subscriber
     * @param downloadDataSource
     */
    private fun runDownload(subscriber: DownloadSubscriber, downloadDataSource: DownloadDataSource?) {
        val info = subscriber.getDownloadInfo().apply {
            status = DownloadState.DOWNLOAD_ING
        }
        LogUtils.d("download[run]: info:${subscriber.getDownloadInfo()}")
        //添加到数据库
        downloadDataSource?.modifyData(info){
        }
        //开始下载

        //新建okhttp客户端，拦截器监听下载进度

        subscriber.getDownloadInfo().let {
            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(DownloadInterceptor(subscriber))
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build()

            //新建下载服务，添加对应关系
            RetrofitHelper("https://www.google.com", okHttpClient).getService()
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
        M_DOWNLOAD_QUEUE.clear()
    }
}
