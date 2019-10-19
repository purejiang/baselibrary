package com.nice.baselibrary.base.net.download

/**
 * 下载监听
 * @author JPlus
 * @date 2019/2/22.
 */
interface NiceDownloadListener {
    /**
     * 下载中
     * @param read
     * @param count
     * @param done
     */
    fun update(read:Long, count:Long, done:Boolean)
    /**
     * 下载成功
     */
    fun downloadSuccess()
    /**
     * 下载失败
     * @param e
     */
    fun downloadFailed(e:Throwable)
    /**
     * 下载取消
     */
    fun downloadCancel()
}