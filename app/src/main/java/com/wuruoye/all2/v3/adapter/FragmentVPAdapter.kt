package com.wuruoye.all2.v3.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.ViewGroup
import com.wuruoye.all2.base.util.loge

/**
 * Created by wuruoye on 2017/9/17.
 * this file is to do
 */
class FragmentVPAdapter(
        private val fm: FragmentManager,
        private val fragments: ArrayList<Fragment>,
        private val titles: ArrayList<String>
) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment =
            fragments[position]

    override fun getCount(): Int =
            fragments.size

    override fun getPageTitle(position: Int): CharSequence =
            titles[position]

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        fm.beginTransaction().show(fragment).commit()
        return fragment
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        loge("destroy fragment")
        val fragment = fragments[position]
        fm.beginTransaction().hide(fragment).commit()
    }
}