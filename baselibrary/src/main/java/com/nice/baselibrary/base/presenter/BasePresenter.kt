package com.nice.baselibrary.base.presenter

/**
 * @author JPlus
 * @date 2019/2/12.
 */
interface  BasePresenter {
    /**
     * 实现订阅关系
     */
    fun subscribe()

    /**
     * 移除订阅关系
     */
    fun unSubscribe()
}