package com.jplus.manyfunction.ui.activity

import android.content.Intent
import android.os.Bundle
import com.jplus.manyfunction.R
import com.jplus.manyfunction.presenter.DownloadListPresenter
import com.nice.baselibrary.base.ui.BaseActivity
import com.jplus.manyfunction.ui.fragment.DownloadListFragment
import com.nice.baselibrary.widget.NiceTitleBar
import com.nice.baselibrary.base.net.download.JDownloadDataSource


/**
 * @author JPlus
 * @date 2019/2/13.
 */
class DownloadListActivity : BaseActivity() {
    companion object {
        private val EXIT_TIME = 2000L
    }

    private var mDownloadFragment: DownloadListFragment?=null
    private var mDownloadTitle: NiceTitleBar?=null
    private var mExitTime=0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

    }

    override fun onStart() {
        super.onStart()
//        NicePermissionUtils.getInstance().requ
//        ApiEntry.getInstance().requestPermission(this, Manifest.permission.CAMERA)
    }
    override fun onInit() {


    }

    override fun onFindView() {
        mDownloadTitle = findViewById(R.id.bt_download)

        if (mDownloadFragment == null) {
            mDownloadFragment = DownloadListFragment()
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fly_download, mDownloadFragment!!)
        transaction.commit()
    }

    override fun onBindListener() {
        mDownloadTitle?.setMainTitle("下载列表")
        DownloadListPresenter(mDownloadFragment, JDownloadDataSource(applicationContext))
    }


    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        ApiEntry.getInstance().handleRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun setBackTwiceMsg(): String {
        return "再按一次就退出了哦~"
    }

}