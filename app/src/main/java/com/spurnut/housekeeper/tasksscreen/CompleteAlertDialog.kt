package com.spurnut.housekeeper.tasksscreen

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.spurnut.housekeeper.R
import com.spurnut.housekeeper.database.enity.Task
import com.spurnut.housekeeper.database.viewmodel.TViewModel
import com.spurnut.housekeeper.database.viewmodel.TaskOverviewViewModel


class CompleteAlertDialog(val viewModel: TViewModel, val task: Task) : DialogFragment() {

    var callback: Callback<String, Boolean>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(getString(R.string.question_complete_task))
                    .setPositiveButton(getString(R.string.complete),
                            DialogInterface.OnClickListener { dialog, id ->
                                // FIRE ZE MISSILES!
                                viewModel.update(Task(task.id, true, task.title, task.urgency, task.description, task.houseId, task.dueDate))
                                val map = HashMap<String, Boolean>()
                                map.put(key = "completed", value = true)
                                callback?.callbackCall(map)
                            })
                    .setNegativeButton(getString(R.string.cancel),
                            DialogInterface.OnClickListener { dialog, id ->
                                // User cancelled the dialog
                            })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}