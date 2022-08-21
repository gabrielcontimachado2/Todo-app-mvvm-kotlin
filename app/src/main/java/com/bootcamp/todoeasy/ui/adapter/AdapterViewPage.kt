package com.bootcamp.todoeasy.ui.adapter

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bootcamp.todoeasy.ui.fragments.month.MonthFragment
import com.bootcamp.todoeasy.ui.fragments.today.TodayFragment
import com.bootcamp.todoeasy.ui.fragments.weekly.WeekFragment


class AdapterViewPage(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                TodayFragment()
            }
            1 -> {
                WeekFragment()
            }
            2 -> {
                MonthFragment()
            }

            else -> { throw Resources.NotFoundException("Position Not Found")}
        }
    }


}