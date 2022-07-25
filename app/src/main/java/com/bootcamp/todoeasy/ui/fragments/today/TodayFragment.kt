package com.bootcamp.todoeasy.ui.fragments.today


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bootcamp.todoeasy.databinding.FragmentTodayBinding
import com.bootcamp.todoeasy.ui.adapter.TaskAdapter
import com.bootcamp.todoeasy.util.Constants.Companion.TODAY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch


@AndroidEntryPoint
class TodayFragment : Fragment() {

    private lateinit var binding: FragmentTodayBinding
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var recyclerViewTask: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodayBinding.inflate(inflater, container, false)

        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

    }

    /** Flow*/
    private fun observeTask(adapter: TaskAdapter) {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.taskDay.value = TODAY

                viewModel.taskFlow.collect { taskList ->
                    adapter.submitList(taskList)
                }
            }
        }

    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter()
        recyclerViewTask = binding.recyclerViewTodayTask
        recyclerViewTask.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = taskAdapter
        }
        observeTask(taskAdapter)
    }

    /** Flow*/
    //private fun observeTask() {
    //    viewLifecycleOwner.lifecycleScope.launch {
    //        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

    //            viewModel.taskDay.emit(TODAY)

    //            viewModel.taskFlow.collect { taskList ->
    //                taskAdapter.submitList(taskList)
    //            }
    //        }
    //    }
    //}

}


