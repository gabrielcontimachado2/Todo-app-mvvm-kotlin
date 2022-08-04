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
import com.bootcamp.todoeasy.util.Constants.Companion.WEEKLY
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPage()
    }

    private fun setupViewPage() {

        binding.viewPageFragment.adapter = AdapterViewPage(requireActivity())
        binding.viewPageFragment.isUserInputEnabled = false

        TabLayoutMediator(binding.tabTask, binding.viewPageFragment) { tabLayout, position ->
            tabLayout.text = when (position) {
                0 -> TODAY
                1 -> WEEKLY
                2 -> MONTH
                else -> {
                    throw  Resources.NotFoundException("Position Not Found")
                }
            }
        }.attach()

    }
}