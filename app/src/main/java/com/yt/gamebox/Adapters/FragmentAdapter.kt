package com.yt.gamebox.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class FragmentAdapter(private var fm: FragmentManager, private var fragmentList: List<Fragment>) :
    FragmentPagerAdapter(fm) {


    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList.get(position)
    }
}