package dev.robert.taskify.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.taskify.data.Task
import dev.robert.taskify.repository.TasksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val repository: TasksRepository
) : ViewModel() {
    fun getAllTasks() = repository.getTasks()

     fun insertTask(task: Task) = CoroutineScope(Dispatchers.Main).launch {
        repository.insertTask(task)
    }

     fun deleteTask(task: Task) = CoroutineScope(Dispatchers.Main).launch {
        repository.deleteTask(task)
    }
}