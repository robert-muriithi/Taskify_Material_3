package dev.robert.taskify.ui.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.format.Time
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.robert.taskify.data.Task
import dev.robert.taskify.databinding.FragmentAddTaskBinding
import dev.robert.taskify.viewmodel.TasksViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddTaskFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddTaskBinding
    private val viewModel: TasksViewModel by viewModels()
    private val status = "UPCOMING"
    private val calender = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.taskTime.setOnClickListener {
            val dialog = TimePickerDialog(
                requireContext(),
                TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                    calender.set(Calendar.HOUR_OF_DAY, hour)
                    calender.set(Calendar.MINUTE, minute)
                    val myFormat = "HH:mm"
                    val sdf = SimpleDateFormat(myFormat, Locale.US)

                    binding.taskTime.text = sdf.format(calender.time).toEditable()
                },
                calender.get(Calendar.HOUR_OF_DAY),
                calender.get(Calendar.MINUTE),
                false
            )
            dialog.show()
        }

        binding.taskDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    calender.set(Calendar.YEAR, year)
                    calender.set(Calendar.MONTH, month)
                    calender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val myFormat = "MM/dd/yyyy"
                    val sdf = SimpleDateFormat(myFormat, Locale.US)
                    binding.taskDate.text = sdf.format(calender.time).toEditable()
                },
                calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH)

            )
            datePickerDialog.show()
            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        }

        binding.addTask.setOnClickListener {
            val title = binding.addTaskTitle.text.toString()
            val desc = binding.addTaskDescription.text.toString()
            val date = binding.taskDate.text.toString()
            val time = binding.taskTime.text.toString()
            val event = binding.taskEvent.text.toString()

            when {
                title.isEmpty() -> binding.addTaskTitle.error = "Title is required"
                desc.isEmpty() -> binding.addTaskDescription.error = "Description is required"
                date.isEmpty() -> binding.taskDate.error = "Date is required"
                time.isEmpty() -> binding.taskTime.error = "Time is required"
                event.isEmpty() -> binding.taskEvent.error = "Event is required"
                else -> {
                    val task = Task(
                        title = title,
                        description = desc,
                        date = date,
                        time = time,
                        event = event,
                        status = status.toBoolean()
                    )
                    try {
                        viewModel.insertTask(task)
                        dismiss()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        return view
    }


    private fun String.toEditable(): Editable? {
        return Editable.Factory.getInstance().newEditable(this)
    }
}

