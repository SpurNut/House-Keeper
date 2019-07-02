package com.spurnut.housekeeper.tasksscreen

import android.app.Activity
import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import java.io.File
import java.io.IOException
import com.spurnut.housekeeper.R
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.content.ContextCompat
import android.content.Context
import android.os.Build
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.spurnut.housekeeper.CustomDateTimePicker
import com.spurnut.housekeeper.database.enity.House
import com.spurnut.housekeeper.database.enity.Task
import com.spurnut.housekeeper.database.enity.TaskPhoto
import com.spurnut.housekeeper.database.enity.UrgencyImportantQuadrant
import com.spurnut.housekeeper.database.viewmodel.TaskViewModel
import com.spurnut.housekeeper.database.viewmodel.TaskViewModelFactory
import com.spurnut.housekeeper.receiver.ReminderReceiver
import java.util.*


class TaskEditActivity : AppCompatActivity(), Callback<String, Int> {

    private var newTask: Boolean = true
    private var task_id: Int = -1
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskviewModel: TaskViewModel
    var currentPhoto: String = ""
    val REQUEST_TAKE_PHOTO = 1
    private var images = listOf<Bitmap>()
    private lateinit var customDateTimePicker: CustomDateTimePicker
    private lateinit var task: Task
    private var house: House? = null

    val imageViewAdapter: ImageViewAdapter = ImageViewAdapter(images)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra(getString(R.string.edit_existing_task))) {
            newTask = false
            task_id = intent.getIntExtra(getString(R.string.edit_existing_task), -1)
        } else {
            if (intent.hasExtra(getString(R.string.start_camera))) {
                task_id = intent.getIntExtra(getString(R.string.start_camera), -1)
            } else if (intent.hasExtra(getString(R.string.task_from_text))) {
                task_id = intent.getIntExtra(getString(R.string.task_from_text), -1)
            }
        }

        val factory = TaskViewModelFactory(application, task_id)
        taskviewModel = ViewModelProviders.of(this, factory).get(TaskViewModel::class.java)

        taskviewModel.task.observe(this, Observer { task: Task ->
            // Update the cached copy of the task in the adapter.
            setTask(task)
            this.task = task
        })

        taskviewModel.images.observe(this, Observer { images_ ->
            // Update the cached copy of the words in the adapter.
            var newImages: List<Bitmap> = emptyList<Bitmap>()
            newImages = newImages.plus(getBitmapFromVectorDrawable(
                    this, R.drawable.ic_add_a_photo_black_24dp))
            images_?.let {
                for (image in it) {
                    val imgFile = File(image.uid)
                    if (imgFile.exists()) {
                        newImages = newImages.plus(BitmapFactory.decodeFile(imgFile.getAbsolutePath()))
                    }
                }
            }
            createCustomDateTimePicker()
            updateImageData(newImages)
        })

        taskviewModel.getHouseById(task_id).observe(this, Observer {
            this.house = it
        })


        if (getIntent().hasExtra(getString(R.string.start_camera))) {
            take_photo()
        }

        setContentView(R.layout.activity_task_edit)

        // set up the RecyclerView
        val numberOfColumns = 3
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(
                numberOfColumns, StaggeredGridLayoutManager.VERTICAL)
        staggeredGridLayoutManager.gapStrategy =
                StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS


        recyclerView = findViewById<RecyclerView>(R.id.recycler_view_edit_image).apply {
            layoutManager = staggeredGridLayoutManager
            adapter = imageViewAdapter
        }
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
        recyclerView.addItemDecoration(SpacesItemDecoration(spacingInPixels))
        imageViewAdapter.callback = this

        val assignHouseButton = findViewById<Button>(R.id.button_assign_house)
        assignHouseButton.setOnClickListener {
            updateTask()
            AssignHouseDialog(taskviewModel.task, taskviewModel).show(supportFragmentManager, getString(R.string.dialog))
        }

        val importanceButton = findViewById<Button>(R.id.button_importance)
        importanceButton.setOnClickListener {
            //ToDo Dialog and db call

        };

        val dueDateButton = findViewById<Button>(R.id.button_set_due_date)
        dueDateButton.setOnClickListener {
            //ToDo Dialog and db call

        };


        val reminderButton = findViewById<Button>(R.id.button_set_reminder)
        reminderButton.setOnClickListener {
            updateTask()


            customDateTimePicker.showDialog()
        };


    }

    private fun createCustomDateTimePicker() {
        val listener = object : CustomDateTimePicker.ICustomDateTimeListener {


            override fun onSet(dialog: Dialog, calendarSelected: Calendar,
                               dateSelected: Date, year: Int, monthFullName: String,
                               monthShortName: String, monthNumber: Int, day: Int,
                               weekDayFullName: String, weekDayShortName: String, hour24: Int,
                               hour12: Int, min: Int, sec: Int, AM_PM: String) {

                val am: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(applicationContext, ReminderReceiver::class.java)


                if (house != null) {
                    intent.putExtra("house", house?.streetName + " " + house?.streetNumber)
                }
                intent.putExtra("taskTitle", task.title)
                intent.putExtra("taskId", task.id)
                val pendingIntent = PendingIntent.getBroadcast(applicationContext, task_id, intent, 0)
                am.set(AlarmManager.RTC_WAKEUP, calendarSelected.timeInMillis, pendingIntent)
            }


            override fun onCancel() {

            }
        };


        customDateTimePicker = CustomDateTimePicker(this, listener)

        /**
         * Pass Directly current time format it will return AM and PM if you set
         * false
         */
        customDateTimePicker.set24HourFormat(true);
//            /**
//             * Pass Directly current data and time to show when it pop up
//             */
        customDateTimePicker.setDate(Calendar.getInstance());
    }

    private fun setTask(task: Task) {
        findViewById<TextInputLayout>(R.id.text_input_title).editText?.setText(task.title)
        findViewById<TextInputLayout>(R.id.editTextDescription).editText?.setText(task.description)
    }

    private fun take_photo() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            this.getPackageName(),
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            val imgFile = File(currentPhoto)

            //save picture in db
            savePhoto(currentPhoto)
//            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        updateTask()
    }

    private fun savePhoto(currentPhoto: String) {
        taskviewModel.insert(TaskPhoto(currentPhoto, task_id))
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhoto = absolutePath
        }
    }

    fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        var drawable = ContextCompat.getDrawable(context, drawableId)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable!!).mutate()
        }

        val bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth,
                drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    override fun callbackCall(data: Map<String, Int>) {

        if (data.containsKey(getString(R.string.add))) {
            take_photo()
        } else if (data.containsKey(getString(R.string.remove))) {
            val imagePosition = data[getString(R.string.remove)]!! - 1
            val id = taskviewModel.images.value!![imagePosition].taskId
            val filePath = taskviewModel.images.value!![imagePosition].uid

            taskviewModel.delete(taskviewModel.images.value!![imagePosition])

            val mySnackbar = Snackbar.make(findViewById(R.id.coordinator_edit),
                    getString(R.string.image_removed), Snackbar.LENGTH_LONG)
            mySnackbar.setAction(getString(R.string.undo), MyUndoListener(filePath, id))
            mySnackbar.show()

        }
    }

    private fun updateTask() {
        taskviewModel.update(Task(task_id, false, findViewById<TextInputLayout>(R.id.text_input_title).editText?.text.toString(), UrgencyImportantQuadrant.URGENT_NOT_IMPORTANT, findViewById<TextInputLayout>(R.id.editTextDescription).editText?.text.toString(), taskviewModel.task.value!!.houseId, Date(1290213012)))
    }

    private fun updateImageData(newData: List<Bitmap>) {
        images = newData
        imageViewAdapter.imageDataSet = newData
        imageViewAdapter.notifyDataSetChanged()
    }

    inner class MyUndoListener(val filePath: String, val id: Int) : View.OnClickListener {

        override fun onClick(v: View?) {
            taskviewModel.insert(TaskPhoto(filePath, id))
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        customDateTimePicker.dismiss()
    }


}
