package com.spurnut.housekeeper.tasksscreen

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.spurnut.housekeeper.R
import kotlin.collections.ArrayList
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.spurnut.housekeeper.database.enity.House
import com.spurnut.housekeeper.database.enity.Task
import com.spurnut.housekeeper.database.enity.TaskPhoto
import com.spurnut.housekeeper.database.viewmodel.TaskViewModel
import com.spurnut.housekeeper.database.viewmodel.TaskViewModelFactory
import org.w3c.dom.Text


class TaskDetailActivity : AppCompatActivity(), Callback<String, Boolean> {
    override fun callbackCall(data: Map<String, Boolean>) {
        if (data.getOrElse("completed") { false }) {
            onBackPressed()
        }
    }

    private var mPager: ViewPager? = null
    private lateinit var taskViewModel: TaskViewModel
    lateinit var task: LiveData<Task>
    var task_id: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)
        val myToolbar = findViewById<View>(R.id.bottomAppBar) as Toolbar
        setSupportActionBar(myToolbar)
        init()
    }

    private fun init() {


        if (getIntent().hasExtra("TASK_ID")) {

            task_id = getIntent().getIntExtra("TASK_ID", 0)
            val factory = TaskViewModelFactory(application, task_id)
            taskViewModel = ViewModelProviders.of(this, factory).get(TaskViewModel::class.java)

            val task = taskViewModel.task
            val taskPhoto = taskViewModel.images

            task.observe(this, Observer { observedTask: Task ->
                // Update the cached copy of the words in the adapter.
                setTasks(observedTask)
                if (observedTask.houseId != null) {
                    taskViewModel.getHouseById(observedTask.houseId)
                            .observe(this, Observer { house -> setHouse(house) })
                }
            })

            taskPhoto.observe(this, Observer { photos ->
                photos?.let {
                    // Update the cached copy of the words in the adapter.
                    setPhotos(photos)
                }
            })


        } else {
            taskViewModel = ViewModelProviders.of(this).get(TaskViewModel::class.java)
        }

    }

    private fun setHouse(house: House?) {
        val assignedHouse = findViewById<TextView>(R.id.detail_assigned_house) as TextView
        val address = markdownHtmlFromText("**Location:** " + house!!.streetName + " " + house.streetNumber)
        assignedHouse.text = address
    }

    private fun setTasks(it: Task) {
        val title = findViewById<View>(R.id.detail_task_title) as TextView
        val description = findViewById<View>(R.id.detail_task_description) as TextView
        val input = "**Description:**\n" + it.description

        title.setText(markdownHtmlFromText(it.title))
        description.setText(markdownHtmlFromText(input))

    }

    private fun setPhotos(it: List<TaskPhoto>) {
        val images = ArrayList<Bitmap>()
        for (i in 0 until it.size)
            images.add(BitmapFactory.decodeFile(it[i].uid))

        mPager = findViewById<View>(R.id.viewPager) as ViewPager
        mPager?.setAdapter(SlidingImageAdapter(this.applicationContext, images))

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
                intent.putExtra(getString(R.string.edit_existing_task), task_id)
                this.startActivity(intent)
            }
            R.id.action_complete -> {
                val fragmentManager = supportFragmentManager
                val dialog = CompleteAlertDialog(taskViewModel, taskViewModel.task.value!!)
                dialog.callback = this
                dialog.show(fragmentManager, null)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

}
