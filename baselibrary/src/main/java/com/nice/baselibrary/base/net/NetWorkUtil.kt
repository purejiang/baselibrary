package com.nice.baselibrary.base.net

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager

/**
 * @author Jp
 * @date 2021/1/22.
 */
class NetWorkUtil {
    companion object {
        const val NETWORK_WIFI = 1
        const val NETWORK_MOBILE = 0
        const val NETWORK_INVALID = -1
        const val NETWORK_UNKNOWN = -2

        /**
         * 主动获取是否联网
         *
         * @param context 上下文
         * @return 是否联网
         */
        fun isOnline(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetworkInfo
            return activeNetwork?.isConnectedOrConnecting ?: false
        }

        /**
         * 主动获取网络类型
         *
         * @param context 上下文
         * @return 网络类型(wifi or mobile)
         */
        fun getNetWorkType(context: Context): Int {
            val connManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mWifiInfo = connManager.activeNetworkInfo
            if (mWifiInfo?.isAvailable == true) {
                if (mWifiInfo?.type == ConnectivityManager.TYPE_WIFI) {
                    return NETWORK_WIFI
                } else if (mWifiInfo?.type == ConnectivityManager.TYPE_MOBILE) {
                    return NETWORK_MOBILE
                }
            } else {
                return NETWORK_INVALID
            }
            return NETWORK_UNKNOWN
        }


    }


}