package com.spurnut.housekeeper.tasksscreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.spurnut.housekeeper.R
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


class TaskEditActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private val IMAGES = listOf<Int>(R.drawable.ic_remove_black_24dp, R.drawable.android,
            R.drawable.android, R.drawable.android)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_edit)

        // set up the RecyclerView
        val numberOfColumns = 3
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view_edit_image).apply {
            layoutManager = staggeredGridLayoutManager
            adapter = ImageViewAdapter(IMAGES)
        }

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
        recyclerView.addItemDecoration(SpacesItemDecoration(spacingInPixels))

    }

}