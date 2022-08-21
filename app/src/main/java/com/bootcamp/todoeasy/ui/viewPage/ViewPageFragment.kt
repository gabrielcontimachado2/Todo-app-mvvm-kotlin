package com.bootcamp.todoeasy.ui.viewPage

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bootcamp.todoeasy.databinding.FragmentViewPageBinding
import com.bootcamp.todoeasy.ui.adapter.AdapterViewPage
import com.bootcamp.todoeasy.util.Constants.Companion.MONTH
import com.bootcamp.todoeasy.util.Constants.Companion.TODAY
import com.bootcamp.todoeasy.util.Constants.Companion.WEEK
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ViewPageFragment : Fragment() {

    private lateinit var binding: FragmentViewPageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentViewPageBinding.inflate(inflater, container, false)

        setupViewPage()

        return binding.root
    }


    /** Create the view Pager for tasks fragments*/
    private fun setupViewPage() {
        binding.viewPageFragment.adapter = AdapterViewPage(requireActivity())

        TabLayoutMediator(binding.tabTask, binding.viewPageFragment) { tabLayout, position ->
            tabLayout.text = when (position) {
                0 -> TODAY
                1 -> WEEK
                2 -> MONTH
                else -> {
                    throw  Resources.NotFoundException("Position Not Found")
                }
            }
        }.attach()
    }
}