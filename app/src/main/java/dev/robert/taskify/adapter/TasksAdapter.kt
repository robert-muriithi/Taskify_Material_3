package dev.robert.taskify.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.robert.taskify.R
import dev.robert.taskify.data.Task
import dev.robert.taskify.databinding.TaskCardBinding
import dev.robert.taskify.ui.fragments.AddTaskFragment
import dev.robert.taskify.viewmodel.TasksViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParserFactory.newInstance
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class TasksAdapter @Inject constructor(private val viewModel: TasksViewModel) : ListAdapter<Task, TasksAdapter.TaskViewHolder>(COMPARATOR) {
    var dateFormat = SimpleDateFormat("EE dd MMM yyyy", Locale.US)
    var inputDateFormat = SimpleDateFormat("dd-M-yyyy", Locale.US)
    var outputDateString: String = ""
    private var taskList = mutableListOf<Task>()

    inner class TaskViewHolder(private val binding: TaskCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(task: Task?) {
            binding.title.text = task?.title
            binding.time.text = task?.time
            binding.description.text = task?.description
            binding.status.text = if (task!!.status) "COMPLETED" else "UPCOMING"

            try {
                val date : Date? = inputDateFormat.parse(task.date)
                outputDateString = dateFormat.format(date!!)

                val output = outputDateString.split(" ").toTypedArray()
                binding.day.text = output[0]
                binding.date.text = output[1]
                binding.month.text = output[2]

            }
            catch (e : Exception){
                e.printStackTrace()
            }

            binding.options.setOnClickListener { view ->
                showPopUpMenu(view, position)
            }
        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    fun showPopUpMenu(view: View?, position: Int) {
        taskList = currentList as MutableList<Task>
        val task: Task = taskList[position]
        val popupMenu = PopupMenu(view!!.context, view, R.style.Widget_Material_PopupMenu)
        popupMenu.menuInflater.inflate(R.menu.options_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.delete -> {
                    val dialog = AlertDialog.Builder(view.context, R.style.CustomDialogTheme)
                        .setTitle("Delete Task")
                        .setMessage("Are you sure you want to delete this task?")
                        .setPositiveButton("Yes") { _, _ ->
                            viewModel.deleteTask(task)
                        }
                        .setNegativeButton("No") { dia, _ ->
                           dia.dismiss()
                        }
                        .create()
                    dialog.setOnShowListener {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
                    }
                    dialog.show()
                }
                R.id.update -> {
                    /*val bundle = AddTaskFragment.createBundle(task)
                    val fragment = AddTaskFragment()
                    fragment.arguments = bundle
                    fragment.show((view.context as AddTaskFragment.OnTaskAddedListener).fragmentManager, "addTask")*/

                }
                R.id.completed -> {
                    val dialog = AlertDialog.Builder(view.context, R.style.CustomDialogTheme)
                        .setTitle("Complete Task")
                        .setMessage("Are you sure you want to complete this task?")
                        .setPositiveButton("Yes") { _, _ ->
                            CoroutineScope(Dispatchers.IO).launch {
                                task.status = true
                                viewModel.updateTask(task)
                                //taskList.removeAt(position)
                            }
                        }
                        .setNegativeButton("No") { dia, _ ->
                            dia.dismiss()
                        }
                        .create()
                    dialog.setOnShowListener {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
                    }
                    dialog.show()
                }
            }
            false
        }
        popupMenu.show()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksAdapter.TaskViewHolder {
        return TaskViewHolder(
            TaskCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TasksAdapter.TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object COMPARATOR : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}
