package com.spurnut.housekeeper.tasksscreen

import android.app.Activity
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
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.spurnut.housekeeper.database.enity.Task
import com.spurnut.housekeeper.database.enity.TaskPhoto
import com.spurnut.housekeeper.database.enity.UrgencyImportantQuadrant
import com.spurnut.housekeeper.database.viewmodel.TaskViewModel
import com.spurnut.housekeeper.database.viewmodel.TaskViewModelFactory
import java.util.*


class TaskEditActivity : AppCompatActivity(), Callback<String, Int> {

    private var newTask: Boolean = true
    private var task_id: Int = -1
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskviewModel: TaskViewModel
    var currentPhoto: String = ""
    val REQUEST_TAKE_PHOTO = 1
    private var images = listOf<Bitmap>()

    val imageViewAdapter: ImageViewAdapter = ImageViewAdapter(images)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra("edit_existing_task")) {
            newTask = false
            task_id = intent.getIntExtra("edit_existing_task", -1)
        } else {
            if (intent.hasExtra("START_CAMERA")) {
                task_id = intent.getIntExtra("START_CAMERA", -1)
            } else if (intent.hasExtra("TASK_FROM_TEXT")) {
                task_id = intent.getIntExtra("TASK_FROM_TEXT", -1)
            }
        }

        val factory = TaskViewModelFactory(application, task_id)
        taskviewModel = ViewModelProviders.of(this, factory).get(TaskViewModel::class.java)

        taskviewModel.task.observe(this, Observer { task: Task ->
            // Update the cached copy of the task in the adapter.
            setTask(task)
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
            updateImageData(newImages)
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


        val applyButton = findViewById<Button>(R.id.button_close)
        applyButton.setOnClickListener {
            //ToDo db aufruf
            onBackPressed()
        };


    }

    private fun setTask(task: Task) {
        findViewById<TextInputLayout>(R.id.text_input_title).editText?.setText(task.title)
        findViewById<EditText>(R.id.editTextDescription).setText(task.description)
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

        if (data.containsKey("add")) {
            take_photo()
        } else if (data.containsKey("remove")) {
            val imagePosition = data["remove"]!! - 1
            val id = taskviewModel.images.value!![imagePosition].taskId
            val filePath = taskviewModel.images.value!![imagePosition].uid

            taskviewModel.delete(taskviewModel.images.value!![imagePosition])

            val mySnackbar = Snackbar.make(findViewById(R.id.coordinator_edit),
                    "Image removed", Snackbar.LENGTH_LONG)
            mySnackbar.setAction("undo", MyUndoListener(filePath, id))
            mySnackbar.show()

        }
    }

    private fun updateTask() {
        taskviewModel.update(Task(task_id, false, findViewById<TextInputLayout>(R.id.text_input_title).editText?.text.toString(), UrgencyImportantQuadrant.URGENT_NOT_IMPORTANT, findViewById<EditText>(R.id.editTextDescription).text.toString(), Date(1290213012)))
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

}
