package com.example.khind.adapter

import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.khind.fragment.AlertFragment
import com.example.khind.fragment.MessageFragment

class ViewPagerAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
         return when (position){
            0 -> MessageFragment()
            1 -> AlertFragment()
            else -> Fragment()
        }
    }

}