package com.spurnut.housekeeper.tasksscreen


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.spurnut.housekeeper.R
import com.spurnut.housekeeper.database.enity.House
import com.spurnut.housekeeper.database.enity.Task
import com.spurnut.housekeeper.database.viewmodel.HouseViewModel
import com.spurnut.housekeeper.database.viewmodel.TaskViewModel

class AssignHouseDialog(val task_live: LiveData<Task>, val taskViewModel: TaskViewModel) :
        DialogFragment(), Callback<String, House> {
    lateinit var task: Task

    override fun callbackCall(data: Map<String, House>) {
        if (data.contains(context!!.getString(R.string.access_assign))) {
            taskViewModel.update(Task(task.id, task.completed, task.title, task.description,
                    houseId = data[context!!.getString(R.string.access_assign)]!!.id,
                    dueDate = task.dueDate, reminderDate = task.reminderDate))
            Toast.makeText(context, getString(R.string.assigned_house)
                    + data[context!!.getString(R.string.access_assign)]?.streetName + " "
                    + data[context!!.getString(R.string.access_assign)]?.streetNumber,
                    Toast.LENGTH_SHORT).show()
            this.dismiss()
        }
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var houseViewModel: HouseViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.assign_house_dialog, container, false)


        houseViewModel = ViewModelProviders.of(this).get(HouseViewModel::class.java)

        val viewAdapter = AssignHouseViewAdapter(houseViewModel)
        viewAdapter.callBack = this
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_assign_house_dialog).apply {
            setHasFixedSize(true)
            adapter = viewAdapter
        }

        task_live.observe(this, Observer { taski ->
            run {
                task = taski
                if (taski.houseId != null) {
                    taskViewModel.getHouseById(taski.houseId).observe(this, Observer { house ->
                        viewAdapter.setAssignedHouse(house)
                    })
                }
            }
        })

        houseViewModel.allHouses.observe(this, Observer { houses ->
            // Update the cached copy of the words in the adapter.
            houses?.let { viewAdapter.setHouses(it) }
        })

        return view
    }
}