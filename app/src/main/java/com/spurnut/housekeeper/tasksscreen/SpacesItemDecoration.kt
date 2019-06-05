package com.spurnut.housekeeper.tasksscreen

//https://stackoverflow.com/questions/28531996/android-recyclerview-gridlayoutmanager-column-spacing

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {

        val currentPosition = parent.getChildLayoutPosition(view)

        outRect.right = 0
        outRect.bottom = 0
        outRect.top = space

        // Add top margin only for the first row to avoid double space between items
        if (currentPosition == 0 || currentPosition.rem(3) == 0) {
            outRect.left = 16
        } else {
            outRect.left = 0
        }
    }
}