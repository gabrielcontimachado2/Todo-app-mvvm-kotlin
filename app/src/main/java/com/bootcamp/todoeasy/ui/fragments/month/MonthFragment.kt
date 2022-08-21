package com.bootcamp.todoeasy.ui.fragments.month

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bootcamp.todoeasy.R
import com.bootcamp.todoeasy.databinding.FragmentMonthBinding
import com.bootcamp.todoeasy.ui.adapter.TaskAdapter
import com.bootcamp.todoeasy.ui.fragments.today.TaskViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MonthFragment : Fragment() {

    private lateinit var binding: FragmentMonthBinding
    private val taskSharedViewModel: TaskViewModel by activityViewModels()
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var recyclerViewTask: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMonthBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observeTaskFlow()
        itemTouchHelper()
        setupCardClicked()
        setupCheckedTask()

        return binding.root
    }

    /** Function for Navigate for the Detail Task Activity and Pass the Task in the Args */
    private fun setupCardClicked() {
        taskAdapter.setonCardClickListener { task ->
            val bundle = Bundle().apply {
                putParcelable("task", task)
            }

            findNavController().navigate(
                R.id.detailTask,
                bundle
            )
        }
    }

    /** Function for Complete or Undone the Task */
    private fun setupCheckedTask() {
        taskAdapter.setonCheckClickListener { taskClicked ->

            /** The floating button in main activity for anchor the snackBar above the fab in bottom bar*/
            val floatingMain: FloatingActionButton =
                activity?.findViewById(R.id.floatingButton_create_task)!!


            if (!taskClicked.status) {

                taskSharedViewModel.updateTaskByChecked(taskClicked.idTask, true)

                Snackbar.make(requireView(), R.string.task_month_completed, Snackbar.LENGTH_LONG)
                    .setAnchorView(floatingMain).show()
            } else {

                taskSharedViewModel.updateTaskByChecked(taskClicked.idTask, false)

                Snackbar.make(requireView(), R.string.task_undo_completed, Snackbar.LENGTH_LONG)
                    .setAnchorView(floatingMain).show()
            }

        }
    }

    /** Create the recyclerView with adapter*/
    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter()
        recyclerViewTask = binding.recyclerViewMonthTask
        recyclerViewTask.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = taskAdapter
        }

    }

    /** Function for Observer the Ui State in ViewModel and set the list in TaskAdapter and if have errors a Toast with the Message */
    private fun observeTaskFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                taskSharedViewModel.uiStateMonth.collect {
                    taskAdapter.submitList(it.taskMonth)

                    if (it.message.isNotEmpty()) {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    /** Item touch helper for swipe card*/
    private fun itemTouchHelper() {
        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(recyclerViewTask)
        }
    }

    /** Function for swipe card effect in recyclerView, delete the task and undo that action with necessary*/
    private val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val task = taskAdapter.currentList[position]

            /** Delete the task in room db */
            taskSharedViewModel.deleteTask(task)

            /** The floating button in main activity for anchor the snackBar above the fab in bottom bar*/
            val floatingMain: FloatingActionButton =
                activity?.findViewById(R.id.floatingButton_create_task)!!

            /** SnackBar with action for undo the delete task*/
            Snackbar.make(view!!, R.string.task_deleted_success, Snackbar.LENGTH_LONG)
                .apply {
                    setAction("Undo") {
                        taskSharedViewModel.insertTask(task)
                        Snackbar.make(
                            view,
                            R.string.create_task_completed,
                            Toast.LENGTH_LONG
                        ).setAnchorView(floatingMain)
                            .show()
                    }.anchorView = floatingMain
                    show()
                }

        }

    }
}