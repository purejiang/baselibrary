package com.jplus.manyfunction.ui.activity

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.jplus.manyfunction.R
import com.nice.baselibrary.base.adapter.BaseAdapter
import com.nice.baselibrary.base.adapter.BaseAdapterWrapper
import com.nice.baselibrary.base.utils.showNormalToast
import com.nice.baselibrary.widget.loading.LoadingListener
import com.nice.baselibrary.widget.loading.LoadingManager
import kotlinx.android.synthetic.main.activity_refresh.*


class RefreshActivity : AppCompatActivity() {
    private var mloadingmanager: LoadingManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refresh)
        mloadingmanager = LoadingManager(this, object : LoadingListener() {
            override fun setRetryEvent(retryView: View?) {

            }

            override fun setEmptyEvent(emptyView: View?) {

            }

            override fun setLoadingEvent(loadingView: View?) {

            }

            override fun generateLoadingLayoutId(): Int {
                return super.generateLoadingLayoutId()
            }
        })
        mloadingmanager?.showLoading()
        Handler().postDelayed({
            initView()
        },3000)

//        jrv_test.setJRefreshViewListener(object : JRefreshView.JRefreshListener {
//            override fun refreshListener() {
//                NiceShowView.instance.NormalToast("refreshListener").show()
//            }
//            override fun loadListener() {
//                NiceShowView.instance.NormalToast("loadListener").show()
//            }
//        }).setAdapter(niceAdapter)

    }

    private fun initView() {
        val list = arrayListOf<String>()
        for (i in 1..100) {
            list.add("item$i")
        }
        val niceAdapter = object : BaseAdapter<String>(list) {
            override fun getLayout(viewType: Int): Int {
                return R.layout.view_test_jry
            }

            override fun convert(holder: VH, item: String, position: Int, payloads: MutableList<Any>?) {
                holder.getView<TextView>(R.id.tv_jry_test).text = item
            }
        }

        niceAdapter.setItemClickListener(object : BaseAdapter.ItemClickListener<String> {
            override fun setItemClick(holder: BaseAdapter.VH, item: String, position: Int) {
                this@RefreshActivity.showNormalToast("setItemClick$position")
            }

            override fun setItemLongClick(holder: BaseAdapter.VH, item: String, position: Int): Boolean {
                this@RefreshActivity.showNormalToast("setItemLongClick$position")
                return true
            }
        })
        val adapterWrapper = BaseAdapterWrapper(niceAdapter, object : BaseAdapterWrapper.CreatedListener {
            override fun setHeadView(): View {
                return LayoutInflater.from(this@RefreshActivity).inflate(R.layout.view_test_head, null)
            }

            override fun setFootView(): View {
                return LayoutInflater.from(this@RefreshActivity).inflate(R.layout.view_test_foot, null)
            }
        })
        smrl_test.setOnRefreshListener { refreshlayout ->
            refreshlayout.finishRefresh(2000/*,false*/)//传入false表示刷新失败
        }
        smrl_test.setOnLoadMoreListener { refreshlayout ->
            refreshlayout.finishLoadMore(2000/*,false*/)//传入false表示加载失败
        }
        val rvManager = LinearLayoutManager(this)
        rvManager.stackFromEnd = true
        rvManager.reverseLayout = true
        rcy_test.layoutManager = rvManager
        rcy_test.itemAnimator = DefaultItemAnimator()
        rcy_test.adapter = adapterWrapper
        mloadingmanager?.showContent()
    }
}
