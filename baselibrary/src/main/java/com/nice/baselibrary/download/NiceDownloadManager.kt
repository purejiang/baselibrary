package com.nice.baselibrary.download


import android.content.Context
import android.os.Environment
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
        private var mNiceDownloadManager: NiceDownloadManager? = null
        private val DOWNLOAD_FILE_DIR = Environment.getExternalStoragePublicDirectory("").absolutePath + File.separator + "downloadFile"
        /**
         * 单例获取
         * @return
         */
        fun getInstance(): NiceDownloadManager? {
            mNiceDownloadManager ?: NiceDownloadManager()
            return mNiceDownloadManager
        }

    }

    private var mInfo2ServiceNice: HashMap<String, NiceDownloadService>? = null
    private var mInfo2Subscribe: HashMap<String, NiceDownloadSubscriber>? = null
    private var mContext: Context? = null


    /**
     * 初始化
     * @param context
     */
    fun init(context: Context) {
        mContext = context
        mInfo2Subscribe = HashMap()
        mInfo2ServiceNice = HashMap()
    }

    /**
     * 新增下载
     * @param niceDownloadInfo
     * @param niceDownloadListener
     */
    fun startNewDownload(niceDownloadInfo: NiceDownloadInfo, niceDownloadListener: NiceDownloadListener, niceDownloadDataSource: NiceDownloadDataSource?) {
        val subscriber = NiceDownloadSubscriber(niceDownloadInfo, niceDownloadListener, niceDownloadDataSource)
        mInfo2Subscribe?.put(niceDownloadInfo.url, subscriber)
        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(NiceDownloadInterceptor(subscriber))
                .build()
        val downloadService = NiceDownloadRetrofitHelper(okHttpClient).getService()!!
        mInfo2ServiceNice?.put(niceDownloadInfo.url, downloadService)
        startDownload(downloadService, niceDownloadInfo, subscriber)
    }

    /**
     * 继续下载
     * @param niceDownloadInfo
     * @param niceDownloadListener
     */
    fun reStartDownload(niceDownloadInfo: NiceDownloadInfo, niceDownloadListener: NiceDownloadListener, niceDownloadDataSource: NiceDownloadDataSource?) {
        //列表包含该url则复用map中指向的service对象，不包含该url则重新构建service进行下载
        if(mInfo2ServiceNice?.containsKey(niceDownloadInfo.url)!!){
            val subscriber =  NiceDownloadSubscriber(niceDownloadInfo, niceDownloadListener, niceDownloadDataSource)
            mInfo2Subscribe?.put(niceDownloadInfo.url, subscriber)
            startDownload(mInfo2ServiceNice?.get(niceDownloadInfo.url)!!, niceDownloadInfo, subscriber)
        }else{
            startNewDownload(niceDownloadInfo, niceDownloadListener, niceDownloadDataSource)
        }
    }

    /**
     * 暂停下载
     * @param niceDownloadInfo
     */
    fun pauseDownload(niceDownloadInfo: NiceDownloadInfo) {
        mInfo2Subscribe?.let {
            it[niceDownloadInfo.url]?.dispose() //通过url获取HashMap中NiceDownloadSubscriber的实例并取消订阅
            it.remove(niceDownloadInfo.url) //从list中移除
        }
        LogUtils.getInstance().d("download+pause:" + niceDownloadInfo.status)
    }

    /**
     * 开启下载
     * @param downloadServiceNice
     * @param niceDownloadInfo
     * @param subscriberNice
     */
    private fun startDownload(downloadServiceNice: NiceDownloadService, niceDownloadInfo: NiceDownloadInfo, subscriberNice: NiceDownloadSubscriber) {
        niceDownloadInfo.run{
            LogUtils.getInstance().d("download+:" + toString())
            downloadServiceNice.downloadFile("bytes=" + read.toString() + "-", url)
                    .subscribeOn(Schedulers.io())
                    ?.doOnNext { t: ResponseBody ->
                        writeRandomAccessFile(t, File(DOWNLOAD_FILE_DIR, name), niceDownloadInfo)
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


