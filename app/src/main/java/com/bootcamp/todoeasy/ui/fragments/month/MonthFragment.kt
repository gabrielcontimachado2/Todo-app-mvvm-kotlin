package com.bootcamp.todoeasy.ui.fragments.month

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bootcamp.todoeasy.databinding.FragmentMonthBinding

class MonthFragment : Fragment() {

    private lateinit var binding: FragmentMonthBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMonthBinding.inflate(inflater, container, false)

        return binding.root
    }
}