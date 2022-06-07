package dev.robert.taskify.repository

import dev.robert.taskify.data.Task
import dev.robert.taskify.data.TaskDatabase
import javax.inject.Inject

class TasksRepository @Inject constructor(val database: TaskDatabase) {
    fun getTasks() = database.taskDao().getAllTasks()
    suspend fun insertTask(task: Task) = database.taskDao().insert(task)
    suspend fun deleteTask(task: Task) = database.taskDao().delete(task)
}