package com.jplus.manyfunction.ui.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jplus.manyfunction.R
import com.nice.baselibrary.base.adapter.NiceAdapter
import com.nice.baselibrary.widget.NiceShowView
import kotlinx.android.synthetic.main.activity_refresh.*


class RefreshActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refresh)
        val list = arrayListOf<String>()
        for (i in 1..100) {
            list.add("item$i")
        }
        val niceAdapter = object : NiceAdapter<String>(list) {
            override fun getLayout(viewType: Int): Int {
                return R.layout.view_test_jry
            }

            override fun convert(holder: VH, item: String, position: Int, payloads: MutableList<Any>?) {
                holder.getView<TextView>(R.id.tv_jry_test).text = item
            }
        }
        niceAdapter.addFootView(R.layout.view_test_foot)
        niceAdapter.setItemClickListener(object : NiceAdapter.ItemClickListener {
            override fun setItemClick(holder: NiceAdapter.VH, position: Int) {
                NiceShowView.instance.NormalToast("setItemClick$position")
            }

            override fun setItemLongClick(holder: NiceAdapter.VH, position: Int): Boolean {
                NiceShowView.instance.NormalToast("setItemLongClick$position")
                return true
            }
        })
//        smrl_test.setOnRefreshListener { refreshlayout ->
//            refreshlayout.finishRefresh(2000/*,false*/)//传入false表示刷新失败
//        }
//        smrl_test.setOnLoadMoreListener { refreshlayout ->
//            refreshlayout.finishLoadMore(2000/*,false*/)//传入false表示加载失败
//        }
//        jrv_test.setJRefreshViewListener(object : JRefreshView.JRefreshListener {
//            override fun refreshListener() {
//                NiceShowView.instance.NormalToast("refreshListener").show()
//            }
//            override fun loadListener() {
//                NiceShowView.instance.NormalToast("loadListener").show()
//            }
//        }).setAdapter(niceAdapter)

    }
}
