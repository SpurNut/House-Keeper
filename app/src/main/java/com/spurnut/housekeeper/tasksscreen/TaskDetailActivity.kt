package com.spurnut.housekeeper.tasksscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spanned
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.spurnut.housekeeper.R
import kotlin.collections.ArrayList
import androidx.appcompat.widget.Toolbar


class TaskDetailActivity : AppCompatActivity() {

    private var mPager: ViewPager? = null
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
        mPager?.setAdapter(SlidingImageAdapter(this.applicationContext, ImagesArray))

        val title = findViewById<View>(R.id.detail_task_title) as TextView
        title.setText("New Set Title is longer as expected how does it look here tell me please and if it is even longer how does it look like?")

        val description = findViewById<View>(R.id.detail_task_description) as TextView
        val input = "Description:\n" + "# this is a description"


        description.setText(markdownHtmlFromText(input))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bottom_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.getItemId()) {
            R.id.action_edit -> {
                val intent = Intent(this, TaskEditActivity::class.java)
                this.startActivity(intent)
            }
            R.id.action_complete -> {
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

}
