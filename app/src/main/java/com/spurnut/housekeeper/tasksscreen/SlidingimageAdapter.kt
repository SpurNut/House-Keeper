package com.spurnut.housekeeper.tasksscreen

import android.content.Context
import android.graphics.Bitmap
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import androidx.viewpager.widget.PagerAdapter
import com.spurnut.housekeeper.R
import com.spurnut.housekeeper.converter.ImageScaling

import java.util.ArrayList


class SlidingImageAdapter(context: Context, private val IMAGES: ArrayList<Bitmap>) : PagerAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return IMAGES.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false)!!

//        val imageView = imageLayout.findViewById(R.id.image) as SubsamplingScaleImageView
        val imageView = imageLayout.findViewById(R.id.image) as ImageView


        imageView.setImageBitmap(ImageScaling.getResizedBitmap(IMAGES[position],448))

        view.addView(imageLayout, 0)

        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }


}