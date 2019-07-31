package com.nice.baselibrary.base.utils

import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.database.CursorIndexOutOfBoundsException
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import com.nice.baselibrary.base.ui.view.NiceShowView
import java.io.File


/**
 * @author JPlus
 * @date 2019/4/24.
 */
class PhotoUtils constructor(private val mActivity:Activity, private val mIsCache:Boolean) {
    private var mCameraCode = 0
    private var mPhotoCode = 1
    private var mCameraCropCode = 2
    private var mPhotoCropCode = 3
    private var mImagePath: String? = null
    private var mCameraCallBack:ChoosePictureCallback?=null
    private var mPhotoCallBack:ChoosePictureCallback?=null
    private var mCameraIsCrop = false
    private var mPhotoIsCrop = false

    /**
     * 打开相机
     * @param fileName
     * @param isCrop
     * @param cameraCallBack
     * @param requestCode
     * @param cropCode
     */
    fun openCamera(fileName: String, isCrop:Boolean, cameraCallBack:ChoosePictureCallback, requestCode: Int, cropCode:Int) {
        mCameraCallBack = cameraCallBack
        mCameraCode = requestCode
        mCameraCropCode = cropCode
        mCameraIsCrop = isCrop
        File(Environment.getExternalStorageDirectory(), "pic").let {
            LogUtils.getInstance().d(it.toString())
            if (!FileUtils.createOrExistsDir(it)) {
                NiceShowView.getInstance().NormalToast("无法生成文件夹,请检查权限").show()
                return
            }
            mImagePath = File(it, fileName).absolutePath
            LogUtils.getInstance().d(mImagePath.toString())
        }

        Intent().let {
            if (AppUtils.getInstance().getApiLevel() >= Build.VERSION_CODES.N) {
                it.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION   // 临时赋予该Intent对uri的可读权限
            }
            it.action = MediaStore.ACTION_IMAGE_CAPTURE
            it.putExtra(MediaStore.EXTRA_OUTPUT, getUriByPath(mImagePath))  // 保存拍照的图片到uri而不是系统相册
            LogUtils.getInstance().e("openCamera")
            mActivity.startActivityForResult(it, requestCode)
        }
    }

    /**
     * 打开相册
     * @param isCrop 是否剪裁
     * @param photoCallBack
     * @param requestCode
     * @param cropCode
     */
    fun openPhoto(isCrop: Boolean, photoCallBack:ChoosePictureCallback, requestCode: Int, cropCode:Int) {
        mPhotoCallBack = photoCallBack
        mPhotoCode = requestCode
        mPhotoCropCode = cropCode
        mPhotoIsCrop = isCrop
        Intent().let {
            it.type = "image/*"
            it.action = Intent.ACTION_PICK
            mActivity.startActivityForResult(it, requestCode)
        }
    }

    /**
     * 剪裁图片
     * @param requestCode
     * @param uri
     *
     */
    private fun cropImage(requestCode: Int, uri: Uri?) {
        /*
        crop	                        String      发送裁剪信号                  intent.putExtra("crop", "true");
        aspectX	                        int         X方向上的比例                 intent.putExtra("aspectX", 1);
        aspectY	                        int         Y方向上的比例                 intent.putExtra("aspectY", 1);
        outputX	                        int         裁剪区的宽                    intent.putExtra("outputX", 240);
        outputY	                        int         裁剪区的高                    intent.putExtra("outputX", 320);
        scale	                        boolean     是否保留比例                  intent.putExtra("scale", true);
        return-data	                    boolean	    是否将数据保留在Bitmap中返回  intent.putExtra("return-data", true);
        data	                        Parcelable	相应的Bitmap数据              intent.putExtra("data", imgaeUri);
        circleCrop	                    boolean	    圆形裁剪区域                  intent.putExtra("circleCrop", true);
        MediaStore.EXTRA_OUTPUT ("output")	URI
        将URI指向相应的file:///...                                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        outputFormat	                String	    输出格式                      intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        noFaceDetection	                boolean	    是否取消人脸识别功能          intent.putExtra("noFaceDetection", true);
         */
        LogUtils.getInstance().d("--crop--")
        Intent("com.android.camera.action.CROP").let {
            if (AppUtils.getInstance().getApiLevel() >= Build.VERSION_CODES.N) {
                it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // 临时权限
            }
            it.setDataAndType(uri, "image/*") // 图片类型
            it.putExtra("crop", "true") //是否裁剪
            it.putExtra("scale", true) // 是否缩放
            it.putExtra("aspectX", 1) // 裁剪框宽的比例
            it.putExtra("aspectY", 1)  // 裁剪框高的比例
            it.putExtra("outputX", 720) // 输出图片的宽度
            it.putExtra("outputY", 720) // 输出图片的高度
            it.putExtra("return-data", false)  //是否直接以Bitmap的形式缓存到内存中（会导致OOM问题）
//            it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(File(Environment.getExternalStorageDirectory(), "pic_"+System.currentTimeMillis()+".jpg")))
            it.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
            it.putExtra("noFaceDetection", true)
            mActivity.startActivityForResult(it, requestCode)
        }
    }

    /**
     * 通过uri获取文件的原本路径
     * @param uri
     * @param selection
     * @return String
     */
    @Throws(CursorIndexOutOfBoundsException::class)
    private fun getPathByUri(uri: Uri?, selection: String?): String? {
        LogUtils.getInstance().d("getPathByUri:$uri")
        var path: String? = null
        val cursor = mActivity.contentResolver.query(uri, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path
    }

    /**
     * 将path转换为uri
     * @param filePath
     * @return Uri
     */
    @Throws(IllegalArgumentException::class)
    private fun getUriByPath(filePath: String?): Uri? {
        LogUtils.getInstance().d("getUriByPath:$filePath")
        return  if (AppUtils.getInstance().getApiLevel() >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(mActivity, AppUtils.getInstance().getPackageName() + ".provider", File(filePath))
        } else {
            Uri.fromFile(File(filePath))
        }
    }

    /**
     * 解析系统返回的uri
     * @param uri
     * @return String
     */
    private fun parseUri(uri: Uri?): String? {
        var imgPath: String? = null
        uri?.let {
            if (DocumentsContract.isDocumentUri(mActivity, it)) {
                val docId = DocumentsContract.getDocumentId(it)
                if ("com.android.providers.media.documents" == it.authority) {
                    val id = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                    val selection = MediaStore.Images.Media._ID + "=" + id
                    imgPath = getPathByUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
                } else if ("com.android.providers.downloads.documents" == it.authority) {
                    val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            docId.toLong())
                    imgPath = getPathByUri(contentUri, null)
                }
            } else if ("content".equals(it.scheme, true)) {
                imgPath = if("com.google.android.apps.photos.content" ==it.authority){
                    it.lastPathSegment
                }else {
                    getPathByUri( it, null)
                }
            } else if ("file".equals(it.scheme, true)) {
                imgPath = it.path
            }
            LogUtils.getInstance().d("imgPath:$imgPath")
        }

        return  imgPath
    }

    private fun onRunCallback(callBack: ChoosePictureCallback?, path: String?){
            if (path != null) {
                LogUtils.getInstance().d( " ChoosePictureCallBack.onSuccess")
                callBack?.onSuccess(path)

            } else {
                LogUtils.getInstance().d(" ChoosePictureCallBack.onFail")
                callBack?.onFail()

            }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK)
        when (requestCode) {
            mCameraCode -> {
                //是否执行裁剪
                if(mCameraIsCrop){
                    cropImage(mCameraCropCode, getUriByPath(mImagePath))
                }else {
                    //回调通知
                    onRunCallback(mCameraCallBack, mImagePath)
                }
            }
            mPhotoCode -> {
                if(mPhotoIsCrop){
                    cropImage(mPhotoCropCode, getUriByPath(parseUri(data?.data)))
                }else {
                    onRunCallback(mPhotoCallBack, parseUri(data?.data))
                }
            }
            mCameraCropCode -> {
                onRunCallback(mCameraCallBack, mImagePath)
            }
            mPhotoCropCode->{
                onRunCallback(mPhotoCallBack, parseUri(data?.data))
            }
        }
    }

    interface ChoosePictureCallback {
        /**
         * 选择图片成功
         * @param path
         */
        fun onSuccess(path: String)

        /**
         * 选择图片失败
         */
        fun onFail()
    }
}