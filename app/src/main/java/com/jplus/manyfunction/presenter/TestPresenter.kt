package com.jplus.manyfunction.presenter

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.jplus.jvideoview.data.Video
import com.jplus.manyfunction.R
import com.jplus.manyfunction.common.Constant
import com.jplus.manyfunction.contract.TestContract
import com.jplus.manyfunction.ui.activity.RefreshActivity
import com.nice.baselibrary.base.common.BaseLibrary
import com.nice.baselibrary.base.net.download.listener.JDownloadCallback
import com.nice.baselibrary.base.net.download.vo.JDownloadInfo
import com.jplus.manyfunction.download.JDownloadManager
import com.jplus.manyfunction.net.HostList
import com.jplus.manyfunction.net.dto.*

import com.nice.baselibrary.base.net.download.JDownloadState
import com.nice.baselibrary.base.net.OkhttpManager
import com.nice.baselibrary.base.utils.*
import com.nice.baselibrary.widget.dialog.JAlertDialog
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*
import java.util.regex.Pattern


/**
 * @author JPlus
 * @date 2019/2/13.
 */
class TestPresenter(private val mView: TestContract.View, private val activity: Activity) : TestContract.Presenter {


    private var mIsCanOpen = true
    private var mPhotoUtils: PhotoUtils? = null

    init {
        mView.setPresenter(this)
        mPhotoUtils = PhotoUtils(true)
    }

    override fun checkToCameraOrPhoto(view: View, jDialog: JAlertDialog) {
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
                        mPhotoUtils?.openPhoto(it, true,
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

    override fun init(activity:Activity) {
        val initRequest = InitializeRequest(activity.packageName,
                activity.getAppVersionName(),
                getOsLevel(),
                activity.getDeviceImei(),
                "Android",
                "",
                "",
                "${activity.getScreenWidth()}*${activity.getScreenHeight()}",
                getDeviceProduct(),
                "",
                "",
                "")
        OkhttpManager.doPost(HostList.BASE_HOST[0].url + Constant.init, initRequest, HostList.BASE_HOST[0].timeOut, object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                LogUtils.d("连接服务器失败：message:" + t.printStackTrace())

            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val str = response.body()?.string()
                    LogUtils.d("请求成功：$str")
                    val res = Gson().fromJson<BaseResponse>(str, BaseResponse::class.java)
                    if(res.result_code==BaseResponse.SUCCESS){
                        val re = Gson().fromJson<InitializeResponse>(res.result_data, InitializeResponse::class.java)
                        if(re.is_debug){
                           //是否debug模式
                            requestShow("token", re.show_num)
                        }
                    }
                } else {
                    LogUtils.d("连接服务器失败：code:" + response.code())
                }
            }
        })

    }

    override fun showWebView(url: String) {
        mView.showWebView(url)
    }

    private fun requestShow(token:String, num:Long){
        val request  = InitShowRequest(token, num)
        OkhttpManager.doPost(HostList.BASE_HOST[0].url + Constant.init_show, request, HostList.BASE_HOST[0].timeOut, object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                LogUtils.d("连接服务器失败：message:" + t.printStackTrace())

            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val str = response.body()?.string()
                    LogUtils.d("请求成功：$str")
                    val res = Gson().fromJson<BaseResponse>(str, BaseResponse::class.java)
                    if(res.result_code==BaseResponse.SUCCESS){
                        val re = Gson().fromJson<InitShowResponse>(res.result_data, InitShowResponse::class.java)
                        if(re.is_h5) {
                            mView.showInitH5View(re)
                        }else{
                            mView.showInitView(re)
                        }
                    }
                } else {
                    LogUtils.d("连接服务器失败：code:" + response.code())
                }
            }
        })
    }

    override fun startPhotoTest() {
        mView.showUploadPic()
    }


    private fun uploadPic(url: String, path: String) {
        LogUtils.d("ChoosePicture onSuccess，path:$path")

        val request = RequestBody.create(MediaType.parse("multipart/form-data"), File(path))
        val body = MultipartBody.Part.createFormData("pic", File(path).name, request)

        OkhttpManager.uploadFile(url, body, 15L, object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                LogUtils.d("连接服务器失败：message:" + t.printStackTrace())
                mView.uploadResultView(null)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val str = response.body()?.string()
                    LogUtils.d("上传成功：$str")
                    val fileUrl = JSONObject(str).getJSONObject("result_data").get("file_url").toString()
                    mView.uploadResultView(fileUrl)

                } else {
                    LogUtils.d("连接服务器失败：code:" + response.code())
                    mView.uploadResultView(null)
                }
            }
        })
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
        mView.getFragActivity()?.let {
            val appInfos = it.getAppsInfo(false)
            mView.showAppInfo(appInfos)
        }
    }

    override fun downLoadPatch(url: String, dirPath: String, jDownloadCallback: JDownloadCallback) {
        val name = StringUtils.parseUrlName(url)
        val download = JDownloadInfo(0, name, url, dirPath + File.separator + name, Date(System.currentTimeMillis()).getDateTimeByMillis(false), "", 0, 0, JDownloadState.DOWNLOAD_UNKNOWN)
        //不判断下载状态
        JDownloadManager.addNewDownload(download, jDownloadCallback, null, false)

    }

    override fun getPatchDownLoadUrl() {
        mView.showPatchDownLoad()
    }

    override fun login(phone: String, password: String) {
        if (!checkAccountPwd(phone, password)) {
            return
        }
        OkhttpManager.doPost("http://192.168.11.175:8000/login/", LoginRequest(phone, password),15, object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                LogUtils.d("连接服务器失败：message:" + t.printStackTrace())
                mView.uploadResultView(null)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val str = response.body()?.string()
                    LogUtils.d("登录成功：$str")
                } else {
                    LogUtils.d("连接服务器失败：code:" + response.code())
                }
            }
        })
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
            ShareUtils.shareFile(it, file, "分享文件")
        }
    }

    override fun refreshLoadView() {
        mView.getFragActivity()?.let {
            it.startActivity(Intent(it, RefreshActivity::class.java))
        }

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