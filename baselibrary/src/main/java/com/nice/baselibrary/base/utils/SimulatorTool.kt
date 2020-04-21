package com.nice.baselibrary.base.utils

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils

/**
 * @author JPlus
 * @date 2020/4/21.
 */
class SimulatorTool {
    companion object{
        /**
         * 判断是否模拟器
         * @param context
         * @return
         */
        fun isEmulator(context: Context): Boolean {
            return isNotLightSensorManager(context) ||isNotCall(context) || isNotCamera(context) || isNotBlueTooth()
        }

        /**
         * 是否没有光传感器
         *
         * @param context
         * @return
         */
        fun isNotLightSensorManager(context: Context): Boolean {
            val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            val sensor8 = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) //光
            val sensor9 = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) //距离
            return null == sensor8 && null == sensor9
        }

        /**
         * 是否不能跳转到拨号界面
         *
         * @param context
         * @return
         */
        fun isNotCall(context: Context): Boolean {
            val url = "tel:" + "123456"
            val intent = Intent()
            intent.data = Uri.parse(url)
            intent.action = Intent.ACTION_DIAL
            //activity是否可达
            return intent.resolveActivity(context.packageManager) == null
        }

        /**
         * 是否没有蓝牙
         *
         * @return
         */
        fun isNotBlueTooth(): Boolean {
            val ba = BluetoothAdapter.getDefaultAdapter()
            return if (ba == null) {
                true
            } else { // 如果有蓝牙不一定是有效的。获取蓝牙名称，若为null 则默认为模拟器
                val name = ba.name
                TextUtils.isEmpty(name)
            }
        }

        /**
         * 是否没有相机
         * @param context
         * @return
         */
        fun isNotCamera(context: Context): Boolean {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            return intent.resolveActivity(context.packageManager) == null
        }

    }
}