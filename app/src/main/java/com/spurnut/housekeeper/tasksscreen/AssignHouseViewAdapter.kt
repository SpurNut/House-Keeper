package com.spurnut.housekeeper.tasksscreen

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.spurnut.housekeeper.R
import com.spurnut.housekeeper.database.enity.House
import com.spurnut.housekeeper.database.viewmodel.HouseViewModel
import kotlinx.android.synthetic.main.assign_house_view.view.*


class AssignHouseViewAdapter(val houseViewModel: HouseViewModel) :
        RecyclerView.Adapter<AssignHouseViewAdapter.HouseViewHolder>() {

    private var houses = emptyList<House>()
    private var assignedHouse: House? = null
    var callBack: Callback<String, House>? = null

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    inner class HouseViewHolder(val item: View) : RecyclerView.ViewHolder(item),
            View.OnClickListener {

        override fun onClick(v: View) {

            when (v.id) {
                v.id -> {
                    val map = HashMap<String, House>()
                    map.put(v.context!!.getString(R.string.access_assign), houses[adapterPosition])
                    callBack!!.callbackCall(map)
                    assignedHouse = houses[adapterPosition]
                    notifyDataSetChanged()
                }
            }
        }

        init {
            item.setOnClickListener(this)
        }
    }

    private var mContext: Context? = null


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): HouseViewHolder {

        mContext = parent.context
        // create a new view
        val layoutInflater = LayoutInflater.from(parent.context)
                .inflate(R.layout.assign_house_view, parent, false)

        return HouseViewHolder(layoutInflater)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: HouseViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.item.assign_house_name.text = houses[position].streetName
        holder.item.assign_house_number.text = houses[position].streetNumber

        if (assignedHouse != null) {
            if (assignedHouse!!.id == houses[position].id) {
                holder.item.assign_house_name.setTextColor(Color.RED)
                holder.item.assign_house_number.setTextColor(Color.RED)
            } else {
                holder.item.assign_house_name.setTextColor(Color.BLACK)
                holder.item.assign_house_number.setTextColor(Color.BLACK)
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = houses.size

    fun setHouses(houses: List<House>) {
        this.houses = houses
        notifyDataSetChanged()

    }

    fun setAssignedHouse(it: House?) {
        assignedHouse = it
        notifyDataSetChanged()
    }
}
