package com.spurnut.housekeeper

import android.app.Activity
import android.app.Dialog
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.DatePicker
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TimePicker
import android.widget.ViewSwitcher

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

/**
 * based on https://stackoverflow.com/questions/38604157/android-date-time-picker-in-one-dialog
 * Created by admin on 6/29/2016.
 */
class CustomDateTimePicker(private val activity: Activity,
                           customDateTimeListener: ICustomDateTimeListener) : View.OnClickListener {
    private var datePicker: DatePicker? = null
    private var timePicker: TimePicker? = null
    private var viewSwitcher: ViewSwitcher? = null

    private val SET_DATE = 100
    private val SET_TIME = 101
    private val SET = 102
    private val CANCEL = 103

    private var btn_setDate: Button? = null
    private var btn_setTime: Button? = null
    private var btn_set: Button? = null
    private var btn_cancel: Button? = null

    private var calendar_date: Calendar? = null

    private var iCustomDateTimeListener: ICustomDateTimeListener? = null

    private val dialog: Dialog

    private var is24HourView = true
    private var isAutoDismiss = true

    private var selectedHour: Int = 0
    private var selectedMinute: Int = 0

    val dateTimePickerLayout: View
        get() {
            val linear_match_wrap = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
            val linear_wrap_wrap = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
            val frame_match_wrap = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT)

            val button_params = LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)

            val linear_main = LinearLayout(activity)
            linear_main.layoutParams = linear_match_wrap
            linear_main.orientation = LinearLayout.VERTICAL
            linear_main.gravity = Gravity.CENTER

            val linear_child = LinearLayout(activity)
            linear_child.layoutParams = linear_wrap_wrap
            linear_child.orientation = LinearLayout.VERTICAL

            val linear_top = LinearLayout(activity)
            linear_top.layoutParams = linear_match_wrap

            btn_setDate = Button(activity)
            btn_setDate!!.layoutParams = button_params
            btn_setDate!!.text = "Set Date"
            btn_setDate!!.id = SET_DATE
            btn_setDate!!.setOnClickListener(this)

            btn_setTime = Button(activity)
            btn_setTime!!.layoutParams = button_params
            btn_setTime!!.text = "Set Time"
            btn_setTime!!.id = SET_TIME
            btn_setTime!!.setOnClickListener(this)

            linear_top.addView(btn_setDate)
            linear_top.addView(btn_setTime)

            viewSwitcher = ViewSwitcher(activity)
            viewSwitcher!!.layoutParams = frame_match_wrap

            datePicker = DatePicker(activity)
            timePicker = TimePicker(activity)
            timePicker!!.setOnTimeChangedListener { view, hourOfDay, minute ->
                selectedHour = hourOfDay
                selectedMinute = minute
            }

            viewSwitcher!!.addView(timePicker)
            viewSwitcher!!.addView(datePicker)

            val linear_bottom = LinearLayout(activity)
            linear_match_wrap.topMargin = 8
            linear_bottom.layoutParams = linear_match_wrap

            btn_set = Button(activity)
            btn_set!!.layoutParams = button_params
            btn_set!!.text = "Set"
            btn_set!!.id = SET
            btn_set!!.setOnClickListener(this)

            btn_cancel = Button(activity)
            btn_cancel!!.layoutParams = button_params
            btn_cancel!!.text = "Cancel"
            btn_cancel!!.id = CANCEL
            btn_cancel!!.setOnClickListener(this)

            linear_bottom.addView(btn_set)
            linear_bottom.addView(btn_cancel)

            linear_child.addView(linear_top)
            linear_child.addView(viewSwitcher)
            linear_child.addView(linear_bottom)

            linear_main.addView(linear_child)

            return linear_main
        }

    init {
        iCustomDateTimeListener = customDateTimeListener

        dialog = Dialog(activity)
        dialog.setOnDismissListener { resetData() }

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogView = dateTimePickerLayout
        dialog.setContentView(dialogView)
    }

    fun showDialog() {
        if (!dialog.isShowing) {
            if (calendar_date == null)
                calendar_date = Calendar.getInstance()

            selectedHour = calendar_date!!.get(Calendar.HOUR_OF_DAY)
            selectedMinute = calendar_date!!.get(Calendar.MINUTE)

            timePicker!!.setIs24HourView(is24HourView)
            timePicker!!.currentHour = selectedHour
            timePicker!!.currentMinute = selectedMinute

            datePicker!!.updateDate(calendar_date!!.get(Calendar.YEAR),
                    calendar_date!!.get(Calendar.MONTH),
                    calendar_date!!.get(Calendar.DATE))

            dialog.show()

            btn_setDate!!.performClick()
        }
    }

    fun setAutoDismiss(isAutoDismiss: Boolean) {
        this.isAutoDismiss = isAutoDismiss
    }

    fun dismissDialog() {
        if (!dialog.isShowing)
            dialog.dismiss()
    }

    fun setDate(calendar: Calendar?) {
        if (calendar != null)
            calendar_date = calendar
    }

    fun setDate(date: Date?) {
        if (date != null) {
            calendar_date = Calendar.getInstance()
            calendar_date!!.time = date
        }
    }

    fun setDate(year: Int, month: Int, day: Int) {
        if (month < 12 && month >= 0 && day < 32 && day >= 0 && year > 100
                && year < 3000) {
            calendar_date = Calendar.getInstance()
            calendar_date!!.set(year, month, day)
        }

    }

    fun setTimeIn24HourFormat(hourIn24Format: Int, minute: Int) {
        if (hourIn24Format < 24 && hourIn24Format >= 0 && minute >= 0
                && minute < 60) {
            if (calendar_date == null)
                calendar_date = Calendar.getInstance()

            calendar_date!!.set(calendar_date!!.get(Calendar.YEAR),
                    calendar_date!!.get(Calendar.MONTH),
                    calendar_date!!.get(Calendar.DAY_OF_MONTH), hourIn24Format,
                    minute)

            is24HourView = true
        }
    }

    fun setTimeIn12HourFormat(hourIn12Format: Int, minute: Int,
                              isAM: Boolean) {
        var hourIn12Format = hourIn12Format
        if (hourIn12Format < 13 && hourIn12Format > 0 && minute >= 0
                && minute < 60) {
            if (hourIn12Format == 12)
                hourIn12Format = 0

            var hourIn24Format = hourIn12Format

            if (!isAM)
                hourIn24Format += 12

            if (calendar_date == null)
                calendar_date = Calendar.getInstance()

            calendar_date!!.set(calendar_date!!.get(Calendar.YEAR),
                    calendar_date!!.get(Calendar.MONTH),
                    calendar_date!!.get(Calendar.DAY_OF_MONTH), hourIn24Format,
                    minute)

            is24HourView = false
        }
    }

    fun set24HourFormat(is24HourFormat: Boolean) {
        is24HourView = is24HourFormat
    }

    interface ICustomDateTimeListener {
        fun onSet(dialog: Dialog, calendarSelected: Calendar,
                  dateSelected: Date, year: Int, monthFullName: String,
                  monthShortName: String, monthNumber: Int, day: Int,
                  weekDayFullName: String, weekDayShortName: String, hour24: Int,
                  hour12: Int, min: Int, sec: Int, AM_PM: String)

        fun onCancel()

    }

    override fun onClick(v: View) {
        when (v.id) {
            SET_DATE -> {
                btn_setTime!!.isEnabled = true
                btn_setDate!!.isEnabled = false

                if (viewSwitcher!!.currentView !== datePicker) {
                    viewSwitcher!!.showPrevious()
                }
            }

            SET_TIME -> {
                btn_setTime!!.isEnabled = false
                btn_setDate!!.isEnabled = true
                if (viewSwitcher!!.currentView === datePicker) {
                    viewSwitcher!!.showNext()
                }
            }


            SET -> {
                if (iCustomDateTimeListener != null) {
                    val month = datePicker!!.month
                    val year = datePicker!!.year
                    val day = datePicker!!.dayOfMonth


                    calendar_date!!.set(year, month, day, selectedHour,
                            selectedMinute)

                    iCustomDateTimeListener!!.onSet(dialog, calendar_date!!,
                            calendar_date!!.time, calendar_date!!
                            .get(Calendar.YEAR),
                            getMonthFullName(calendar_date!!.get(Calendar.MONTH)),
                            getMonthShortName(calendar_date!!.get(Calendar.MONTH)),
                            calendar_date!!.get(Calendar.MONTH), calendar_date!!
                            .get(Calendar.DAY_OF_MONTH),
                            getWeekDayFullName(calendar_date!!
                                    .get(Calendar.DAY_OF_WEEK)),
                            getWeekDayShortName(calendar_date!!
                                    .get(Calendar.DAY_OF_WEEK)), calendar_date!!
                            .get(Calendar.HOUR_OF_DAY),
                            getHourIn12Format(calendar_date!!
                                    .get(Calendar.HOUR_OF_DAY)), calendar_date!!
                            .get(Calendar.MINUTE), calendar_date!!
                            .get(Calendar.SECOND), getAMPM(calendar_date!!))
                }
                if (dialog.isShowing && isAutoDismiss)
                    dialog.dismiss()
            }

            CANCEL -> {
                if (iCustomDateTimeListener != null)
                    iCustomDateTimeListener!!.onCancel()
                if (dialog.isShowing)
                    dialog.dismiss()
            }
        }
    }

    private fun getMonthFullName(monthNumber: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, monthNumber)

        val simpleDateFormat = SimpleDateFormat("MMMM")
        simpleDateFormat.calendar = calendar

        return simpleDateFormat.format(calendar.time)
    }

    private fun getMonthShortName(monthNumber: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, monthNumber)

        val simpleDateFormat = SimpleDateFormat("MMM")
        simpleDateFormat.calendar = calendar

        return simpleDateFormat.format(calendar.time)
    }

    private fun getWeekDayFullName(weekDayNumber: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, weekDayNumber)

        val simpleDateFormat = SimpleDateFormat("EEEE")
        simpleDateFormat.calendar = calendar

        return simpleDateFormat.format(calendar.time)
    }

    private fun getWeekDayShortName(weekDayNumber: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, weekDayNumber)

        val simpleDateFormat = SimpleDateFormat("EE")
        simpleDateFormat.calendar = calendar

        return simpleDateFormat.format(calendar.time)
    }

    private fun getHourIn12Format(hour24: Int): Int {
        var hourIn12Format = 0

        if (hour24 == 0)
            hourIn12Format = 12
        else if (hour24 <= 12)
            hourIn12Format = hour24
        else
            hourIn12Format = hour24 - 12

        return hourIn12Format
    }

    private fun getAMPM(calendar: Calendar): String {
        return if (calendar.get(Calendar.AM_PM) == Calendar.AM)
            "AM"
        else
            "PM"
    }

    private fun resetData() {
        calendar_date = null
        is24HourView = true
    }

    fun dismiss() {
        this.dialog.dismiss()
    }

    fun cancel() {
        this.dialog.cancel()
    }

    fun isShowing(): Boolean {
        return this.dialog.isShowing()
    }

    companion object {

        /**
         * @param date
         * date in String
         * @param fromFormat
         * format of your **date** eg: if your date is 2011-07-07
         * 09:09:09 then your format will be **yyyy-MM-dd hh:mm:ss**
         * @param toFormat
         * format to which you want to convert your **date** eg: if
         * required format is 31 July 2011 then the toFormat should be
         * **d MMMM yyyy**
         * @return formatted date
         */
        fun convertDate(date: String, fromFormat: String,
                        toFormat: String): String {
            var date = date
            try {
                var simpleDateFormat = SimpleDateFormat(fromFormat)
                val d = simpleDateFormat.parse(date)
                val calendar = Calendar.getInstance()
                calendar.time = d

                simpleDateFormat = SimpleDateFormat(toFormat)
                simpleDateFormat.calendar = calendar
                date = simpleDateFormat.format(calendar.time)

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return date
        }

        fun pad(integerToPad: Int): String {
            return if (integerToPad >= 10 || integerToPad < 0)
                integerToPad.toString()
            else
                "0$integerToPad"
        }


    }
}