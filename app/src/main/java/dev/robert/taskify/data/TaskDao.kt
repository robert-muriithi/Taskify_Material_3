package dev.robert.taskify.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM tasks_table")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("UPDATE tasks_table SET column_title = :title, column_desc = :description, column_date = :date, column_time = :time, column_status = :status, column_event = :event WHERE id = :id")
    suspend fun updateAnExistingRow(
        id: Int,
        title: String?,
        description: String?,
        date: String?,
        time: String?,
        status: String?,
        event: String?
    )
}