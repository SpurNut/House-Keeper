package com.spurnut.housekeeper.navigationdrawer

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.spurnut.housekeeper.database.viewmodel.HouseViewModel
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.spurnut.housekeeper.R
import com.spurnut.housekeeper.database.enity.House


class AddEditHouseDialog : DialogFragment() {

    private lateinit var dialogBuilder: AlertDialog.Builder
    private lateinit var houseViewModel: HouseViewModel
    private var house: House? = null

    companion object {
        fun newInstance(house: House?) = AddEditHouseDialog().apply {
            val frag = AddEditHouseDialog()
            if (house != null) {
                frag.setHouse(house)
            }
            return frag
        }
    }



    fun setHouse(house: House) {
        this.house = house
    }


    fun onCreateDialogView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.add_house_dialog, container) // inflate here
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // do something with 'view'
        val inputStreetName = view.findViewById<TextInputEditText>(R.id.input_street_name)
        val inputStreetNumber = view.findViewById<TextInputEditText>(R.id.input_street_number)

        if (house != null) {
            inputStreetName?.setText(house!!.streetName)
            inputStreetNumber?.setText(house!!.streetNumber)
        }

        dialogBuilder
                .setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
                .setPositiveButton(getString(R.string.ok)) { _, _ ->
                    if (house == null) {
                        houseViewModel.insert(
                                House(0, inputStreetName!!.text.toString(),
                                        inputStreetNumber!!.text.toString()))
                    } else {
                        houseViewModel.update(House(house!!.id, inputStreetName!!.text.toString(),
                                inputStreetNumber!!.text.toString()))
                    }
                }
    }

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        houseViewModel = ViewModelProviders.of(this).get(HouseViewModel::class.java)

        dialogBuilder = AlertDialog.Builder(context!!)
                .setTitle(getString(R.string.add_house))

        // call default fragment methods and set view for dialog
        val view = onCreateDialogView(activity!!.layoutInflater, null, null)
        onViewCreated(view, null)
        dialogBuilder.setView(view)

        return dialogBuilder.create()
    }
}