package com.spurnut.housekeeper.tasksscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.spurnut.housekeeper.R
import kotlinx.android.synthetic.main.edit_image_layout.view.*

class ImageViewAdapter(private var imageDataSet: List<Int>) :
        RecyclerView.Adapter<ImageViewAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.edit_image_layout,
                parent, false)

        return ImageViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        holder.item.image_view_edit_image.setImageResource(imageDataSet[position])

    }

    override fun getItemCount() : Int = imageDataSet.size


    class ImageViewHolder(val item: View) : RecyclerView.ViewHolder(item),
            View.OnClickListener {
        override fun onClick(v: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}