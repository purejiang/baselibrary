//package com.nice.baselibrary.widget.refresh
//
//import android.content.Context
//import android.util.AttributeSet
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.nice.baselibrary.base.adapter.BaseAdapter
//import kotlinx.android.synthetic.main.view_refresh_load.view.*
//
//
///**
// * @author JPlus
// * @date 2019/11/29.
// */
//class JRefreshView(mContext: Context?, attrs: AttributeSet?) : ViewGroup(mContext, attrs) {
////    constructor(mContext: Context) : super(mContext)
////    constructor(mContext: Context, attributeSet: AttributeSet) : super(mContext, attributeSet)
////    constructor(mContext: Context, attributeSet: AttributeSet,  defStyleAttr:Int) : super(mContext, attributeSet, defStyleAttr)
//
//    private var mView: View? = null
//    private var mListener: JRefreshListener? = null
//    private var mLoadRes: Int? = null
//    private val mRvManager by lazy {
//        LinearLayoutManager(mContext)
//    }
//
//    init {
////        mView = LayoutInflater.from(mContext).inflate(R.layout.view_refresh_load, this)
////        swrl_jrefresh.setOnRefreshListener {
////            mListener?.refreshListener()
////        }
//
//    }
//
//    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
//        val childCount = childCount
//        for (i in 0 until childCount) {
//            val childView = getChildAt(i)
//            childView.layout(l, i * height, r, (i + 1) * height)
//        }
//    }
//
////    fun setLoadView(loadVieRes: Int) {
////        rcy_jrefresh_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
////            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
////                super.onScrollStateChanged(recyclerView, newState)
////            }
////
////            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
////                super.onScrolled(recyclerView, dx, dy)
////                val lcp = mRvManager.findLastCompletelyVisibleItemPosition()
////                Log.d("pipa", "lcp:$lcp,mRvManager.itemCount:${mRvManager.itemCount}")
////
////
////            }
////        })
////    }
//
//    fun setAdapter(adapter: BaseAdapter<*>): JRefreshView {
//        rcy_jrefresh_view.layoutManager = mRvManager
//        mRvManager.orientation = RecyclerView.VERTICAL
//        rcy_jrefresh_view.adapter = adapter
//        return this
//    }
//
//    fun setJRefreshViewListener(listener: JRefreshListener): JRefreshView {
//        mListener = listener
//        return this
//    }
//
//    fun refreshEnd() {
//        swrl_jrefresh?.isRefreshing = false
//    }
//
//    fun loadEnd() {
//
//    }
//
//    interface JRefreshListener {
//
//        fun refreshListener()
//
//        fun loadListener()
//    }
//}