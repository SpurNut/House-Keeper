package com.spurnut.housekeeper.tasksscreen

import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.spurnut.housekeeper.R
import com.spurnut.housekeeper.model.Task
import kotlinx.android.synthetic.main.my_text_view.view.*
import org.markdown4j.Markdown4jProcessor;
import android.content.Context


class MyAdapter(private var myDataset: List<Task>) :
        RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val item: View) : RecyclerView.ViewHolder(item)

    private var mContext: Context? = null


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyAdapter.MyViewHolder {

        mContext = parent.context
        // create a new view
        val textView = LayoutInflater.from(parent.context)
                .inflate(com.spurnut.housekeeper.R.layout.my_text_view, parent, false)
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(textView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        // title
        val string = Markdown4jProcessor().process(myDataset[position].title).dropLast(2)
        val html = HtmlCompat.fromHtml(
                string,
                HtmlCompat.FROM_HTML_MODE_LEGACY).dropLast(2) as Spanned

        holder.item.task_title.text = html

        //due date
        if (myDataset[position].dueDate != null)
        holder.item.task_due_date.text = String.format("%s %s",
                mContext?.resources?.getString(R.string.due_on),
                myDataset[position].dueDate.toString())
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size

    fun getItems() = myDataset
}