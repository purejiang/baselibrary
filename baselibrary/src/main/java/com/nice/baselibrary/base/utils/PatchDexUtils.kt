package com.nice.baselibrary.base.utils

import android.content.Context
import dalvik.system.DexClassLoader
import dalvik.system.PathClassLoader
import java.io.File
import java.lang.reflect.Array


/**
 *  热修复工具类，暂时只使用dex热修复
 *  参考：https://juejin.im/post/5a0ad2b551882531ba1077a2
 * @author JPlus
 * @date 2019/4/23.
 */
object PatchDexUtils {
    private var mDexList: MutableList<File> = ArrayList()
    /**
     * 获取需要热修复的dex
     * @param context
     * @param dexFile
     */
    fun loadDex(context: Context, dexFile: File) {
        dexFile.getDirFiles()?.forEach {
            if (it.name.endsWith(".dex")) { // .dex结尾
                mDexList.add(it)
                LogUtils.d("[${this.javaClass.simpleName}] --loadDex:" + it.absolutePath)
            }
        }
        mergeDex(context)
    }

    /**
     * 合并dex
     * @param context
     */
    private fun mergeDex(context: Context) {
        //应用内私有文件夹
        val dexFileInApp = File(context.writePrivateDir("patch").absolutePath)
        if (!dexFileInApp.exists()) {
            dexFileInApp.mkdirs()
        }

        mDexList.forEach {
             LogUtils.d("[${this.javaClass.simpleName}] --mergeDex:${it.absolutePath}")
            //PathClassLoader只能加载系统已安装的apk
            val pathLoader = context.classLoader as PathClassLoader
            //DexClassLoader用来加载外部的dex、apk、jar
            val dexClassLoader = DexClassLoader(it.absolutePath, dexFileInApp.absolutePath, null, pathLoader)

            //通过反射拿到基类的pathList对象
            val dexPathList = getPathList(dexClassLoader)
            val pathPathList = getPathList(pathLoader)

            //通过反射拿到基pathList对象的dexElements数组
            val newDexElements = getDexElements(dexPathList)
            val oldDexElements = getDexElements(pathPathList)

            // 合并完成
            val dexElements = mergeArrays(newDexElements, oldDexElements)

            //重新设置属性
            val pathPathList2 = getPathList(pathLoader)// 一定要重新获取，不要用pathPathList，会报错
            setFiled(pathPathList2, pathPathList2.javaClass, "dexElements", dexElements)
        }

    }

    /**
     * 通过反射设置属性
     * @param pathPathList
     * @param clazz
     * @param filed
     * @param dexElements
     */
    private fun setFiled(pathPathList: Any, clazz: Class<Any>, filed: String, dexElements: Any) {
        clazz.getDeclaredField(filed).run {
            isAccessible = true
            //返回值
            set(pathPathList, dexElements)
        }
    }

    /**
     * 合并数组
     * @param newDexElements
     * @param oldDexElements
     * @return
     */
    @Throws(Exception::class)
    private fun mergeArrays(newDexElements: Any, oldDexElements: Any): Any {
        //获取组件类型
        val componentType: Class<*>? = newDexElements.javaClass.componentType

        //新建数组
        val new = Array.getLength(newDexElements)
        val old = Array.getLength(oldDexElements)

        val result = new + old
        val componentList = Array.newInstance(componentType, result)

        //拼接数组，外部加载的数组放在系统apk数组的前面
        System.arraycopy(newDexElements, 0, componentList, 0, new)
        System.arraycopy(oldDexElements, 0, componentList, new, old)
        return componentList
    }

    /**
     * 获取Element数组dexElements
     * @param dexPathList
     */
    private fun getDexElements(dexPathList: Any): Any {
        return getFiled(dexPathList, dexPathList.javaClass, "dexElements")
    }

    /**
     * 获取DexPathList对象pathList
     * @param dexLoader
     */
    private fun getPathList(dexLoader: Any): Any {
        return getFiled(dexLoader, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList")
    }

    /**
     * 反射获取属性
     * @param any
     * @param clazz
     * @param filed
     */
    @Throws(NoSuchFieldException::class)
    private fun getFiled(any: Any, clazz: Class<*>, filed: String): Any {
        return clazz.getDeclaredField(filed).run {
            isAccessible = true
            //返回值
            get(any)
        }
    }


}

