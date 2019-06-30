package com.spurnut.housekeeper.tasksscreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.spurnut.housekeeper.R
import com.spurnut.housekeeper.database.enity.Task
import com.spurnut.housekeeper.database.viewmodel.TaskOverviewViewModel
import com.spurnut.housekeeper.database.viewmodel.intLiveData

class TaskFragment : Fragment(), Callback<String, Task> {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskOverviewViewModel: TaskOverviewViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_tasks, container, false)
        val viewAdapter = TaskViewAdapter()
        viewAdapter.callback = this

        taskOverviewViewModel = ViewModelProviders.of(this).get(TaskOverviewViewModel::class.java)
        taskOverviewViewModel.allTasks.observe(this, Observer { tasks ->
            // Update the cached copy of the words in the adapter.
            tasks?.let { viewAdapter.setTasks(it) }
        })

        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_tasks_fragment).apply {
            setHasFixedSize(true)
            adapter = viewAdapter
        }

        val sharedPref = this.context!!.getSharedPreferences(
                this.context?.resources?.getString(R.string.user_config), Context.MODE_PRIVATE)

        sharedPref!!.intLiveData(this.context!!.resources!!.getString(R.string.description_text_size), 12).observe(this, Observer { size ->
            viewAdapter.setDescriptionSize(size)
        })

        return view
    }

    override fun callbackCall(data: Map<String, Task>) {

        if (data.containsKey("complete")) {

            val task = data["complete"] ?: error("empty map")
            taskOverviewViewModel
            val fragmentManager = fragmentManager!!
            val dialog = CompleteAlertDialog(taskOverviewViewModel, task)
            dialog.callback
            dialog.show(fragmentManager, null)
        }
    }


}
