package dev.robert.taskify.ui.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.robert.taskify.data.Task
import dev.robert.taskify.databinding.FragmentAddTaskBinding
import dev.robert.taskify.viewmodel.TasksViewModel
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddTaskFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddTaskBinding
    private val viewModel: TasksViewModel by viewModels()
    private val status = "UPCOMING"
    private var calender = Calendar.getInstance()
    var mYear = 0
    var mMonth :Int = 0
    var mDay: Int = 0
    var datePickerDialog: DatePickerDialog? = null
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.taskTime.setOnClickListener {
            val dialog = TimePickerDialog(
                requireContext(),
                { _, hour, minute ->
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
            calender.set(Calendar.YEAR, mYear)
            calender.set(Calendar.MONTH, mMonth)
            calender.set(Calendar.DAY_OF_MONTH, mDay)
            datePickerDialog = DatePickerDialog(
                activity!!,
                { _: DatePicker?, year , monthOfYear , dayOfMonth ->
                    binding.taskDate.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    datePickerDialog?.dismiss()
                },
                mYear,
                mMonth,
                mDay
            )
            datePickerDialog!!.datePicker.minDate = System.currentTimeMillis() - 1000
            datePickerDialog!!.show()
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

