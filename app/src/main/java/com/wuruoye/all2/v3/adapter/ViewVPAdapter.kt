package com.wuruoye.all2.v3.adapter

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

/**
 * Created by wuruoye on 2017/10/8.
 * this file is to do
 */

class ViewVPAdapter(
        private val viewList: ArrayList<View>
) : PagerAdapter() {

    override fun getCount(): Int = viewList.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean =
            view === `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = viewList[position]
        if (container.indexOfChild(view) == -1){
            container.addView(view)
        }
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

    }
}
