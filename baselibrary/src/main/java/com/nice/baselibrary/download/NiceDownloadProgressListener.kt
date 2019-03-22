package com.nice.baselibrary.download

/**
 * 下载进度监听
 * @author JPlus
 * @date 2019/2/22.
 */
interface NiceDownloadProgressListener {
    /**
     * 下载中
     * @param read
     * @param count
     * @param done
     */
    fun update(read:Long, count:Long, done:Boolean)
}