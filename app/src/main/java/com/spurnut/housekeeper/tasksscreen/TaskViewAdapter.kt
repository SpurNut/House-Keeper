package com.spurnut.housekeeper.tasksscreen

import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.spurnut.housekeeper.R
import com.spurnut.housekeeper.model.Task
import kotlinx.android.synthetic.main.task_view.view.*
import org.markdown4j.Markdown4jProcessor;
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewTreeObserver
import android.widget.Button
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log


class TaskViewAdapter(private var taskDataset: List<Task>) :
        RecyclerView.Adapter<TaskViewAdapter.TaskViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class TaskViewHolder(val item: View) : RecyclerView.ViewHolder(item), TextWatcher,
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
                    val intent = Intent(v.context, TaskDetailActivity::class.java)
                    v.context.startActivity(intent)
                }
            }
        }

        init {
            item.showMoreToggle.setOnClickListener(this)
            item.button_open.setOnClickListener(this)
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
                                    viewType: Int): TaskViewAdapter.TaskViewHolder {

        mContext = parent.context
        // create a new view
        //TODO rename textview into something correct
        val textView = LayoutInflater.from(parent.context)
                .inflate(com.spurnut.housekeeper.R.layout.task_view, parent, false)

        return TaskViewHolder(textView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        // title
        holder.item.task_title.text = markdownHtmlFromText(taskDataset[position].title).dropLast(2)

        //due date
        if (taskDataset[position].dueDate != null)
            holder.item.task_due_date.text = String.format("%s %s",
                    mContext?.resources?.getString(R.string.due_on),
                    taskDataset[position].dueDate.toString())

        //description
        if (taskDataset[position].description != null) {
            holder.item.task_description.text = markdownHtmlFromText("Description:\n" + taskDataset[position].description!!).dropLast(2)
        }



        holder.item.task_description.viewTreeObserver.addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        holder.item.task_description.viewTreeObserver.removeOnPreDrawListener(this)
                        holder.item.task_description.layout.getLineCount()
                        val linecount = holder.item.task_description.lineCount
                        if (holder.item.task_description.lineCount > 2) {
                            holder.item.task_description.setLines(2)
                            holder.item.showMoreToggle.visibility = View.VISIBLE
                        }
                        Log.d(TAG, "Number of lines is $linecount")
                        return true
                    }
                })
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = taskDataset.size

    fun getItems() = taskDataset
}
