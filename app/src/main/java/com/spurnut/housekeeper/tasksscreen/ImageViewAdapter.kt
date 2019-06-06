package com.spurnut.housekeeper.tasksscreen

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.spurnut.housekeeper.R
import kotlinx.android.synthetic.main.edit_image_layout.view.*


class ImageViewAdapter(private var imageDataSet: List<Bitmap>) :
        RecyclerView.Adapter<ImageViewAdapter.ImageViewHolder>() {

    var callback: Callback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.edit_image_layout,
                parent, false)

        return ImageViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        holder.item.image_view_edit_image.setImageBitmap(imageDataSet[position])

        if (position == 0) {
            val remove_button = holder.item.findViewById<Button>(R.id.remove_image)
            remove_button.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int = imageDataSet.size


    inner class ImageViewHolder(val item: View) : RecyclerView.ViewHolder(item),
            View.OnClickListener {

        init {
            item.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (!item.remove_image.isVisible) {
                callback!!.callbackCall()

            }
        }
    }
}