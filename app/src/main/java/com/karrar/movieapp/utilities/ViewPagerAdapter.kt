package com.karrar.movieapp.utilities

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

data class TabItem(
    val title: String,
    val fragment: Fragment
)

class ViewPagerAdapter (
    fragmentActivity: FragmentActivity,
    private val tabs: List<TabItem>
): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = tabs.size

    override fun createFragment(position: Int): Fragment = tabs[position].fragment
}