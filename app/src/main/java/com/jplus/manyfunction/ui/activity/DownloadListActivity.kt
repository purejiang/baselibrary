package com.jplus.manyfunction.ui.activity

import android.os.Bundle
import com.jplus.manyfunction.R
import com.jplus.manyfunction.presenter.DownloadListPresenter
import com.nice.baselibrary.base.ui.BaseActivity
import com.jplus.manyfunction.ui.fragment.DownloadListFragment


/**
 * @author JPlus
 * @date 2019/2/13.
 */
class DownloadListActivity : BaseActivity() {

    private val mDownloadFragment: DownloadListFragment by lazy {
        DownloadListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

    }
    override fun onInit() {


    }

    override fun onFindView() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fly_download, mDownloadFragment)
        transaction.commit()
    }

    override fun onBindListener() {
//        bt_download.setMiddleTitle("下载列表")
        DownloadListPresenter(this, mDownloadFragment)
    }

    override fun setBackTwiceMsg(): String? {
        return null
    }
}