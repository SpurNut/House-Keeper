/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.spurnut.housekeeper.tasksscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.spurnut.housekeeper.R
import com.spurnut.housekeeper.model.Task
import com.spurnut.housekeeper.model.URGENCY_IMPORTANCE_QUADRANT
import java.text.DateFormat
import java.util.*


/**
 * Shows the main title screen with a button that navigates to [About].
 */
class Task_Fragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_tasks, container, false)

        val tasks = listOf<Task>(Task(title = "# Fenster reparieren\n " +
                "* und danach leckeres\n " +
                "* Essen kochen und dann noch laufen und Einkaufen",
                urgency = URGENCY_IMPORTANCE_QUADRANT.URGENT_IMPORTANT,
                description = "Fenster hat abgeblätterte Farbe.", dueDate = Date(), images = null),
                Task(title = "Nicht so viel Text",
                urgency = URGENCY_IMPORTANCE_QUADRANT.URGENT_IMPORTANT,
                description = "Fenster hat abgeblätterte Farbe.", dueDate = null, images = null),
                Task(title = "Nicht so viel Text",
                        urgency = URGENCY_IMPORTANCE_QUADRANT.URGENT_IMPORTANT,
                        description = "Fenster hat abgeblätterte Farbe.", dueDate = Date(), images = null),
                Task(title = "Nicht so viel Text",
                        urgency = URGENCY_IMPORTANCE_QUADRANT.URGENT_IMPORTANT,
                        description = "Fenster hat abgeblätterte Farbe.", dueDate = Date(), images = null))


        val viewAdapter = MyAdapter(tasks)

        recyclerView = view.findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            setHasFixedSize(true)
            adapter = viewAdapter
        }

        // Extend the Callback class
        class _ithCallback() : ItemTouchHelper.Callback() {
            //and in your imlpementaion of
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                // get the viewHolder's and target's positions in your adapter data, swap them
                Collections.swap(viewAdapter.getItems(), viewHolder.getAdapterPosition(), target.getAdapterPosition())
                // and notify the adapter that its dataset has changed
                viewAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int): Unit {
                println("swiped")
            }

            //defines the enabled move directions in each state (idle, swiping, dragging).

            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                return makeFlag(androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG,
                        androidx.recyclerview.widget.ItemTouchHelper.DOWN or ItemTouchHelper.UP or ItemTouchHelper.START or ItemTouchHelper.END)
            }
        }

        // Create an `ItemTouchHelper` and attach it to the `RecyclerView`
        val itemTouchHelper = ItemTouchHelper(_ithCallback())
        itemTouchHelper.attachToRecyclerView(recyclerView)



        return view
    }


}
