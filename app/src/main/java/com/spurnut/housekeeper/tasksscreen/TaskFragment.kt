package com.spurnut.housekeeper.tasksscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.spurnut.housekeeper.R
import com.spurnut.housekeeper.database.viewmodel.TaskViewModel

class TaskFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_tasks, container, false)
        val viewAdapter = TaskViewAdapter()

        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel::class.java)
        taskViewModel.allTasks.observe(this, Observer { tasks ->
            // Update the cached copy of the words in the adapter.
            tasks?.let { viewAdapter.setTasks(it) }
        })

        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_tasks_fragment).apply {
            setHasFixedSize(true)
            adapter = viewAdapter
        }

        return view
    }


}
