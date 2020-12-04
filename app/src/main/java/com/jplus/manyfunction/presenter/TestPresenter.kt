package com.jplus.manyfunction.presenter

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.jplus.jvideoview.data.Video
import com.jplus.manyfunction.R
import com.jplus.manyfunction.common.Constant
import com.jplus.manyfunction.contract.TestContract
import com.jplus.manyfunction.download.DownloadCallback
import com.jplus.manyfunction.download.JDownloadManager
import com.jplus.manyfunction.net.HostList
import com.jplus.manyfunction.net.dto.*
import com.nice.baselibrary.base.common.BaseLibrary
import com.nice.baselibrary.base.download.DownloadState
import com.nice.baselibrary.base.entity.vo.AppInfo
import com.nice.baselibrary.base.entity.vo.DownloadInfo
import com.nice.baselibrary.base.net.doGet
import com.nice.baselibrary.base.net.doPost
import com.nice.baselibrary.base.net.uploadFile
import com.nice.baselibrary.base.ui.BaseActivity
import com.nice.baselibrary.base.utils.*
import com.nice.baselibrary.widget.dialog.BaseAlertDialog
import io.reactivex.rxjava3.observers.DisposableObserver
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.File
import java.util.*
import java.util.regex.Pattern


/**
 * @author JPlus
 * @date 2019/2/13.
 */
class TestPresenter(private val mView: TestContract.View, private val activity: BaseActivity) : TestContract.Presenter {


    private var mIsCanOpen = true
    private var mPhotoUtils: PhotoUtils? = null
    private var mHandler:Handler?=null
    init {
        mView.setPresenter(this)
        mPhotoUtils = PhotoUtils(true, "${activity.packageName}.provider")
        mHandler = Handler{
            @Suppress("UNCHECKED_CAST")
            when(it.what){
                1->   mView.showAppInfo(it.obj as MutableList<AppInfo>)
            }
            true
        }
    }

    override fun checkToCameraOrPhoto(view: View, jDialog: BaseAlertDialog) {
        activity.let {
            BaseLibrary.instance.requestPermissions(it, mutableSetOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), object : JPermissionsUtils.PermissionListener {
                override fun ignoredCallback(permissions: MutableSet<String>) {

                }

                override fun grantedCallback(permissions: MutableSet<String>) {

                }

                override fun deniedCallback(permissions: MutableSet<String>) {

                    if (Manifest.permission.WRITE_EXTERNAL_STORAGE in permissions) {
                        mIsCanOpen = mIsCanOpen && false
                    }
                    if (Manifest.permission.CAMERA in permissions) {
                        mIsCanOpen = mIsCanOpen && false
                    }

                }

            })
        }

        when (view.id) {
            R.id.ntv_photo_dialog_camera -> {
                if (mIsCanOpen) {
                    mView.getFragActivity()?.let {
                        mPhotoUtils?.openCamera(it, "pic_" + System.currentTimeMillis() + ".jpg", true,
                                object : PhotoUtils.ChoosePictureCallback {
                                    override fun onSuccess(path: String) {
                                        uploadPic("http://192.168.11.184:8000/upload_file/", path)
                                    }

                                    override fun onFail() {

                                    }

                                }, 1006, 1007)
                    }
                } else {
                    mView.getFragActivity()?.let {
                        JPermissionsUtils.showPermissionDialog(it, "权限提示", mutableSetOf(Manifest.permission.CAMERA))
                    }
                }
            }
            R.id.ntv_photo_dialog_photo -> {
                if (mIsCanOpen) {
                    mView.getFragActivity()?.let {
                        mPhotoUtils?.openPhotoAlbum(it, true,
                                object : PhotoUtils.ChoosePictureCallback {
                                    override fun onSuccess(path: String) {
                                        uploadPic("http://192.168.11.175:8000/upload_file/", path)
                                    }

                                    override fun onFail() {

                                    }

                                }, 1008, 1009)
                    }

                }
            }
            R.id.ntv_photo_dialog_cancel -> jDialog.dismiss()
        }

    }

    override fun init(activity: Activity) {
        val initRequest = InitializeRequest(activity.packageName,
                activity.getAppVersionName(),
                getOsLevel(),
                activity.getDeviceIMEI(),
                "Android",
                "",
                "",
                "${activity.getScreenWidth()}*${activity.getScreenHeight()}",
                getDeviceProduct(),
                "",
                "",
                "")
        doPost(HostList.BASE_HOST[0].url + Constant.init, initRequest, object : DisposableObserver<ResponseBody>() {
            override fun onComplete() {

            }

            override fun onNext(response: ResponseBody) {
                val str =response.string()
                LogUtils.d("请求成功：$str")
                val res = Gson().fromJson(str, BaseResponse::class.java)
                if (res.result_code == BaseResponse.SUCCESS) {
                    val re = Gson().fromJson(res.result_data, InitializeResponse::class.java)
                    if (re.is_debug) {
                        //是否debug模式
                        requestShow("token", re.show_num)
                    }
                }
            }

            override fun onError(e: Throwable) {
                LogUtils.d("连接服务器失败：message:" + e.printStackTrace())

            }
        },"https://www.google.com")

    }

    override fun showWebView(url: String) {
        mView.showWebView(url)
    }

    override fun getWebSource(url: String) {
        doGet(url, object : DisposableObserver<ResponseBody>(){
            override fun onComplete() {

            }

            override fun onNext(response: ResponseBody) {
                val str = response.string()
                LogUtils.d("请求成功：$str")
            }

            override fun onError(e: Throwable) {

            }

        }, "https://www.google.com")
    }

    private fun requestShow(token: String, num: Long) {
        val request = InitShowRequest(token, num)
        doPost(HostList.BASE_HOST[0].url + Constant.init_show, request, object : DisposableObserver<ResponseBody>() {
            override fun onComplete() {

            }

            override fun onNext(response: ResponseBody) {
                val str = response.string()
                LogUtils.d("请求成功：$str")
                val res = Gson().fromJson<BaseResponse>(str, BaseResponse::class.java)
                if (res.result_code == BaseResponse.SUCCESS) {
                    val re = Gson().fromJson<InitShowResponse>(res.result_data, InitShowResponse::class.java)
                    if (re.is_h5) {
                        mView.showInitH5View(re)
                    } else {
                        mView.showInitView(re)
                    }
                }
            }

            override fun onError(e: Throwable) {
                LogUtils.d("连接服务器失败：message:" + e.printStackTrace())
            }
        },"https://www.google.com")
    }

    override fun startPhotoTest() {
        mView.showUploadPic()
    }


    private fun uploadPic(url: String, path: String) {
        LogUtils.d("ChoosePicture onSuccess，path:$path")

        val request = File(path).asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("pic", File(path).name, request)
        uploadFile(url, body, object : DisposableObserver<ResponseBody>() {
            override fun onComplete() {

            }

            override fun onNext(response: ResponseBody) {
                val str = response.string()
                LogUtils.d("上传成功：$str")
                val fileUrl = JSONObject(str).getJSONObject("result_data").get("file_url").toString()
                mView.uploadResultView(fileUrl)
            }

            override fun onError(e: Throwable) {
                LogUtils.d("连接服务器失败：message:" + e.printStackTrace())
                mView.uploadResultView(null)
            }
        }, "https://www.google.com")
//        uploadFile(url, body, object : Callback<ResponseBody> {
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//
//            }
//
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//
//            }
//        })
    }


    override fun startPermissionTest() {
        mView.getFragActivity()?.let {
            JPermissionsUtils.requestPermissions(it, null, object : JPermissionsUtils.PermissionListener {
                override fun ignoredCallback(permissions: MutableSet<String>) {
                    if (permissions.size > 0) {
                        Log.d("pipa", "ignoredCallback$permissions")
                    }
                }

                override fun grantedCallback(permissions: MutableSet<String>) {
                    Log.d("pipa", "grantedCallback$permissions")
                }

                override fun deniedCallback(permissions: MutableSet<String>) {
                    Log.d("pipa", "deniedCallback$permissions")
                    if (Manifest.permission.SYSTEM_ALERT_WINDOW in permissions) {
                        JPermissionsUtils.showWindowDialog(it, "权限提示", "为保证应用的正常使用，请开启允许显示在应用上层权限。")
                    }
                    mView.showNotPermissionView(content = "为保证应用的正常使用，请开启权限。")
                }

            })
        }
    }

    override fun getAppInfos() {
        mView.showAppInfoDialog()
        GlobalScope.launch {
            val appInfos =  mView.getFragActivity()?.getAppsInfo(false)
            Log.d("pipa", "appInfos:$appInfos")
           mHandler?.let{
               val msg = it.obtainMessage()
               msg.what = 1
               //传递对象
               msg.obj = appInfos
               mHandler?.sendMessage(msg)
           }
        }
    }

    override fun downLoadPatch(url: String, dirPath: String, downloadCallback: DownloadCallback) {
        val name = parseUrlName(url)
        val download = DownloadInfo(0, name, url, dirPath + File.separator + name, Date(System.currentTimeMillis()).getDateTimeByMillis(false), "", 0, 0, DownloadState.DOWNLOAD_UNKNOWN, "")
        //不判断下载状态
        JDownloadManager.addNewDownload(download, downloadCallback, null, false)

    }

    override fun getPatchDownLoadUrl() {
        mView.showPatchDownLoad()
    }

    override fun login(phone: String, password: String) {
        if (!checkAccountPwd(phone, password)) {
            return
        }
        doPost("http://192.168.11.175:8000/login/", LoginRequest(phone, password), object : DisposableObserver<ResponseBody>() {
            override fun onComplete() {

            }

            override fun onNext(response: ResponseBody) {
                val str = response.string()
                LogUtils.d("登录成功：$str")
            }

            override fun onError(e: Throwable) {
                LogUtils.d("连接服务器失败：message:" + e.printStackTrace())
                mView.showLoginResult(false, e.message?:"")
            }
        },"https://www.google.com")
    }

    private fun checkAccountPwd(phone: String, password: String): Boolean {
        val phoneResult = Pattern.matches("^(13[0-9]|14[5|7]|15[0-9]|17[0-9]|18[0-9]|19[0-9])\\d{8}\$", phone)
        val passwordResult = Pattern.matches("[a-zA-Z]\\w{5,17}\$", password)
        return phoneResult && passwordResult
    }

    override fun playVideo(urlList: MutableList<Video>) {
        mView.showVideoView(urlList)
    }

    override fun download() {
        mView.downloadList()
    }

    override fun share(file: File) {
        mView.getFragActivity()?.let {
            ShareUtils.shareFile(it, file.absolutePath, "分享文件", activity.packageName + ".provider")
        }
    }

    override fun refreshLoadView() {
        mView.showRefreshLoadView()
    }


    override fun subscribe() {

    }

    override fun unSubscribe() {

    }

    override fun activityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mView.getFragActivity()?.let {
            mPhotoUtils?.onActivityResult(it, requestCode, resultCode, data)
        }
    }

    override fun permissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        mView.getFragActivity()?.let {
            BaseLibrary.instance.handleRequestPermissionsResult(it, requestCode, permissions, grantResults)
        }
    }


}