package com.example.khind.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.khind.R
import com.example.khind.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class NotifyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify)

        setupTabLayout()
        setupToolbar()

    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbarNotify)
        setSupportActionBar(toolbar)

    }

    private fun setupTabLayout() {
        val viewPager2 = findViewById<ViewPager2>(R.id.viewPager)
        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        val adapter = ViewPagerAdapter(supportFragmentManager,lifecycle)
        viewPager2.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager2){ tab, position ->
            when (position){
                0 -> tab.text = "Messages"
                1 -> tab.text = "Alerts"
            }
        }.attach()
    }
}

