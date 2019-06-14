package com.spurnut.housekeeper.tasksscreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.spurnut.housekeeper.R
import kotlinx.android.synthetic.main.edit_image_layout.view.*


class ImageViewAdapter(var imageDataSet: List<Bitmap>) :
        RecyclerView.Adapter<ImageViewAdapter.ImageViewHolder>() {

    var callback: Callback<String, Int>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.edit_image_layout,
                parent, false)

        return ImageViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        val image = BitmapDrawable(holder.item.context!!.resources, imageDataSet[position])
        holder.item.background = image

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
                val callbackData = createCallbackData(key = "add", value = 1)
                callback!!.callbackCall(data = callbackData)
            } else {
                when (v!!.id) {
                    R.id.remove_image -> {

                        val callbackData = createCallbackData(key = "remove", value = layoutPosition)
                        callback!!.callbackCall(data = callbackData)
                    }
                }
            }
        }

        private fun createCallbackData(key: String, value: Int): Map<String, Int> {
            val map: HashMap<String, Int> = HashMap()
            map.put(key = key, value = value)
            return map
        }
    }
}