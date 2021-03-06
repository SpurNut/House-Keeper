package com.spurnut.housekeeper.tasksscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.spurnut.housekeeper.R
import com.spurnut.housekeeper.database.enity.Task
import kotlinx.android.synthetic.main.task_view.view.*
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewTreeObserver
import android.widget.Button
import android.content.ContentValues.TAG
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import androidx.core.text.toSpanned
import com.spurnut.housekeeper.database.enity.House
import com.spurnut.housekeeper.markdown.markdownHtmlFromText


class TaskViewAdapter :
        RecyclerView.Adapter<TaskViewAdapter.TaskViewHolder>() {

    private var tasks = emptyList<Task>()
    private var houses = emptyList<House>()
    var callback: Callback<String, Task>? = null
    private var descriptionTextSize: Int = 12

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    inner class TaskViewHolder(val item: View) : RecyclerView.ViewHolder(item), TextWatcher,
            View.OnClickListener {

        override fun onClick(v: View) {

            when (v.id) {
                R.id.showMoreToggle -> {
                    if (item.showMoreToggle.isChecked) {
                        item.task_description.setLines(item.task_description.lineCount)
                    } else {
                        item.task_description.setLines(2)
                    }
                }
                R.id.button_open -> {
                    val taskId = tasks[this.adapterPosition].id
                    val intent = Intent(v.context, TaskDetailActivity::class.java)

                    intent.putExtra(mContext!!.getString(R.string.task_id_detail_activity), taskId)
                    v.context.startActivity(intent)
                }
                R.id.button_complete -> {
                    callback!!.callbackCall(createCallbackData(
                            mContext!!.getString(R.string.complete), tasks[this.adapterPosition]))
                }
            }
        }

        private fun createCallbackData(key: String, value: Task): Map<String, Task> {
            val map: HashMap<String, Task> = HashMap()
            map[key] = value
            return map
        }

        init {
            item.showMoreToggle.setOnClickListener(this)
            item.button_open.setOnClickListener(this)
            item.button_complete.setOnClickListener(this)
            val count = item.task_description.lineCount
            println(count)

        }


        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }


        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun afterTextChanged(s: Editable?) {
            if (item.task_description.lineCount > 2) {
                val showMoreToggle = item.findViewById(R.id.showMoreToggle) as Button
                showMoreToggle.visibility = View.VISIBLE

            }
        }
    }

    private var mContext: Context? = null


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): TaskViewHolder {

        mContext = parent.context
        // create a new view
        val layoutinflater = LayoutInflater.from(parent.context)
                .inflate(R.layout.task_view, parent, false)

        return TaskViewHolder(layoutinflater)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        // title
        holder.item.task_title.text = tasks[position].title

        //assigned house
        if (tasks[position].houseId != null) {
            for (house: House in this.houses) {
                if (house.id == tasks[position].houseId) {
                    holder.item.task_assigned_house.text = TextUtils.concat(
                            markdownHtmlFromText(mContext!!.getString(R.string.location)),
                            "${house.streetName} ${house.streetNumber}".toSpanned())
                }

            }
        } else {
            val address = TextUtils.concat(markdownHtmlFromText(
                    mContext!!.getString(R.string.location)),
                    mContext!!.getString(R.string.no_location_assigned).toSpanned())
            holder.item.task_assigned_house.text = address
        }

        //reminder date
        if (tasks[position].reminderDate != null) {
            holder.item.task_reminder.text = tasks[position].reminderDate.toString()
            holder.item.task_reminder_icon.visibility = View.VISIBLE
        } else {
            holder.item.task_reminder.text = ""
            holder.item.task_reminder_icon.visibility = View.GONE
        }

        //description
        if (tasks[position].description != null) {
            holder.item.task_description.text = TextUtils.concat(
                    markdownHtmlFromText("**Description:**\n"),
                    tasks[position].description!!.toSpanned())
            holder.item.task_description.textSize = descriptionTextSize.toFloat()
        }



        holder.item.task_description.viewTreeObserver.addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        holder.item.task_description.viewTreeObserver.removeOnPreDrawListener(this)
                        val linecount = holder.item.task_description.lineCount
                        if (holder.item.task_description.lineCount > 2) {
                            holder.item.task_description.setLines(2)
                            holder.item.showMoreToggle.visibility = View.VISIBLE
                        }
                        return true
                    }
                })
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = tasks.size

    fun setTasks(tasks: List<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()

    }

    fun setDescriptionSize(size: Int) {
        descriptionTextSize = size
        notifyDataSetChanged()
    }

    fun addHouses(houses: List<House>) {
        this.houses = houses
        notifyDataSetChanged()
    }
}
