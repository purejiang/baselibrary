package com.nice.baselibrary.base.download

/**
 * 下载状态
 * @author JPlus
 * @date 2019/12/11.
 */
class DownloadState {
        companion object {
            /**
             *未知
             */
            const val DOWNLOAD_UNKNOWN = "unknown"
            /**
             *准备下载
             */
            const val DOWNLOAD_READY = "ready"
            /**
             *下载失败
             */
            const val DOWNLOAD_FAILED = "failed"
            /**
             *下载中
             */
            const val DOWNLOAD_ING = "ing"
            /**
             *下载成功
             */
            const val DOWNLOAD_SUCCESS = "success"
            /**
             *下载暂停
             */
            const val DOWNLOAD_PAUSE = "pause"
            /**
             *下载取消
             */
            const val DOWNLOAD_CANCEL = "cancel"
        }
}