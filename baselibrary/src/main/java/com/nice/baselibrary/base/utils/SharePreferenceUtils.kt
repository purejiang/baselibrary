package com.nice.baselibrary.base.utils

import android.content.Context
import android.content.SharedPreferences
import com.nice.baselibrary.base.common.Constant



/**
 * @author JPlus
 * @date 2019/3/4.
 */
class SharePreferenceUtils {
    companion object {
        fun create(context: Context, name: String = Constant.Companion.Persistence.DEFAULT, mode: Int = Context.MODE_PRIVATE): SpUtil {
            return SpUtil(context, name, mode)
        }

    }

    class SpUtil {
      private var mSp:SharedPreferences?=null
        constructor(context: Context,name: String, mode: Int){
          mSp = context.getSharedPreferences(name, mode)
      }

      fun set(key: String, value: Any):Boolean {
          val edit = mSp?.edit()
          when (value) {
              is Boolean -> edit?.putBoolean(key, value)
              is Float -> edit?.putFloat(key, value)
              is Int -> edit?.putInt(key, value)
              is Long -> edit?.putLong(key, value)
              is String -> edit?.putString(key, value)
              else -> {
              }
          }
          return edit?.commit()!!
      }

      fun get(key: String, default: Any): Any? {
          return when (default) {
              is Boolean -> mSp?.getBoolean(key, default)
              is Float -> mSp?.getFloat(key, default)
              is Int -> mSp?.getInt(key, default)
              is Long -> mSp?.getLong(key, default)
              is String -> mSp?.getString(key, default)
              else -> {
                  null
              }
          }
      }
      fun remove(key:String){
          val edit = mSp?.edit()
          edit?.remove(key)
          edit?.commit()
      }

      fun getAll():Map<String, *>{
          return mSp?.all!!
      }

      fun clear(){
            mSp?.edit()?.clear()?.commit()
      }
      fun contains(key: String):Boolean{
          return mSp?.contains(key)!!
      }
  }
}