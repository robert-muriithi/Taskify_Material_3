package dev.robert.taskify.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "tasks_table")
data class Task (
    @ColumnInfo(name = "column_title")
    var title: String,
    @ColumnInfo(name = "column_desc")
    var description: String,
    @ColumnInfo(name = "column_date")
    var date: String,
    @ColumnInfo(name = "column_time")
    var time: String,
    @ColumnInfo(name = "column_event")
    var event: String,
    @ColumnInfo(name = "column_status")
    var status: Boolean
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}