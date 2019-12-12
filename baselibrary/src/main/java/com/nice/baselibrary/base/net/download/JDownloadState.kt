package com.nice.baselibrary.base.net.download

/**
 * 下载状态
 * @author JPlus
 * @date 2019/12/11.
 */
class JDownloadState {
        companion object {
            /**
             *准备下载
             */
            val DOWNLOAD_READY = "ready"
            /**
             *下载失败
             */
            val DOWNLOAD_FAILED = "failed"
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