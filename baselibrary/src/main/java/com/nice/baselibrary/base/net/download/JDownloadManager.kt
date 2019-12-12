package com.nice.baselibrary.base.net.download


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
class JDownloadManager{

    private val mInfo2Service: HashMap<String, JDownloadService> by lazy {
        HashMap<String, JDownloadService>()
    }
    private val mInfo2Subscribe: HashMap<String, JDownloadSubscriber>by lazy {
        HashMap<String, JDownloadSubscriber>()
    }
    private val mServiceList:MutableList<JDownloadService> by lazy {
        mutableListOf<JDownloadService>()
    }


    /**
     * 新增下载
     * @param downloadInfo
     * @param jDownloadCallback
     * @param jDownloadDataSource
     */
    fun startNewDownload(downloadInfo: JDownloadInfo, jDownloadCallback: JDownloadCallback, jDownloadDataSource: JDownloadDataSource?) {
        LogUtils.d("download[start——new]:info:${downloadInfo}")
        //新建绑定，添加对应关系
        val subscriber = JDownloadSubscriber(downloadInfo, jDownloadCallback, jDownloadDataSource)
        mInfo2Subscribe[downloadInfo.url] = subscriber
        //新建okhttp客户端，拦截器监听下载进度
        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(JDownloadInterceptor(subscriber))
                .build()
        //新建下载服务，添加对应关系
        val downloadService = JDownloadRetrofitHelper(okHttpClient).getService()
        mInfo2Service[downloadInfo.url] = downloadService
        //开始下载
        startDownload(downloadService, downloadInfo, subscriber)
    }

    /**
     * 继续下载
     * @param jDownloadInfo
     * @param jDownloadCallback
     */
    fun reStartDownload(jDownloadInfo: JDownloadInfo, jDownloadCallback: JDownloadCallback, jDownloadDataSource: JDownloadDataSource?) {
        LogUtils.d("download[restart]: info:${jDownloadInfo}")
        //列表包含该url则复用map中指向的service对象，不包含该url则重新构建service进行下载
        if(mInfo2Service.containsKey(jDownloadInfo.url)){
            val subscriber =  JDownloadSubscriber(jDownloadInfo, jDownloadCallback, jDownloadDataSource)
            mInfo2Subscribe[jDownloadInfo.url] = subscriber
            startDownload(mInfo2Service[jDownloadInfo.url]!!, jDownloadInfo, subscriber)
        }else{
            startNewDownload(jDownloadInfo, jDownloadCallback, jDownloadDataSource)
        }
    }

    /**
     * 暂停下载
     * @param jDownloadInfo
     */
    fun pauseDownload(jDownloadInfo: JDownloadInfo) {
        LogUtils.d("download[pause]: status:${ jDownloadInfo.status}")
        mInfo2Subscribe.let {
            //通过url获取HashMap中NiceDownloadSubscriber的实例并取消订阅
            it[jDownloadInfo.url]?.dispose()
            // 从list中移除
            it.remove(jDownloadInfo.url)
        }
    }

    /**
     * 开启下载
     * @param downloadService
     * @param jDownloadInfo
     * @param subscriberNice
     */
    private fun startDownload(downloadService: JDownloadService, jDownloadInfo: JDownloadInfo, subscriberNice: JDownloadSubscriber) {
        LogUtils.d("download[start]:status:${jDownloadInfo.status}-url:${jDownloadInfo.url}")
        jDownloadInfo.run{
            LogUtils.d("download[start]: info:${toString()}")
            downloadService.downloadFile("bytes=$read-", url)
                    .subscribeOn(Schedulers.io())
                    ?.doOnNext { t: ResponseBody ->
                        writeRandomAccessFile(t, File(path), jDownloadInfo)
                    }
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(subscriberNice)
        }

    }

    /**
     * 断点续传文件写入
     * @param responseBody
     * @param file
     * @param infoJ
     */
    @Throws(IOException::class)
    private fun writeRandomAccessFile(responseBody: ResponseBody, file: File, infoJ: JDownloadInfo) {
        LogUtils.d("download[access_file]: response:${responseBody}-file_path:${file.absolutePath}\ndownloadInfo:${infoJ}")
        if (!file.parentFile.exists())
            file.parentFile.mkdirs()
        val allLength: Long = if (infoJ.count == 0L) {
            responseBody.contentLength()
        } else {
            infoJ.count
        }
        val channelOut: FileChannel
        val randomAccessFile = RandomAccessFile(file, "rwd")
        channelOut = randomAccessFile.channel
        val mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, infoJ.read, allLength - infoJ.read)
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
}


