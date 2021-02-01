package com.jplus.manyfunction.ui.fragment

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.jplus.manyfunction.download.DownloadCallback
import com.jplus.manyfunction.net.dto.InitShowResponse
import com.jplus.manyfunction.ui.activity.DownloadListActivity
import com.jplus.manyfunction.ui.activity.RefreshActivity
import com.jplus.manyfunction.ui.activity.WebActivity
import com.jplus.manyfunction.utils.ParseVideoInUrl
import com.nice.baselibrary.base.adapter.BaseAdapter
import com.nice.baselibrary.base.common.Constant
import com.nice.baselibrary.base.entity.vo.AppInfo
import com.nice.baselibrary.base.listener.NotDoubleOnClickListener
import com.nice.baselibrary.base.ui.BaseFragment
import com.nice.baselibrary.base.utils.*
import com.nice.baselibrary.widget.JTextView
import com.nice.baselibrary.widget.dialog.BaseAlertDialog
import com.nice.baselibrary.widget.dialog.JDialog
import kotlinx.android.synthetic.main.fragment_test.*
import okhttp3.ResponseBody
import java.io.File

/**
 * @author JPlus
 * @date 2019/5/8.
 */
class TestFragment : BaseFragment(), TestContract.View {

    private var presenter: JVideoViewPresenter? = null
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
        btn_app_refresh?.setOnClickListener(object : NotDoubleOnClickListener() {
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
        btn_web_source?.setOnClickListener(object : NotDoubleOnClickListener() {
            override fun notDoubleOnClick(view: View) {
                activity?.run {
                    getAlertDialog()
                            .setBackgroundRes(R.drawable.bg_normal_dialog_view)
                            .setLayoutRes(R.layout.view_web_source_dialog)
                            .setCancelable(true)
                            .setTag("webSource")
                            .setAnimationRes(R.style.NiceDialogAnim)
                            .setGravity(Gravity.BOTTOM)
                            .setDimAmount(0.0f)
                            .setBindViewListener(object : BaseAlertDialog.OnBindViewListener {
                                override fun onBindView(viewHolder: BaseAdapter.VH) {
                                    val editText: EditText = viewHolder.getView<EditText>(R.id.edt_web_url)
                                    editText.hint = "输入链接"
                                    viewHolder.getView<Button>(R.id.btn_get_source).setOnClickListener {
                                        mPresenter?.getWebSource(editText.text.toString())
                                    }
                                }
                            })
                            .setKeyListener(DialogInterface.OnKeyListener { _, _, _ -> false })
                            .build()
                            .show()
                }
            }
        })
        btn_net_work?.setOnClickListener(object : NotDoubleOnClickListener() {
            override fun notDoubleOnClick(view: View) {
                mPresenter?.getNetWork()
            }

        })
        mPresenter?.onNetWorkCallback()
//        jdt_text?.setEditCallBack {
//            Log.d("pipa", "jdt_text.changedToEmpty")
//        }
    }

    override fun showNetWork(state: String?, isOnline: Boolean?) {
        btn_net_work?.text = "state:$state, isOnline:$isOnline"
    }
    override fun showNetWorkState(state: String?, isOnline: Boolean?, strength: Int?) {

        state?.let {
            tv_net_work_state.text = "state:${it}"
//            when (it) {
//                NetWorkReceiver.NETWORK_INVALID -> tv_net_work_state.text = "state:无网络"
//                NetWorkReceiver.NETWORK_WIFI -> tv_net_work_state.text = "state:Wifi"
//                NetWorkReceiver.NETWORK_MOBILE -> tv_net_work_state.text = "state:移动网络"
//                else -> tv_net_work_state.text = "state:未知网络"
//            }
        }
        isOnline?.let {
            tv_net_work_online.text = ", isOnline:${isOnline}"
        }
        strength?.let {
            tv_net_work_strength.text = ",strength: ${strength}"
        }

    }

    override fun showInitView(response: InitShowResponse) {
        this.activity?.createDialog(JDialog.DIALOG_NORMAL)
                ?.setTitle(response.title)
                ?.setMessage(response.message)
                ?.setCanceled(response.is_cancel)
                ?.let {
                    if (response.cancel_msg.isNotEmpty()) {
                        it.setCancel(response.cancel_msg, true, object : JDialog.DialogClickListener {
                            override fun onClick() {

                            }
                        })
                    }
                    if (response.confirm_msg.isNotEmpty()) {
                        it.setConfirm(response.confirm_msg, true, object : JDialog.DialogClickListener {
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
                    .setScreenHeightPercent(0.2f)
                    .setScreenWidthPercent(1.0f)
                    .setAnimationRes(R.style.NiceDialogAnim)
                    .setGravity(Gravity.BOTTOM)
                    .setDimAmount(0.0f)
                    .setBindViewListener(object : BaseAlertDialog.OnBindViewListener {
                        override fun onBindView(viewHolder: BaseAdapter.VH) {
                            viewHolder.getView<JTextView>(R.id.ntv_photo_dialog_camera).text = "相机"
                            viewHolder.getView<JTextView>(R.id.ntv_photo_dialog_photo).text = "照片"
                            viewHolder.getView<JTextView>(R.id.ntv_photo_dialog_cancel).text = "取消"

                        }
                    })
                    .addClickedId(R.id.ntv_photo_dialog_camera, R.id.ntv_photo_dialog_photo, R.id.ntv_photo_dialog_cancel)
                    .setViewClickListener(object : BaseAlertDialog.OnViewClickListener {
                        override fun onClick(viewHolder: BaseAdapter.VH, view: View, dialog: BaseAlertDialog) {
                            dialog.dismiss()
                            mPresenter?.checkToCameraOrPhoto(view, dialog)
                        }
                    })

                    .setKeyListener(DialogInterface.OnKeyListener { _, _, _ -> false })
                    .build()
                    .show()
        }
    }

    override fun showNotPermissionView(content: String) {
        this.activity?.createDialog(JDialog.DIALOG_NORMAL)
                ?.setTitle("关于权限")
                ?.setMessage(content)
                ?.setCanceled(true)
                ?.setCancel("取消", true, object : JDialog.DialogClickListener {
                    override fun onClick() {

                    }
                })?.setConfirm("去设置", true, object : JDialog.DialogClickListener {
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
                    .setBindViewListener(object : BaseAlertDialog.OnBindViewListener {
                        override fun onBindView(viewHolder: BaseAdapter.VH) {
                            viewHolder.getView<JTextView>(R.id.ntv_photo_dialog_camera).text = "相机"
                            viewHolder.getView<JTextView>(R.id.ntv_photo_dialog_photo).text = "照片"
                            viewHolder.getView<JTextView>(R.id.ntv_photo_dialog_cancel).text = "取消"

                        }
                    })
                    .addClickedId(R.id.ntv_photo_dialog_camera, R.id.ntv_photo_dialog_photo, R.id.ntv_photo_dialog_cancel)
                    .setViewClickListener(object : BaseAlertDialog.OnViewClickListener {
                        override fun onClick(viewHolder: BaseAdapter.VH, view: View, dialog: BaseAlertDialog) {
                            dialog.dismiss()
                            mPresenter?.checkToCameraOrPhoto(view, dialog)
                        }
                    })

                    .setKeyListener(DialogInterface.OnKeyListener { _, _, _ -> false })
                    .build()
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

    private var recyclerTest: RecyclerView? = null
    override fun showAppInfo(infos: MutableList<AppInfo>) {
        val adapter = object : BaseAdapter<AppInfo>(infos) {
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
                this@TestFragment.activity?.showNormalToast(infos[position].appName)
            }

            override fun setItemLongClick(holder: BaseAdapter.VH, item: AppInfo, position: Int): Boolean {
                this@TestFragment.activity?.showNormalToast(infos[position].signMd5)
                return true
            }
        })
        recyclerTest?.adapter = adapter
    }

    override fun showAppInfoDialog() {
        this.activity?.let {
            it.getAlertDialog()
                    .setLayoutRes(R.layout.list_dialog_test)
                    .setCancelable(true)
                    .setTag("newDialog")
                    .setScreenHeightPercent(0.8f)
                    .setScreenWidthPercent(0.8f)
                    .setAnimationRes(R.style.NiceDialogAnim)
                    .setBindViewListener(object : BaseAlertDialog.OnBindViewListener {
                        override fun onBindView(viewHolder: BaseAdapter.VH) {
                            viewHolder.getView<RecyclerView>(R.id.recycler_test).let { rcy ->
                                recyclerTest = rcy
                                rcy.layoutManager = LinearLayoutManager(it).apply {
                                    this.orientation = LinearLayoutManager.VERTICAL
                                }
                            }
                        }
                    })
                    .setGravity(Gravity.CENTER)
                    .setDimAmount(0.2f)
                    .build()
                    .show()
        }
    }


    override fun showPatchDownLoad() {
        this.activity?.let {
            BaseAlertDialog.Builder(it.supportFragmentManager)
                    .setLayoutRes(R.layout.view_patch_dialog)
                    .setCancelable(true)
                    .setTag("newDialog")
//                    .setScreenHeightPercent(it, 0.4f)
                    .setScreenWidthPercent(0.8f)
                    .setAnimationRes(R.style.NiceDialogAnim)
                    .setGravity(Gravity.CENTER)
                    .setDimAmount(0.0f)
                    .addClickedId(R.id.btn_patch_download)
                    .setViewClickListener(object : BaseAlertDialog.OnViewClickListener {
                        override fun onClick(viewHolder: BaseAdapter.VH, view: View, dialog: BaseAlertDialog) {
                            when (view.id) {
                                R.id.btn_patch_download -> {
                                    LogUtils.d("btn_patch_download.setOnClickListener")
                                    val dirPath = File(Constant.Path.ROOT_DIR, Constant.Path.PATCH_DEX_DIR).absolutePath
                                    mPresenter?.downLoadPatch("http://192.168.11.175:8000/file/upload/class2.dex", dirPath, object : DownloadCallback {
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
                    .build()
                    .show()
        }
    }

    override fun showLogin() {
        ParseVideoInUrl().getResponse("https://z1.m1907.cn/?jx=安家")
//        throw RuntimeException("Is RuntimeException.")
        this.activity?.let {
            BaseAlertDialog.Builder(it.supportFragmentManager)
                    .setLayoutRes(R.layout.view_login_dialog)
                    .setCancelable(true)
                    .setTag("newDialog")
                    .setScreenHeightPercent(0.5f)
                    .setScreenWidthPercent(0.8f)
                    .setAnimationRes(R.style.NiceDialogAnim)
                    .setGravity(Gravity.CENTER)
                    .setDimAmount(0.0f)
                    .addClickedId(R.id.btn_login_login)
                    .setViewClickListener(object : BaseAlertDialog.OnViewClickListener {
                        override fun onClick(viewHolder: BaseAdapter.VH, view: View, dialog: BaseAlertDialog) {
                            when (view.id) {
                                R.id.btn_login_login -> {
                                    val phone = viewHolder.getView<TextInputEditText>(R.id.input_edit_phone_login).text.toString()
                                    val pwd = viewHolder.getView<TextInputEditText>(R.id.input_edit_password_login).text.toString()
                                    mPresenter?.login(phone, pwd)
                                }
                            }
                        }
                    }).build()
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
            BaseAlertDialog.Builder(it.supportFragmentManager)
                    .setLayoutRes(R.layout.view_video)
                    .setCancelable(false)
                    .setScreenWidthPercent(1.0f)
                    .setTag("videoDialog")
                    .setAnimationRes(R.style.NiceDialogAnim)
                    .setGravity(Gravity.CENTER)
                    .setDimAmount(0.8f)
                    .setBindViewListener(object : BaseAlertDialog.OnBindViewListener {
                        override fun onBindView(viewHolder: BaseAdapter.VH) {
                            this@TestFragment.activity?.let { activity ->
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
                    }).build()
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