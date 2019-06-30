package com.spurnut.housekeeper

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.Observer
import com.spurnut.housekeeper.database.viewmodel.intLiveData

class SettingsActivity : AppCompatActivity() {

    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        sharedPref = getSharedPreferences(
                getString(R.string.user_config), Context.MODE_PRIVATE)

        sharedPref.intLiveData(getString(R.string.description_text_size), 12).observe(this, Observer { size ->
            val radioGroup = findViewById<RadioGroup>(R.id.radio_group_description_text_size)
            if (size > 12) {
                radioGroup.check(R.id.radio_description_text_normal)
            } else {
                radioGroup.check(R.id.radio_description_text_small)
            }
        })

    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_description_text_small ->
                    if (checked) {
                        writeToDefaultSharedPreferences(12)
                    }
                R.id.radio_description_text_normal ->
                    if (checked) {
                        writeToDefaultSharedPreferences(16)
                    }
            }
        }
    }

    fun writeToDefaultSharedPreferences(size: Int) {
        with(sharedPref.edit()) {
            putInt(getString(R.string.description_text_size), size)
            apply()
        }
    }
}
