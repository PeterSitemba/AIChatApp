package com.bootsnip.aichat.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import okio.source
import java.io.*
import java.util.Random


fun Bitmap.getFilePath(context: Context): Pair<String, InputStream?> {
    val filename = "${System.currentTimeMillis()}.png"
    var fos: OutputStream? = null
    var inputStream: InputStream? = null
    context.contentResolver?.also { resolver ->
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
        val imageUri: Uri? =
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        fos = imageUri?.let { resolver.openOutputStream(it) }
        inputStream = imageUri?.let { resolver.openInputStream(it) }
    }

    fos?.use {
        this.compress(Bitmap.CompressFormat.PNG, 100, it)
    }

    return Pair(filename, inputStream)
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
    val uri = Uri.parse(imgBitmapPath)

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