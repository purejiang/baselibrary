package com.nice.baselibrary.base.net.download


import android.content.Context
import com.nice.baselibrary.base.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.channels.FileChannel


/**
 * 下载管理
 * @author JPlus
 * @date 2019/2/21.
 */
class NiceDownloadManager private constructor() {
    companion object {
        val instance: NiceDownloadManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NiceDownloadManager() }
    }

    private var mContext: Context? = null

    private val mInfo2ServiceNice: HashMap<String, NiceDownloadService> by lazy {
        HashMap<String, NiceDownloadService>()
    }
    private val mInfo2Subscribe: HashMap<String, NiceDownloadSubscriber>by lazy {
        HashMap<String, NiceDownloadSubscriber>()
    }

    /**
     * 初始化
     * @param context
     */
    fun init(context: Context) {
        mContext = context
    }

    /**
     * 新增下载
     * @param niceDownloadInfo
     * @param niceDownloadListener
     * @param niceDownloadDataSource
     */
    fun startNewDownload(niceDownloadInfo: NiceDownloadInfo, niceDownloadListener: NiceDownloadListener, niceDownloadDataSource: NiceDownloadDataSource?) {
        LogUtils.instance.d("download[start——new]:info:${niceDownloadInfo}")
        //新建绑定，添加对应关系
        val subscriber = NiceDownloadSubscriber(niceDownloadInfo, niceDownloadListener, niceDownloadDataSource)
        mInfo2Subscribe[niceDownloadInfo.url] = subscriber
        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(NiceDownloadInterceptor(subscriber))
                .build()
        //新建下载服务，添加对应关系
        val downloadService = NiceDownloadRetrofitHelper(okHttpClient).getService()!!
        mInfo2ServiceNice[niceDownloadInfo.url] = downloadService
        //开始下载
        startDownload(downloadService, niceDownloadInfo, subscriber)
    }

    /**
     * 继续下载
     * @param niceDownloadInfo
     * @param niceDownloadListener
     */
    fun reStartDownload(niceDownloadInfo: NiceDownloadInfo, niceDownloadListener: NiceDownloadListener, niceDownloadDataSource: NiceDownloadDataSource?) {
        LogUtils.instance.d("download[restart]: info:${niceDownloadInfo}")
        //列表包含该url则复用map中指向的service对象，不包含该url则重新构建service进行下载
        if(mInfo2ServiceNice.containsKey(niceDownloadInfo.url)){
            val subscriber =  NiceDownloadSubscriber(niceDownloadInfo, niceDownloadListener, niceDownloadDataSource)
            mInfo2Subscribe[niceDownloadInfo.url] = subscriber
            startDownload(mInfo2ServiceNice[niceDownloadInfo.url]!!, niceDownloadInfo, subscriber)
        }else{
            startNewDownload(niceDownloadInfo, niceDownloadListener, niceDownloadDataSource)
        }
    }

    /**
     * 暂停下载
     * @param niceDownloadInfo
     */
    fun pauseDownload(niceDownloadInfo: NiceDownloadInfo) {
        LogUtils.instance.d("download[pause]: status:${ niceDownloadInfo.status}")
        mInfo2Subscribe.let {
            //通过url获取HashMap中NiceDownloadSubscriber的实例并取消订阅
            it[niceDownloadInfo.url]?.dispose()
            // 从list中移除
            it.remove(niceDownloadInfo.url)
        }
    }

    /**
     * 开启下载
     * @param downloadServiceNice
     * @param niceDownloadInfo
     * @param subscriberNice
     */
    private fun startDownload(downloadServiceNice: NiceDownloadService, niceDownloadInfo: NiceDownloadInfo, subscriberNice: NiceDownloadSubscriber) {
        LogUtils.instance.d("download[start]:status:${niceDownloadInfo.status}-url:${niceDownloadInfo.url}")
        niceDownloadInfo.run{
            LogUtils.instance.d("download[start]: info:${toString()}")
            downloadServiceNice.downloadFile("bytes=$read-", url)
                    .subscribeOn(Schedulers.io())
                    ?.doOnNext { t: ResponseBody ->
                        writeRandomAccessFile(t, File(path), niceDownloadInfo)
                    }
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(subscriberNice)
        }

    }

    /**
     * 断点续传文件写入
     * @param responseBody
     * @param file
     * @param infoNice
     */
    @Throws(IOException::class)
    private fun writeRandomAccessFile(responseBody: ResponseBody, file: File, infoNice: NiceDownloadInfo) {
        LogUtils.instance.d("download[access_file]: response:${responseBody}-file_path:${file.absolutePath}\ndownloadInfo:${infoNice}")
        if (!file.parentFile.exists())
            file.parentFile.mkdirs()
        val allLength: Long = if (infoNice.count == 0L) {
            responseBody.contentLength()
        } else {
            infoNice.count
        }
        val channelOut: FileChannel
        val randomAccessFile = RandomAccessFile(file, "rwd")
        channelOut = randomAccessFile.channel
        val mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, infoNice.read, allLength - infoNice.read)
        val buffer = ByteArray(1024 * 8)
        var len = 0
        var record = 0
        while ({ len = responseBody.byteStream().read(buffer);len }() != -1) {
            mappedBuffer.put(buffer, 0, len)
            record += len
        }
        responseBody.byteStream().close()
        channelOut?.close()
        randomAccessFile.close()
    }

    /**
     * 下载状态
     */
    class DownloadStatus {
        companion object {
            /**
             *准备下载
             */
            val DOWNLOAD_START = "start"
            /**
             *下载失败
             */
            val DOWNLOAD_FAILED = "filed"
            /**
             *下载中
             */
            val DOWNLOAD_ING = "ing"
            /**
             *下载成功
             */
            val DOWNLOAD_SUCCESS = "success"
            /**
             *下载暂停
             */
            val DOWNLOAD_PAUSE = "pause"
            /**
             *下载取消
             */
            val DOWNLOAD_CANCEL = "cancel"
        }

    }
}


