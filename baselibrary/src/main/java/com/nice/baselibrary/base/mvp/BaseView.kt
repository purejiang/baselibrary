package com.nice.baselibrary.base.mvp

/**
 * @author JPlus
 * @date 2019/2/12.
 */
interface BaseView<in T> {
    /**
     * 设置presenter
     * @param presenter
     */
    fun setPresenter(presenter:T)
}