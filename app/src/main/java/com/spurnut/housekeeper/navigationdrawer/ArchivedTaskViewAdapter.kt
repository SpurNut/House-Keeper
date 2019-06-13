package com.spurnut.housekeeper.navigationdrawer

import com.spurnut.housekeeper.tasksscreen.Callback
import com.spurnut.housekeeper.tasksscreen.markdownHtmlFromText
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.spurnut.housekeeper.database.enity.Task
import android.content.Context
import com.spurnut.housekeeper.R
import kotlinx.android.synthetic.main.archive_task_view.view.*


class ArchivedTaskViewAdapter :
        RecyclerView.Adapter<ArchivedTaskViewAdapter.TaskViewHolder>() {

    private var tasks = emptyList<Task>()
    var callback: Callback<String, Task>? = null

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    inner class TaskViewHolder(val item: View) : RecyclerView.ViewHolder(item),
            View.OnClickListener {

        override fun onClick(v: View) {

            when (v.id) {
                R.id.button_revive -> {
                    callback?.callbackCall(createCallbackData("revive", tasks[this.adapterPosition]))
                }
            }
        }

        private fun createCallbackData(key: String, value: Task): Map<String, Task> {
            val map: HashMap<String, Task> = HashMap()
            map.put(key = key, value = value)
            return map
        }

        init {
            item.button_revive.setOnClickListener(this)
        }
    }

    private var mContext: Context? = null


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): TaskViewHolder {

        mContext = parent.context
        // create a new view
        val layoutinflater = LayoutInflater.from(parent.context)
                .inflate(R.layout.archive_task_view, parent, false)

        return TaskViewHolder(layoutinflater)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        // title
        holder.item.archived_task_title.text = markdownHtmlFromText(tasks[position].title)

        //due date
        if (tasks[position].dueDate != null)
            holder.item.archived_task_due_date.text = String.format("%s %s",
                    mContext?.resources?.getString(R.string.due_on),
                    tasks[position].dueDate.toString())

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = tasks.size

    fun setTasks(tasks: List<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()

    }
}

