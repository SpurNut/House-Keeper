package com.spurnut.housekeeper.navigationdrawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.spurnut.housekeeper.R
import com.spurnut.housekeeper.database.enity.House
import com.spurnut.housekeeper.database.viewmodel.HouseViewModel
import com.spurnut.housekeeper.tasksscreen.Callback

class HouseDialog : DialogFragment(), Callback<String, House> {
    override fun callbackCall(data: Map<String, House>) {
        if (data.contains("edit")) {
            val dialogFragment = AddEditHouseDialog.newInstance(data["edit"])
            if (fragmentManager != null) {
                dialogFragment.show(fragmentManager!!, "Dialog");
            }
        }
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var houseViewModel: HouseViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.house_dialog, container, false)

        houseViewModel = ViewModelProviders.of(this).get(HouseViewModel::class.java)

        val viewAdapter = HouseViewAdapter(houseViewModel)
        viewAdapter.callBack = this
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_houses_dialog).apply {
            setHasFixedSize(true)
            adapter = viewAdapter
        }

        houseViewModel.allHouses.observe(this, Observer { houses ->
            // Update the cached copy of the words in the adapter.
            houses?.let { viewAdapter.setHouses(it) }
        })
        return view
    }

    override fun onResume() {
//        val dialogHeight = MainActivity.displayMetrics.heightPixels - margin * 2 - MainActivity.StatusBarHeight
//        val dialogWidth = MainActivity.displayMetrics.widthPixels - margin * 2
//        dialog?.window?.setLayout(dialogWidth, dialogHeight)
        super.onResume()
    }
}