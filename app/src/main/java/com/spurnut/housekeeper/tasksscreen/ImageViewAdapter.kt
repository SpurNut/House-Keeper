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


class ImageViewAdapter(var imageDataSet: List<Bitmap>) :
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
                item.remove_image.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (!item.remove_image.isVisible) {
                val callbackData = create_callback_data(key = "add", value = 1)
                callback!!.callbackCall(data = callbackData)
            } else {
                when (v!!.id) {
                    R.id.remove_image -> {

                        val callbackData = create_callback_data(key = "remove", value = layoutPosition)
                        callback!!.callbackCall(data = callbackData)
                    }
                }
            }
        }

        private fun create_callback_data(key: String, value: Int): Map<String, Int> {
            val map: HashMap<String, Int> = HashMap<String, Int>()
            map.put(key = key, value = value)
            return map
        }
    }
}