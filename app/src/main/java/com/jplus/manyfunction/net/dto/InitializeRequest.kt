package com.jplus.manyfunction.net.dto

/**
 * @author JPLus
 * @date 2019/6/22.
 */
data class InitializeRequest(val packageName: String,//包名
                             val versionName: String,//app版本名
                             val sdkVersion: String, // 系统版本 system_version 如 9.0
                             val driverId: String,//设备唯一标识
                             var sysName: String, // 系统名称 system_name 如 iPhone
                             var network: String, // 网络环境 1.wifi 2.4G 3.3G 4.2G
                             var mac: String,  // MAC
                             var screen: String, // 屏幕分辨率
                             var model: String,  // 设备机型，如 MI 2S
                             var sysInfo: String,  // 设备详细信息
                             var other: String// 其它信息
)