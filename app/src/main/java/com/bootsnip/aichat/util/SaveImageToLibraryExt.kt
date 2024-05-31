package com.bootsnip.aichat.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.OutputStream
import java.io.*
import java.util.Random


fun Bitmap.saveMediaToStorage(context: Context) {
    val filename = "${System.currentTimeMillis()}.jpg"
    var fos: OutputStream? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        context.contentResolver?.also { resolver ->
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            val imageUri: Uri? =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }
        }
    } else {
        val imagesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File(imagesDir, filename)
        fos = FileOutputStream(image)
    }
    fos?.use {
        this.compress(Bitmap.CompressFormat.JPEG, 100, it)
    }
}

fun Bitmap.shareImage(
    context: Context,
    isLoading: (Boolean) -> Unit
) {
    val rand = Random()
    val randNo = rand.nextInt(100000)

    val imgBitmapPath = MediaStore.Images.Media.insertImage(
        context.contentResolver, this,
        "ASTRAIMG:$randNo", null
    )
    val uri  = Uri.parse(imgBitmapPath)

    // share Intent
    val shareIntent = Intent(Intent.ACTION_SEND)
    shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
    shareIntent.type = "image/jpg"
    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    // Open the chooser dialog box
    context.startActivity(Intent.createChooser(shareIntent, "Share with"))
    isLoading(false)
}