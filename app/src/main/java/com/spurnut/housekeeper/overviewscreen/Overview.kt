package com.spurnut.housekeeper.overviewscreen

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.spurnut.housekeeper.R
import com.spurnut.housekeeper.converter.ImageScaling

class Overview : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_overview, container, false)
        val image = BitmapFactory.decodeResource(view.context.resources, R.drawable.under_construction)
        val construction_sign = view.findViewById<ImageView>(R.id.image_construction_sign)
        construction_sign.setImageBitmap(ImageScaling.getResizedBitmap(image,448))
        return view
    }
}
