package com.nice.baselibrary.base.source


/**
 * 数据处理基类
 * @author JPlus
 * @date 2019/2/14.
 */
interface DataSource<T> {
    /**
     * 添加数据
     * @param data 添加的数据
     * @param callBack 添加结果的回调
     */
     fun addData(data:T, callBack:((result:Boolean)->Unit))

    /**
     * 添加数据通过list
     * @param dataList 添加的数据集合
     * @param callBack 添加结果的回调
     */
     fun addDataList(dataList:MutableList<T>, callBack:((result:Boolean)->Unit))
    /**
     * 删除数据
     * @param data 需要删除的数据
     * @param callBack 删除结果的回调
     */
     fun removeData(data:T, callBack:((result:Boolean)->Unit))
    /**
     * 删除数据通过list
     * @param dataList 需要删除的数据集合
     * @param callBack 删除结果的回调
     */
     fun removeDataList(dataList:MutableList<T>, callBack:((result:Boolean)->Unit))
    /**
     * 删除所有
     * @param callBack 删除结果的回调
     */
     fun removeAllData(callBack:((result:Boolean)->Unit))
    /**
     * 修改数据
     * @param data 修改后的数据
     * @param callBack 修改结果的回调
     */
     fun modifyData(data:T, callBack:((result:Boolean)->Unit))
    /**
     * 通过特定值查询数据
     * @param map 特定值组合成的map
     * @param callback 查询的结果
     */
     fun getData(map:MutableMap<String, out String>, callback:LoadDataListCallback<T>)
    /**
     * 通过id查询数据
     * @param id id
     * @param callback 查询的结果
     */
     fun getData(id:Int, callback:LoadDataCallback<T>)
    /**
     * 分页查询
     * @param pages
     * @param limit
     * @param callback 查询的结果
     */
     fun getDataList(pages:Int, limit:Int, callback:LoadDataListCallback<T>)
    /**
     * 查询所有数据
     * @param callback 查询的结果
     */
     fun getAllData(callback:LoadDataListCallback<T>)

    /**
     * 多个数据查询的接口
     */
    interface LoadDataListCallback<T> {
        /**
         * 查询成功
         * @param dataList 查询结果
         */
        fun onDataLoaded(dataList: MutableList<T>)
        /**
         * 查询失败
         * @param throwable 问题
         */
        fun onDataNotAvailable(throwable: Throwable)
    }
    /**
     * 单个数据查询的接口
     */
    interface LoadDataCallback<T> {
        /**
         * 查询成功的回调
         * @param data 查询的结果
         */
        fun onDataLoaded(data: T?)
        /**
         * 查询失败的回调
         * @param throwable 问题
         */
        fun onDataNotAvailable(throwable: Throwable)
    }
}