package com.wuruoye.all2.v3.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

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
}