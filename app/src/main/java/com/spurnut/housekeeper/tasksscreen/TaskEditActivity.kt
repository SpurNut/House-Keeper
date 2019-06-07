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
import java.util.*
import com.spurnut.housekeeper.R
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.content.ContextCompat
import android.content.Context
import android.os.Build
import android.view.View
import com.google.android.material.snackbar.Snackbar


class TaskEditActivity : AppCompatActivity(), Callback {

    private lateinit var recyclerView: RecyclerView
    var currentPhoto: String = ""
    val REQUEST_TAKE_PHOTO = 1
    private var images = listOf<Bitmap>()

    val imageViewAdapter: ImageViewAdapter = ImageViewAdapter(images)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (getIntent().hasExtra("START_CAMERA")) {
            take_photo()
        }

        setContentView(R.layout.activity_task_edit)

        // set up the RecyclerView
        val numberOfColumns = 3
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(
                3, StaggeredGridLayoutManager.VERTICAL)
        staggeredGridLayoutManager.gapStrategy =
                StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS


        recyclerView = findViewById<RecyclerView>(R.id.recycler_view_edit_image).apply {
            layoutManager = staggeredGridLayoutManager
            adapter = imageViewAdapter
        }
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
        recyclerView.addItemDecoration(SpacesItemDecoration(spacingInPixels))
        imageViewAdapter.callback = this
        updateImageData(images.plus(getBitmapFromVectorDrawable(
                this, R.drawable.ic_add_a_photo_black_24dp)))
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
            if (imgFile.exists()) {
                val myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath())
                updateImageData(images.plus(myBitmap))
            }
        }
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
            val imagePosition = data["remove"]
            val img = images.get(imagePosition!!)

            val mySnackbar = Snackbar.make(findViewById(R.id.coordinator_edit),
                    "Image removed", Snackbar.LENGTH_LONG)
            mySnackbar.setAction("undo", MyUndoListener(images))
            updateImageData(images.minus(img))
            mySnackbar.show()

        }
    }

    private fun updateImageData(newData: List<Bitmap>) {
        images = newData
        imageViewAdapter.imageDataSet = newData
        imageViewAdapter.notifyDataSetChanged()
    }

    inner class MyUndoListener(val oldImages: List<Bitmap>) : View.OnClickListener {

        override fun onClick(v: View?) {
            updateImageData(oldImages)
        }

    }

}
