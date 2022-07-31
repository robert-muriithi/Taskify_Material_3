package dev.robert.taskify.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.robert.taskify.R
import dev.robert.taskify.adapter.TasksAdapter
import dev.robert.taskify.databinding.FragmentAllTasksBinding
import dev.robert.taskify.viewmodel.TasksViewModel


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class AllTasksFragment : Fragment() {
    private val TAG = "AllTasksFragment"
    private lateinit var binding: FragmentAllTasksBinding
    private val viewModel : TasksViewModel by viewModels()
    private val adapter : TasksAdapter by lazy { TasksAdapter(viewModel) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllTasksBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_allTasksFragment_to_addTaskFragment)
        }
        binding.recyclerView.adapter = adapter

        viewModel.getAllTasks().observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return view
    }
}
