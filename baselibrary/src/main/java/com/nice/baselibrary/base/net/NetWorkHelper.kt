package com.nice.baselibrary.base.net

import android.content.Context
import android.net.*
import android.os.Build
import android.util.Log
import kotlin.math.log

/**
 * @author Jp
 * @date 2021/1/22.
 */
class NetWorkHelper {
    companion object {
        const val WIFI = "wifi"
        const val MOBILE_NET = "mobile_net"
        const val UNKNOWN = "unknown"

        private var mConnManager: ConnectivityManager?=null
        private var mCallback: ConnectivityManager.NetworkCallback?=null
        /**
         * 注册网络信息回调
         * @param context 上下文
         * @param callback 网络信息回调
         * @return 网络信息
         */
        fun registerCallback(context: Context, callback:NetCallback) {
            mConnManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            mCallback = object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    callback.onNetAvailable(true)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    callback.onNetAvailable(false)
                }

                override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                        when {
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                                callback.onNetType(WIFI)
                            }
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                                callback.onNetType(MOBILE_NET);
                            }
                            else -> {
                                callback.onNetType(UNKNOWN);
                            }
                        }
                    }
                }

                override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
                    super.onLinkPropertiesChanged(network, linkProperties)

                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mCallback?.let{
                    mConnManager?.registerDefaultNetworkCallback(it)
                }

            }

        }

        /**
         * 取消回调
         */
        fun unRegisterCallback(){
            mCallback?.let{
                mConnManager?.unregisterNetworkCallback(it)
            }

        }
    }

    interface NetCallback{
        /**
         * 是否能访问网络
         * @param available 网络是否可用
         */
        fun onNetAvailable(available: Boolean)

        /**
         * 返回网络类型
         * @param type 网络类型
         */
        fun onNetType(type: String)
    }
}