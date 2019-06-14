package com.spurnut.housekeeper.tasksscreen

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.spurnut.housekeeper.R
import com.spurnut.housekeeper.database.enity.Task
import com.spurnut.housekeeper.database.enity.UrgencyImportantQuadrant
import com.spurnut.housekeeper.database.viewmodel.TaskOverviewViewModel
import java.util.*

class CreateTaskDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.create_task)
                    .setItems(R.array.create_options
                    ) { dialog, which ->
                        // The 'which' argument contains the index position
                        // of the selected item
                        val intent = Intent(this.context, TaskEditActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        val taskOverviewViewModel = ViewModelProviders.of(this)
                                .get(TaskOverviewViewModel::class.java)
                        val task = Task(0, false, "",
                                UrgencyImportantQuadrant.URGENT_IMPORTANT, "", null, Date(22005515))
                        val task_id = taskOverviewViewModel.insert(task)
                        if (which == 1) {
                            intent.putExtra("START_CAMERA", task_id)
                        } else {
                            intent.putExtra("TASK_FROM_TEXT", task_id)
                        }
                        startActivity(intent)
                    }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
