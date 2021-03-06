package com.spurnut.housekeeper.navigationdrawer

import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.spurnut.housekeeper.R
import com.spurnut.housekeeper.database.enity.Task
import com.spurnut.housekeeper.database.viewmodel.ArchivedTaskViewModel
import com.spurnut.housekeeper.tasksscreen.Callback


class ArchiveDialog : DialogFragment(), Callback<String, Task> {
    override fun callbackCall(data: Map<String, Task>) {
        if (data.contains(context!!.getString(R.string.access_revive))) {
            val task = data[context!!.getString(R.string.access_revive)]
            archivedTaskViewModel.update(Task(id = task!!.id, completed = false, title = task.title,
                    description = task.description, houseId = task.houseId, dueDate = task.dueDate,
                    reminderDate = task.reminderDate))
        }
    }

    private var margin: Int = 0
    private lateinit var recyclerView: RecyclerView
    private lateinit var archivedTaskViewModel: ArchivedTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        margin = 10
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.archive_dialog, container, false)
        val viewAdapter = ArchivedTaskViewAdapter()
        viewAdapter.callback = this

        archivedTaskViewModel = ViewModelProviders.of(this).get(ArchivedTaskViewModel::class.java)
        archivedTaskViewModel.allArchivedTasks.observe(this, Observer { tasks ->
            // Update the cached copy of the words in the adapter.
            tasks?.let { viewAdapter.setTasks(it) }
        })

        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_archive_dialog).apply {
            setHasFixedSize(true)
            adapter = viewAdapter
        }

        val fab: ExtendedFloatingActionButton = view.findViewById(R.id.fab_delete_archived)
        view.findViewById<ExtendedFloatingActionButton>(R.id.fab_delete_archived).isEnabled = false
        fab.setOnClickListener { view ->
            archivedTaskViewModel.deleteAllArchived()
        }

        archivedTaskViewModel.allArchivedImages.observe(this, Observer { tasks ->
            // Update the cached copy of the words in the adapter.
            tasks?.let { view.findViewById<ExtendedFloatingActionButton>(R.id.fab_delete_archived).isEnabled = true }
        })

        return view
    }

    override fun onResume() {
//        val dialogHeight = MainActivity.displayMetrics.heightPixels - margin * 2 - MainActivity.StatusBarHeight
//        val dialogWidth = MainActivity.displayMetrics.widthPixels - margin * 2
//        dialog?.window?.setLayout(dialogWidth, dialogHeight)
        super.onResume()
    }
}