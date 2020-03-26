package com.jplus.manyfunction.ui.activity


import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import com.jplus.manyfunction.R
import com.jplus.manyfunction.presenter.TestPresenter
import com.jplus.manyfunction.ui.fragment.TestFragment
import com.nice.baselibrary.base.ui.BaseActivity
import com.nice.baselibrary.widget.dialog.JAlertDialog


class MainActivity : BaseActivity() {


    private var jDialog: JAlertDialog? = null

    private var mFragment: TestFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        AppUtils.instance.init(this)
//        LogUtils.init(this, true)
//        JPermissionsUtils.instance.init(this)
//        CrashHandler.instance.init(this, 1)
//        Log.d("pipa", "" + CrashHandler.instance.getAllFiles())
//        Log.d("pipa", "getdate" + DateUtils.getEndDateTime(true,Date(System.currentTimeMillis())))
//        LeniuPaySdk.getInstance(this).showPayDialog("元宝", null, "50")
    }

    override fun onContentChanged() {
        super.onContentChanged()
        Log.d("pipa", "onContentChanged")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("pipa", "onSaveInstanceState")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d("pipa", "onConfigurationChanged")
    }
    override fun onStart() {
        super.onStart()
//        throw RuntimeException("test failed")

    }

    override fun finish() {
        super.finish()

    }

    override fun onDestroy() {
        super.onDestroy()
    }
/*

*/


//        Assist.init(this)
//        Assist.getAutoNetWork(false).SetOnNetWorkChangeListener(object :NetWorkReceiver.OnNetWorkChangeListener{
//            override fun getNetStatus(isOnline: Boolean) {
//                LogUtils.e("isOnline:"+isOnline)
//            }
//
//            override fun getNetType(type: Int) {
//                val message = when(type){
//                    NetWorkReceiver.NETWORK_MOBILE -> "移动网络已连接！"
//                    NetWorkReceiver.NETWORK_WIFI -> "Wifi已连接"
//                    NetWorkReceiver.NETWORK_INVALID ->"网络未连接"
//                    else -> {
//                        "未知网络类型"
//                    }
//                }
//                NiceShowView.instance.NormalToast(message).show()
////                niceDialog?.dismiss()
//            }
//
//            override fun getNetStrength(strength: Int) {
//                LogUtils.e("strength:"+strength)
//            }
//        })


    override fun onInit() {

    }

    override fun onFindView() {
        if(mFragment ==null){
            mFragment = TestFragment()
        }
        val translation = supportFragmentManager.beginTransaction()
        translation.add(R.id.test_fragment_view, mFragment!!)
        translation.commit()
    }

    override fun onBindListener() {
        mFragment?.let{
            TestPresenter(it, this)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("pipa", "onActivityResult")
        mFragment?.onActivityResult(requestCode, resultCode, data)//在Fragment里收到ActivityResult的结果
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mFragment?.onRequestPermissionsResult(requestCode, permissions, grantResults)//在Fragment里收到权限请求的结果
    }

    override fun setBackTwiceMsg(): String {
        return "再按一次退出~"
    }


}

