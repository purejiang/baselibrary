package com.nice.baselibrary.base.ui

/**
 * @author JPlus
 * @date 2019/2/12.
 */
interface NiceBaseView<in T> {
    /**
     * 设置presenter
     * @param presenter
     */
    fun setPresenter(presenter:T)
}