package com.jplus.manyfunction.ui.fragment

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.textfield.TextInputEditText
import com.jplus.jvideoview.data.Video
import com.jplus.jvideoview.data.source.VideoRepository
import com.jplus.jvideoview.data.source.local.LocalVideoDataSource
import com.jplus.jvideoview.data.source.remote.RemoteVideoDataSource
import com.jplus.jvideoview.jvideo.JVideoState
import com.jplus.jvideoview.jvideo.JVideoView
import com.jplus.jvideoview.jvideo.JVideoViewPresenter
import com.jplus.manyfunction.R
import com.jplus.manyfunction.contract.TestContract
import com.jplus.manyfunction.net.dto.InitShowResponse
import com.jplus.manyfunction.ui.activity.DownloadListActivity
import com.jplus.manyfunction.ui.activity.RefreshActivity
import com.jplus.manyfunction.ui.activity.WebActivity
import com.jplus.manyfunction.utils.ParseVideoInUrl
import com.nice.baselibrary.base.adapter.BaseAdapter
import com.nice.baselibrary.base.common.Constant
import com.nice.baselibrary.base.listener.NotDoubleOnClickListener
import com.nice.baselibrary.base.net.download.JDownloadCallback
import com.nice.baselibrary.base.ui.BaseFragment
import com.nice.baselibrary.base.utils.*
import com.nice.baselibrary.base.vo.AppInfo
import com.nice.baselibrary.widget.JTextView
import com.nice.baselibrary.widget.dialog.JAlertDialog
import com.nice.baselibrary.widget.dialog.JDialog
import kotlinx.android.synthetic.main.fragment_test.*
import okhttp3.ResponseBody
import java.io.File

/**
 * @author JPlus
 * @date 2019/5/8.
 */
class TestFragment : BaseFragment(), TestContract.View {

    private var presenter: JVideoViewPresenter?=null
    private var mPresenter: TestContract.Presenter? = null

    override fun getInitView(view: View?, bundle: Bundle?) {

    }

    override fun getFragActivity(): Activity? {
        return this.activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter?.startPermissionTest()
        this.activity?.let {
            mPresenter?.init(it)
        }
    }

    override fun bindListener() {
        LogUtils.d("Fragment bindListener")
        btn_test_patch?.setOnClickListener(object : NotDoubleOnClickListener() {
            override fun notDoubleOnClick(view: View) {
                mPresenter?.getPatchDownLoadUrl()
            }
        })

        btn_upload_pic?.setOnClickListener(object : NotDoubleOnClickListener() {
            override fun notDoubleOnClick(view: View) {
                LogUtils.d("btn_upload_pic.setOnClickListener")
                mPresenter?.startPhotoTest()
            }
        })

        btn_app_infos?.setOnClickListener(object : NotDoubleOnClickListener() {
            override fun notDoubleOnClick(view: View) {
                LogUtils.d("btn_app_infos.setOnClickListener")
                mPresenter?.getAppInfos()
            }
        })
        btn_app_login?.setOnClickListener(object : NotDoubleOnClickListener() {
            override fun notDoubleOnClick(view: View) {
                LogUtils.d("btn_app_login.setOnClickListener")
                showLogin()
            }

        })
        btn_app_video?.setOnClickListener(object : NotDoubleOnClickListener() {
            override fun notDoubleOnClick(view: View) {
                val list = mutableListOf<Video>(Video(1, "斗破苍穹第一集：废柴萧炎", "", "https://cn5.download05.com/hls/20190804/ebfd96741e2e6e854144b2e012c30755/1564883639/index.m3u8", 0))
                mPresenter?.playVideo(list)
            }
        })
        btn_app_download?.setOnClickListener(object : NotDoubleOnClickListener() {
            override fun notDoubleOnClick(view: View) {
                mPresenter?.download()
            }
        })
        btn_app_refresh?.setOnClickListener(object :NotDoubleOnClickListener(){
            override fun notDoubleOnClick(view: View) {
                mPresenter?.refreshLoadView()
            }

        })
        btn_app_share?.setOnClickListener(object : NotDoubleOnClickListener() {
            override fun notDoubleOnClick(view: View) {
                mPresenter?.share(File(Constant.Path.ROOT_DIR, "技术支持中心-AndroidSDK-蒋鹏(季度工作报告).pptx"))
            }
        })
        btn_app_web?.setOnClickListener(object : NotDoubleOnClickListener() {
            override fun notDoubleOnClick(view: View) {
                mPresenter?.showWebView("")
            }
        })
    }


    override fun showInitView(response: InitShowResponse) {
        this.activity?.createDialog(JDialog.DIALOG_NORMAL)
                ?.setTitle(response.title)
                ?.setMessage(response.message)
                ?.setCanceled(response.is_cancel)
                ?.let {
                    if (response.cancel_msg.isNotEmpty()) {
                        it.setCancel(response.cancel_msg, true,object : JDialog.DialogClickListener {
                            override fun onClick() {

                            }
                        })
                    }
                    if (response.confirm_msg.isNotEmpty()) {
                        it.setConfirm(response.confirm_msg, true,object : JDialog.DialogClickListener {
                            override fun onClick() {

                            }
                        })
                    }
                    it.show()
                }
    }

    override fun showInitH5View(response: InitShowResponse) {
        this.activity?.run {
                    getAlertDialog()
                    .setBackgroundRes(R.drawable.bg_normal_dialog_view)
                    .setLayoutRes(R.layout.view_photo_dialog)
                    .setCancelable(true)
                    .setTag("newDialog")
                    .setScreenHeightPercent( 0.2f)
                    .setScreenWidthPercent( 1.0f)
                    .setAnimationRes(R.style.NiceDialogAnim)
                    .setGravity(Gravity.BOTTOM)
                    .setDimAmount(0.0f)
                    .setBindViewListener(object : JAlertDialog.OnBindViewListener {
                        override fun onBindView(viewHolder: BaseAdapter.VH) {
                            viewHolder.getView<JTextView>(R.id.ntv_photo_dialog_camera).text = "相机"
                            viewHolder.getView<JTextView>(R.id.ntv_photo_dialog_photo).text = "照片"
                            viewHolder.getView<JTextView>(R.id.ntv_photo_dialog_cancel).text = "取消"

                        }
                    })
                    .addClickedId(R.id.ntv_photo_dialog_camera, R.id.ntv_photo_dialog_photo, R.id.ntv_photo_dialog_cancel)
                    .setViewClickListener(object : JAlertDialog.OnViewClickListener {
                        override fun onClick(viewHolder: BaseAdapter.VH, view: View, dialog: JAlertDialog) {
                            dialog.dismiss()
                            mPresenter?.checkToCameraOrPhoto(view, dialog)
                        }
                    })

                    .setKeyListener(DialogInterface.OnKeyListener { _, _, _ -> false })
                    .create()
                    .show()
        }
    }

    override fun showNotPermissionView(content: String) {
        this.activity?.createDialog(JDialog.DIALOG_NORMAL)
                ?.setTitle("关于权限")
                ?.setMessage(content)
                ?.setCanceled(true)
                ?.setCancel("取消", true,object : JDialog.DialogClickListener {
                    override fun onClick() {

                    }
                })?.setConfirm("去设置",true, object : JDialog.DialogClickListener {
                    override fun onClick() {
                        JPermissionsUtils.startActivityToSetting(context as Activity)
                    }
                })?.show()

    }

    override fun showUploadPic() {
        //照相、相片剪裁上传、显示demo
        this.activity?.run {
                    getAlertDialog()
                    .setBackgroundRes(R.drawable.bg_normal_dialog_view)
                    .setLayoutRes(R.layout.view_photo_dialog)
                    .setCancelable(true)
                    .setTag("newDialog")

                    .setAnimationRes(R.style.NiceDialogAnim)
                    .setGravity(Gravity.BOTTOM)
                    .setDimAmount(0.0f)
                    .setBindViewListener(object : JAlertDialog.OnBindViewListener {
                        override fun onBindView(viewHolder: BaseAdapter.VH) {
                            viewHolder.getView<JTextView>(R.id.ntv_photo_dialog_camera).text = "相机"
                            viewHolder.getView<JTextView>(R.id.ntv_photo_dialog_photo).text = "照片"
                            viewHolder.getView<JTextView>(R.id.ntv_photo_dialog_cancel).text = "取消"

                        }
                    })
                    .addClickedId(R.id.ntv_photo_dialog_camera, R.id.ntv_photo_dialog_photo, R.id.ntv_photo_dialog_cancel)
                    .setViewClickListener(object : JAlertDialog.OnViewClickListener {
                        override fun onClick(viewHolder: BaseAdapter.VH, view: View, dialog: JAlertDialog) {
                            dialog.dismiss()
                            mPresenter?.checkToCameraOrPhoto(view, dialog)
                        }
                    })

                    .setKeyListener(DialogInterface.OnKeyListener { _, _, _ -> false })
                    .create()
                    .show()
        }
    }

    override fun uploadResultView(url: String?) {
        Glide.with(this).load(url)
                .placeholder(R.drawable.loading_pic)//占位图
                .error(R.drawable.loading_error)//加载错误图
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)//取消缓存功能
                .override(300, 300)//设置尺寸
                .centerCrop()//中心裁剪
                .transform(CircleCrop())//圆形剪裁
                .into(img_head_view)
    }

    override fun showAppInfo(infos: MutableList<AppInfo>) {
        this.activity?.let {
            val adapter= object :BaseAdapter<AppInfo>(infos) {
                override fun getLayout(viewType: Int): Int {
                    return R.layout.app_info_view
                }

                override fun convert(holder: BaseAdapter.VH, item: AppInfo, position: Int, payloads: MutableList<Any>?) {
                    holder.getView<TextView>(R.id.tv_app_name).text = item.appName
                    holder.getView<TextView>(R.id.tv_package_name).text = item.packageName
                    holder.getView<ImageButton>(R.id.imb_del_app).setOnClickListener {
                        item.packageName?.let { s ->
                            this@TestFragment.activity?.deleteAppByPackageName(s)
                        }
                    }
                    holder.getView<ImageButton>(R.id.imb_start_up).setOnClickListener {
                        item.packageName?.let { s ->
                            this@TestFragment.activity?.startAppByPackageName(s)
                        }
                    }
                }
            }
            adapter.setItemClickListener(object : BaseAdapter.ItemClickListener<AppInfo> {
                override fun setItemClick(holder: BaseAdapter.VH, item: AppInfo, position: Int) {
                    it.showNormalToast(infos[position].appName)
                }

                override fun setItemLongClick(holder: BaseAdapter.VH, item: AppInfo, position: Int): Boolean {
                    it.showNormalToast(infos[position].signMd5)
                    return true
                }
            })
            it.getAlertDialog()
                    .setLayoutRes(R.layout.list_dialog_test)
                    .setCancelable(true)
                    .setTag("newDialog")
                    .setScreenHeightPercent(0.8f)
                    .setScreenWidthPercent(0.8f)
                    .setAnimationRes(R.style.NiceDialogAnim)
                    .setGravity(Gravity.CENTER)
                    .setDimAmount(0.2f)
                    .setListRes(R.id.recycler_test, LinearLayoutManager.VERTICAL)
                    .setAdapter(adapter)
                    .create()
                    .show()

        }
    }

    override fun showPatchDownLoad() {
        this.activity?.let {
            JAlertDialog.Builder(it.supportFragmentManager)
                    .setLayoutRes(R.layout.view_patch_dialog)
                    .setCancelable(true)
                    .setTag("newDialog")
//                    .setScreenHeightPercent(it, 0.4f)
                    .setScreenWidthPercent( 0.8f)
                    .setAnimationRes(R.style.NiceDialogAnim)
                    .setGravity(Gravity.CENTER)
                    .setDimAmount(0.0f)
                    .addClickedId(R.id.btn_patch_download)
                    .setViewClickListener(object : JAlertDialog.OnViewClickListener {
                        override fun onClick(viewHolder: BaseAdapter.VH, view: View, dialog: JAlertDialog) {
                            when (view.id) {
                                R.id.btn_patch_download -> {
                                    LogUtils.d("btn_patch_download.setOnClickListener")
                                    val dirPath = File(Constant.Path.ROOT_DIR, Constant.Path.PATCH_DEX_DIR).absolutePath
                                    mPresenter?.downLoadPatch("http://192.168.11.175:8000/file/upload/class2.dex", dirPath, object : JDownloadCallback {
                                        override fun next(responseBody: ResponseBody) {

                                        }

                                        override fun pause() {
                                        }

                                        override fun update(read: Long, count: Long, done: Boolean) {

                                        }

                                        override fun downloadSuccess() {
                                            viewHolder.getView<TextView>(R.id.tv_upload_content).text = "下载结束请重启App"

                                        }

                                        override fun downloadFailed(e: Throwable) {
                                            e.printStackTrace()

                                        }

                                        override fun downloadCancel() {

                                        }

                                    })
                                }
                            }
                        }

                    })
                    .create()
                    .show()
        }
    }

    override fun showLogin() {
        ParseVideoInUrl().getResponse("https://z1.m1907.cn/?jx=安家")
//        throw RuntimeException("Is RuntimeException.")
        this.activity?.let {
            JAlertDialog.Builder(it.supportFragmentManager)
                    .setLayoutRes(R.layout.view_login_dialog)
                    .setCancelable(true)
                    .setTag("newDialog")
                    .setScreenHeightPercent( 0.5f)
                    .setScreenWidthPercent( 0.8f)
                    .setAnimationRes(R.style.NiceDialogAnim)
                    .setGravity(Gravity.CENTER)
                    .setDimAmount(0.0f)
                    .addClickedId(R.id.btn_login_login)
                    .setViewClickListener(object : JAlertDialog.OnViewClickListener {
                        override fun onClick(viewHolder: BaseAdapter.VH, view: View, dialog: JAlertDialog) {
                            when (view.id) {
                                R.id.btn_login_login -> {
                                    val phone = viewHolder.getView<TextInputEditText>(R.id.input_edit_phone_login).text.toString()
                                    val pwd = viewHolder.getView<TextInputEditText>(R.id.input_edit_password_login).text.toString()
                                    mPresenter?.login(phone, pwd)
                                }
                            }
                        }
                    }).create()
                    .show()
        }
    }

    override fun showLoginResult(result: Boolean, message: String) {

    }

    override fun downloadList() {
        this.activity?.let {
            startActivity(Intent(it, DownloadListActivity::class.java))
        }
    }

    override fun showVideoView(urlList: MutableList<Video>) {
        this.activity?.let {
            JDialog
            JAlertDialog.Builder(it.supportFragmentManager)
                    .setLayoutRes(R.layout.view_video)
                    .setCancelable(false)
                    .setScreenWidthPercent(1.0f)
                    .setTag("videoDialog")
                    .setAnimationRes(R.style.NiceDialogAnim)
                    .setGravity(Gravity.CENTER)
                    .setDimAmount(0.8f)
                    .setBindViewListener(object :JAlertDialog.OnBindViewListener{
                        override fun onBindView(viewHolder: BaseAdapter.VH) {
                            this@TestFragment.activity?.let{ activity->
                                presenter = JVideoViewPresenter(
                                        activity,
                                        viewHolder.getView<JVideoView>(R.id.jv_video_main),
                                        VideoRepository.getInstance(RemoteVideoDataSource(urlList), LocalVideoDataSource()).apply {
                                            refreshVideos()
                                        }, JVideoState.PlayBackEngine.PLAYBACK_IJK_PLAYER)

                                presenter?.subscribe()
                            }
                        }
                    }).setDismissListener(DialogInterface.OnDismissListener {
                        LogUtils.d("view_video.dismiss()")
                        presenter?.unSubscribe()
                    }).create()
                    .show()
        }
    }

    override fun showRefreshLoadView() {
        activity?.let {
            it.startActivity(Intent(it, RefreshActivity::class.java))
        }
    }

    override fun showShareView() {

    }

    override fun showWebView(url: String) {
        this.activity?.let {
            startActivity(Intent(it, WebActivity::class.java))
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_test
    }

    override fun setPresenter(presenter: TestContract.Presenter) {
        mPresenter = presenter
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
        mPresenter?.subscribe()
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.unSubscribe()
        mPresenter = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mPresenter?.activityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mPresenter?.permissionResult(requestCode, permissions, grantResults)
    }

}