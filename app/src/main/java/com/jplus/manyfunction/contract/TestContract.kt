package com.jplus.manyfunction.contract

import android.app.Activity
import android.content.Intent
import com.jplus.jvideoview.data.Video
import com.jplus.manyfunction.net.dto.InitShowResponse
import com.nice.baselibrary.base.net.download.JDownloadCallback
import com.nice.baselibrary.base.mvp.BaseView
import com.nice.baselibrary.base.mvp.BasePresenter
import com.nice.baselibrary.base.vo.AppInfo
import com.nice.baselibrary.widget.dialog.JAlertDialog
import java.io.File

/**
 * @author JPlus
 * @date 2019/2/13.
 */
interface TestContract {
    interface View : BaseView<Presenter> {
        /**
         */
        fun getFragActivity():Activity?
        /**
         * 初始化原生弹窗
         */
        fun showInitView(response:InitShowResponse)
        /**
         * 初始化h5弹窗
         */
        fun showInitH5View(response:InitShowResponse)
        /**
         * 未通过权限界面
         */
        fun showNotPermissionView(content:String)

        /**
         * 图片上传界面
         */
        fun showUploadPic()

        /**
         * 图片上传结果显示
         */
        fun uploadResultView(url:String?)

        /**
         * appInfoDemo界面
         */
        fun showAppInfo(infos:MutableList<AppInfo>)
        /**
         * 登录界面
         */
        fun showLogin()
        /**
         * 登录结果界面
         */
        fun showLoginResult(result:Boolean, message:String)
        /**
         * 热修复下载界面
         */
        fun showPatchDownLoad()
        /**
         * 视频播放界面
         */
        fun showVideoView(urlList: MutableList<Video>)
        /**
         * 下载列表
         */
        fun downloadList()
        /**
         * 分享功能
         */
        fun showShareView()
        /**
         * 上拉加载下拉刷新功能
         */
        fun showRefreshLoadView()
        /**
         * WebView
         */
        fun showWebView(url: String)
    }

    interface Presenter: BasePresenter {
        /**
         * 初始化
         */
        fun init(activity:Activity)
        /**
         * 请求权限测试
         */
        fun startPermissionTest()
        /**
         * ActivityResult返回
         */
        fun activityResult(requestCode: Int, resultCode: Int, data: Intent?)
        /**
         * 请求权限返回
         */
        fun permissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)

        /**
         * 图片上传测试
         */
        fun startPhotoTest()

        /**
         * 图片上传功能
         */
        fun checkToCameraOrPhoto(view: android.view.View, jDialog: JAlertDialog)
        /**
         * 获取appInfos
         */
        fun getAppInfos()

        /**
         * 热修复下载url
         */
        fun getPatchDownLoadUrl()

        /**
         * 登录
         */
        fun login(phone:String, password:String)

        /**
         * 热修复dex下载
         */
        fun downLoadPatch(url: String, dirPath:String, jDownloadCallback: JDownloadCallback)

        /**
         * 视频流播放
         */
        fun playVideo(urlList: MutableList<Video>)

        /**
         * 分享功能
         */
        fun share(file: File)
        /**
         * 下载列表
         */
        fun download()
        /**
         * 上拉加载下拉刷新
         */
        fun refreshLoadView()
        /**
         * WebView
         */
        fun showWebView(url: String)

    }
}