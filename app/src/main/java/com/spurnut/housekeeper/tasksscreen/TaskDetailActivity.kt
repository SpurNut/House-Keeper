package com.spurnut.housekeeper.tasksscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.spurnut.housekeeper.R
import kotlin.collections.ArrayList
import androidx.core.content.ContextCompat
import android.view.WindowManager


class TaskDetailActivity : AppCompatActivity() {

    private var mPager: ViewPager? = null
    private var currentPage = 0
    private var NUM_PAGES = 0
    private val IMAGES = arrayOf<Int>(R.drawable.android, R.drawable.android,
            R.drawable.android, R.drawable.android)
    private val ImagesArray = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)
        init()
    }

    private fun init() {
        for (i in 0 until IMAGES.size)
            ImagesArray.add(IMAGES[i])

        mPager = findViewById<View>(R.id.viewPager) as ViewPager
        mPager?.setAdapter(SlidingImage_Adapter(this.applicationContext, ImagesArray))
    }

}
