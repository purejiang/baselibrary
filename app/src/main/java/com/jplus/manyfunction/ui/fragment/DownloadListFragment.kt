package com.jplus.manyfunction.ui.fragment


import android.os.Bundle

import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.jplus.manyfunction.R
import com.jplus.manyfunction.adapter.DownloadAdapter
import com.jplus.manyfunction.contract.DownloadListContract
import com.nice.baselibrary.base.common.Constant
import com.nice.baselibrary.base.ui.BaseFragment
import com.nice.baselibrary.widget.NiceEditText
import com.nice.baselibrary.base.net.download.NiceDownloadInfo
import java.io.File

/**
 * @author JPlus
 * @date 2019/2/16.
 */
class DownloadListFragment : BaseFragment(), DownloadListContract.ViewNice {

    private var mPresenter: DownloadListContract.Presenter?=null
    private var mDownloadRecy: RecyclerView?=null
    private var mDownloadAdapter: DownloadAdapter?=null

    private var mEditext: NiceEditText?=null
    private var mButton:Button?=null

    override fun getInitView(view: View?, bundle: Bundle?) {
        mDownloadRecy = view?.findViewById(R.id.rcy_downloads)
        val rvManager = LinearLayoutManager(this.context)
        mDownloadRecy?.layoutManager = rvManager
        rvManager.orientation = OrientationHelper.VERTICAL
        mEditext = view?.findViewById(R.id.base_edt_download_name)
        mButton = view?.findViewById(R.id.btn_commit_download)


    }

    override fun bindListener() {
        val urls = "https://cn5.3days.cc/hls/20190804/ebfd96741e2e6e854144b2e012c30755/1564883639/film_00000.ts"
        mEditext?.setText( urls)
        mButton?.setOnClickListener {
            val url = mEditext?.text.toString()
            if(url!=""){
                mPresenter?.addDownload(url, File(Constant.Path.ROOT_DIR, Constant.Path.DOWNLOAD_DIR).absolutePath)
                mEditext?.text?.clear()
            }
        }
    }


    override fun addDownload(item: NiceDownloadInfo) {
        mDownloadAdapter?.addItem(item)
    }
    override fun getLayoutId(): Int {
        return R.layout.fragemnt_download
    }

    override fun setPresenter(presenter: DownloadListContract.Presenter) {
       mPresenter = presenter
    }

    override fun showEmptyList() {

    }

    override fun onResume() {
        super.onResume()
        mPresenter?.subscribe()
    }

    override fun onPause() {
        super.onPause()
        mPresenter?.unSubscribe()
    }
    override fun showData(items: MutableList<NiceDownloadInfo>) {
        mDownloadAdapter = DownloadAdapter(items)
        mDownloadAdapter?.setItemClickListener(object : DownloadAdapter.ItemClickListener{
            override fun setItemClick(itemView: DownloadAdapter.VH, item: NiceDownloadInfo, position:Int) {
                mPresenter?.startDownload(item, itemView)
            }

            override fun setItemLongClick(itemView: DownloadAdapter.VH, item: NiceDownloadInfo, position:Int):Boolean {
                mPresenter?.removeDownload(position)
                return true
            }
        })
        mDownloadRecy?.adapter = mDownloadAdapter
    }


    override fun addDownloads(niceDownloads: MutableList<NiceDownloadInfo>) {

    }

    override fun removeDownload(position: Int) {
       mDownloadAdapter?.deleteItem(position)
    }
}