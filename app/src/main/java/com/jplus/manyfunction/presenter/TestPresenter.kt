package com.jplus.manyfunction.presenter

import android.Manifest
import android.content.Intent
import android.util.Log
import android.view.View
import com.jplus.manyfunction.R
import com.jplus.manyfunction.contract.TestContract
import com.jplus.manyfunction.net.dto.LoginRequest
import com.jplus.manyfunction.ui.activity.RefreshActivity
import com.nice.baselibrary.base.common.BaseLibrary
import com.nice.baselibrary.base.net.download.*
import com.nice.baselibrary.base.net.upload.OkhttpManager
import com.nice.baselibrary.base.utils.*
import com.nice.baselibrary.widget.dialog.NiceAlertDialog
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
class TestPresenter(view: TestContract.View) : TestContract.Presenter {

    private var mView: TestContract.View? = view
    private var mIsCanOpen = true
    private var mPhotoUtils: PhotoUtils? = null
    private var mDataSourceJ: JDownloadDataSource? = null

    init {
        mView?.setPresenter(this)
        mPhotoUtils = PhotoUtils(true)
        mView?.getFragActivity()?.let {
            mDataSourceJ = JDownloadDataSource(it)
        }
    }

    override fun checkToCameraOrPhoto(view: View, niceDialog: NiceAlertDialog) {
        mView?.getFragActivity()?.let {
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
                    mView?.getFragActivity()?.let {
                        mPhotoUtils?.openCamera(it, "pic_" + System.currentTimeMillis() + ".jpg", true,
                                object : PhotoUtils.ChoosePictureCallback {
                                    override fun onSuccess(path: String) {
                                        uploadPic("http://192.168.11.175:8000/upload_file/", path)
                                    }

                                    override fun onFail() {

                                    }

                                }, 1006, 1007)
                    }
                } else {
                    mView?.getFragActivity()?.let {
                        JPermissionsUtils.showPermissionDialog(it, "权限提示", mutableSetOf(Manifest.permission.CAMERA))
                    }
                }
            }
            R.id.ntv_photo_dialog_photo -> {
                if (mIsCanOpen) {
                    mView?.getFragActivity()?.let {
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
            R.id.ntv_photo_dialog_cancel -> niceDialog.dismiss()
        }

    }

    override fun startPhotoTest() {
        mView?.showUploadPic()
    }


    private fun uploadPic(url: String, path: String) {
        LogUtils.d("ChoosePicture onSuccess，path:$path")

        val request = RequestBody.create(MediaType.parse("multipart/form-data"), File(path))
        val body = MultipartBody.Part.createFormData("pic", File(path).name, request)

        OkhttpManager.uploadFile(url, body, object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                LogUtils.d("请求失败：message:" + t.printStackTrace())
                mView?.uploadResultView(null)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val str = response.body()?.string()
                    LogUtils.d("上传成功：$str")
                    val fileUrl = JSONObject(str).getJSONObject("result_data").get("file_url").toString()
                    mView?.uploadResultView(fileUrl)

                } else {
                    LogUtils.d("上传失败：code:" + response.code())
                    mView?.uploadResultView(null)
                }
            }
        })
    }


    override fun startPermissionTest() {
        mView?.getFragActivity()?.let {
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
                }

            })
        }
    }

    override fun getAppInfos() {
        mView?.getFragActivity()?.let {
            val appInfos = it.getAppsInfo(false)
            mView?.showAppInfo(appInfos)
        }
    }

    override fun downLoadPatch(url: String, dirPath: String, jDownloadCallback: JDownloadCallback) {
        val downloadList = mDataSourceJ?.getData(mutableMapOf(Pair("url", url))) ?: mutableListOf()
        if (downloadList.size == 0) {
            val name = StringUtils.parseUrlName(url)
            val download = JDownloadInfo(0, name, url, dirPath + File.separator + name, Date(System.currentTimeMillis()).getDateTimeByMillis(false), "", 0, 0, JDownloadState.DOWNLOAD_READY)
            mDataSourceJ?.addData(download)
            //不判断下载状态
            download.status = JDownloadState.DOWNLOAD_ING
            mDataSourceJ?.modifyData(download)
            JDownloadManager().startNewDownload(download, jDownloadCallback, mDataSourceJ!!)
        }
    }

    override fun getPatchDownLoadUrl() {
        mView?.showPatchDownLoad()
    }

    override fun login(phone: String, password: String) {
        if (!checkAccountPwd(phone, password)) {
            return
        }
        OkhttpManager.doPost("http://192.168.11.175:8000/login/", LoginRequest(phone, password), object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                LogUtils.d("请求失败：message:" + t.printStackTrace())
                mView?.uploadResultView(null)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val str = response.body()?.string()
                    LogUtils.d("登录成功：$str")
                } else {
                    LogUtils.d("登录失败：code:" + response.code())
                }
            }
        })
    }

    private fun checkAccountPwd(phone: String, password: String): Boolean {
        val phoneResult = Pattern.matches("^(13[0-9]|14[5|7]|15[0-9]|17[0-9]|18[0-9]|19[0-9])\\d{8}\$", phone)
        val passwordResult = Pattern.matches("[a-zA-Z]\\w{5,17}\$", password)
        return phoneResult && passwordResult
    }

    override fun playVideo(url: String) {
        mView?.showVideoView()
    }

    override fun share(file: File) {
        mView?.getFragActivity()?.let {
            ShareUtils.shareFile(it, file, "分享文件")
        }
    }

    override fun refreshLoadView() {
        mView?.getFragActivity()?.let {
            it.startActivity(Intent(it, RefreshActivity::class.java))
        }

    }


    override fun subscribe() {

    }

    override fun unSubscribe() {

    }

    override fun activityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mView?.getFragActivity()?.let {
            mPhotoUtils?.onActivityResult(it, requestCode, resultCode, data)
        }
    }

    override fun permissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        mView?.getFragActivity()?.let {
            BaseLibrary.instance.handleRequestPermissionsResult(it, requestCode, permissions, grantResults)
        }
    }


}