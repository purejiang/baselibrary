package com.nice.baselibrary.base.net

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Parcelable
import android.telephony.PhoneStateListener
import android.telephony.SignalStrength
import android.telephony.TelephonyManager
import com.nice.baselibrary.base.net.NetWorkUtil.Companion.NETWORK_WIFI
import com.nice.baselibrary.base.net.NetWorkUtil.Companion.NETWORK_INVALID
import com.nice.baselibrary.base.net.NetWorkUtil.Companion.NETWORK_MOBILE

/**
 * 广播获取网络信息
 * @author Jp
 * @date 2021/1/22.
 */
class NetWorkReceiver(private val mOnNetWorkChangeListener: OnNetWorkChangeListener): BroadcastReceiver(){
    companion object{
        private var netWorkReceiver: NetWorkReceiver? = null
        /**
         * 被动监听网络信息，无信号强度的获取
         *
         * @param context 上下文
         * @param isGetRssi 是否获取信号强度
         * @param onNetWorkChangeListener
         * @return 返回NetWorkReceiver
         */
        fun register(context: Context, isGetRssi: Boolean, onNetWorkChangeListener: OnNetWorkChangeListener) {
            netWorkReceiver = NetWorkReceiver(onNetWorkChangeListener)
            val intentFilter = IntentFilter()
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
            if (isGetRssi) {
                intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
                intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
                intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION)
            }
            context.registerReceiver(netWorkReceiver, intentFilter)
        }

        /**
         * 注销广播
         * @param context 上下文
         */
        fun unRegister(context: Context) {
            if (netWorkReceiver != null) {
                context.unregisterReceiver(netWorkReceiver)
            }
        }
    }
    
        private var mTelephonyManager: TelephonyManager? = null

        override fun onReceive(context: Context, intent: Intent) {
            //检查网络状态的类型
            val netWorkStatus = NetWorkUtil.getNetWorkType(context)
            //WIFI状态下返回的信号强度
            if (netWorkStatus == NETWORK_WIFI || netWorkStatus ==NETWORK_INVALID) {
                val connectionInfo = (context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager).connectionInfo
                if (connectionInfo != null) {
                    val dbm = connectionInfo.rssi
                    //信号强度的单位有dbm和asu，一般手机获取的都是dbm(负值)，
                    mOnNetWorkChangeListener.getNetStrength(dbm2asu(""+dbm))
                    // Log.d("pipa", "wifi:" + dbm2asu(dbm));
                }
                //移动网络下的信号强度
            } else if (netWorkStatus == NETWORK_MOBILE) {
                mTelephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                //开始监听
                val mListener = PhoneStatListener()
                mTelephonyManager?.listen(mListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS)
            }
            mOnNetWorkChangeListener.getNetType(netWorkStatus)

            // 在接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播，当然刚打开wifi肯定还没有连接到有效的无线
            if (WifiManager.NETWORK_STATE_CHANGED_ACTION == intent.action) {
                val parcelableExtra = intent.getParcelableExtra<Parcelable>(WifiManager.EXTRA_NETWORK_INFO)
                if (null != parcelableExtra) {
                    val networkInfo = parcelableExtra as NetworkInfo
                    val state = networkInfo.state
                    //当然，这边可以更精确的确定状态
                    mOnNetWorkChangeListener.getNetInfo(state == NetworkInfo.State.CONNECTED);
                }
            }
        }

        private inner class PhoneStatListener : PhoneStateListener() {
            //获取信号强度
            override fun onSignalStrengthsChanged(signalStrength: SignalStrength) {
                super.onSignalStrengthsChanged(signalStrength)
                //获取0-4的5种信号级别，越大信号越好,但是api23开始才能用
                //int level = signalStrength.getLevel();

                //获取网络信号强度
                if (mTelephonyManager?.networkType == TelephonyManager.NETWORK_TYPE_LTE) {
                    //4G网络
                    val signalInfo = signalStrength.toString()
                    val params = signalInfo.split(" ".toRegex()).toTypedArray()
                    mOnNetWorkChangeListener.getNetStrength(dbm2asu(params[9]))
                    //  Log.d("pipa", ""+dbm2asu(Integer.parseInt(params[9])));
                } else if (mTelephonyManager?.networkType == TelephonyManager.NETWORK_TYPE_HSDPA || mTelephonyManager?.networkType == TelephonyManager.NETWORK_TYPE_HSPA || mTelephonyManager?.networkType == TelephonyManager.NETWORK_TYPE_HSUPA || mTelephonyManager?.networkType == TelephonyManager.NETWORK_TYPE_UMTS) {
                    mOnNetWorkChangeListener.getNetStrength(dbm2asu(""+signalStrength.gsmSignalStrength))
                    //  Log.d("pipa", ""+dbm2asu(signalStrength.getGsmSignalStrength()));
                } else {
                    mOnNetWorkChangeListener.getNetStrength(dbm2asu(""+signalStrength.gsmSignalStrength))
                    //  Log.d("pipa", ""+dbm2asu(signalStrength.getGsmSignalStrength()));
                }
                //  Log.d("pipa", ""+dbm2asu(signalStrength.getGsmSignalStrength()));
            }
        }

        /**
         * dbm转换asu (ASU - 141) ≤ dBm < (ASU - 140)，dbm=-113+(2*asu)
         * @param dbm 强度
         * @return int
         */
        private fun dbm2asu(dbm: String): Int {
            return try{
                (dbm.toInt() + 113) / 2
            }catch (e:Exception){
                e.printStackTrace()
                0
            }

        }


        /**
         * 返回网络信息的接口
         */
        interface OnNetWorkChangeListener {
            /**
             * 是否能访问网络
             * @param isOnline 是否能访问网络
             */
            fun getNetInfo(isOnline: Boolean)

            /**
             * 返回网络类型
             * @param type 网络类型
             */
            fun getNetType(type: Int)

            /**
             * 返回网络强弱
             * @param strength 网络强弱
             */
            fun getNetStrength(strength: Int)
        }
}