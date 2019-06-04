package com.spurnut.housekeeper.tasksscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.spurnut.housekeeper.R
import kotlin.collections.ArrayList
import androidx.appcompat.widget.Toolbar


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
        val myToolbar = findViewById<View>(R.id.bottomAppBar) as Toolbar
        setSupportActionBar(myToolbar)
        init()
    }

    private fun init() {
        for (i in 0 until IMAGES.size)
            ImagesArray.add(IMAGES[i])

        mPager = findViewById<View>(R.id.viewPager) as ViewPager
        mPager?.setAdapter(SlidingImage_Adapter(this.applicationContext, ImagesArray))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bottom_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.getItemId()) {
            R.id.action_edit -> {
            }
            R.id.action_complete -> {
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

}
