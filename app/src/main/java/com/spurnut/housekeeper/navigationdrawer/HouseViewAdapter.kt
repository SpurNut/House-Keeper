package com.spurnut.housekeeper.navigationdrawer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.spurnut.housekeeper.R
import com.spurnut.housekeeper.database.enity.House
import com.spurnut.housekeeper.database.viewmodel.HouseViewModel
import kotlinx.android.synthetic.main.house_view.view.*

class HouseViewAdapter(val houseViewModel: HouseViewModel) :
        RecyclerView.Adapter<HouseViewAdapter.HouseViewHolder>() {

    private var houses = emptyList<House>()

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    inner class HouseViewHolder(val item: View) : RecyclerView.ViewHolder(item),
            View.OnClickListener {

        override fun onClick(v: View) {

            when (v.id) {
                R.id.button_edit -> {
                    //Todo open edit house dialog
                }
                R.id.button_delete -> {
                    houseViewModel.delete(houses[this.adapterPosition])
                }
            }
        }

        init {
            item.button_edit.setOnClickListener(this)
            item.button_delete.setOnClickListener(this)
        }
    }

    private var mContext: Context? = null


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): HouseViewHolder {

        mContext = parent.context
        // create a new view
        val layoutinflater = LayoutInflater.from(parent.context)
                .inflate(R.layout.house_view, parent, false)

        return HouseViewHolder(layoutinflater)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: HouseViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.item.house_name.text = houses[position].streetName
        holder.item.house_number.text = houses[position].streetNumber
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = houses.size

    fun setHouses(houses: List<House>) {
        this.houses = houses
        notifyDataSetChanged()

    }
}

