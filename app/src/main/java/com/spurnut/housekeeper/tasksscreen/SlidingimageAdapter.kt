package com.spurnut.housekeeper.tasksscreen

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import androidx.viewpager.widget.PagerAdapter
import com.spurnut.housekeeper.R

import java.util.ArrayList


class SlidingImageAdapter(context: Context, private val IMAGES: ArrayList<Int>) : PagerAdapter() {
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


//        imageView.setImage(ImageSource.resource(IMAGES[position]))
//        imageView.setImage(ImageSource.resource(R.mipmap.ic_launcher))
        imageView.setImageResource(IMAGES[position])

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