package com.spurnut.housekeeper.tasksscreen

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.spurnut.housekeeper.R

class CreateTaskDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.create_task)
                    .setItems(R.array.create_options,
                            { dialog, which ->
                                // The 'which' argument contains the index position
                                // of the selected item
                                if(which == 1) {
                                    val intent = Intent(this.context, TaskEditActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    intent.putExtra("START_CAMERA", true)
                                    startActivity(intent)
                                }
                            })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
