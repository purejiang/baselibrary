package com.nice.baselibrary.base.ui.view.dialog

import android.view.View
import com.nice.baselibrary.base.adapter.NiceAdapter


/**
 * @author JPlus
 * @date 2019/4/17.
 */
class DialogAdapter(private val views:ArrayList<View>): NiceAdapter<View>(views) {



    override fun getLayout(viewType: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun convert(holder: VH, item: View, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addItem(item: View) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addItems(items: ArrayList<View>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteItem(item: View) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteItem(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteItems(items: ArrayList<View>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(position: Int): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun refreshItems(items: ArrayList<View>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setItemClickListener(itemClickListener: ItemClickListener<View>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}