package com.jplus.manyfunction.ui.activity

import android.os.Bundle
import com.jplus.manyfunction.R
import com.jplus.manyfunction.presenter.DownloadListPresenter
import com.nice.baselibrary.base.BaseActivity
import com.jplus.manyfunction.ui.fragment.DownloadListFragment
import com.nice.baselibrary.base.common.ApiEntry
import com.nice.baselibrary.base.view.NiceTitleBar
import com.nice.baselibrary.download.NiceDownloadDataSource


/**
 * @author JPlus
 * @date 2019/2/13.
 */
class DownloadListActivity :BaseActivity() {

    private var mDownloadFragment: DownloadListFragment?=null
    private var mDownloadTitle: NiceTitleBar?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)
        ApiEntry.getInstance().init(this, true)
    }

    override fun onStart() {
        super.onStart()
        ApiEntry.getInstance().requestPermission()
    }

    override fun onInit() {

    }

    override fun onFindView() {
        mDownloadTitle = findViewById(R.id.bt_download)

        if (mDownloadFragment == null) {
            mDownloadFragment = DownloadListFragment()
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fly_download, mDownloadFragment)
        transaction.commit()

    }

    override fun onBindListener() {
        mDownloadTitle?.setMainTitle("下载列表")
        DownloadListPresenter(this, mDownloadFragment, NiceDownloadDataSource(applicationContext))
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
        ApiEntry.getInstance().handleRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}