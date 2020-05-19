package com.nice.baselibrary.base.download

/**
 * 下载进度监听
 * @author JPlus
 * @date 2019/2/22.
 */
interface DownloadProgressListener {
    /**
     * 下载中
     * @param read
     * @param count
     * @param done
     */
    fun update(read:Long, count:Long, done:Boolean)
}