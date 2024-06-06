package com.bootsnip.aichat.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import java.io.*


fun Bitmap.saveImageAndRetrieveUri(context: Context): String {
    val filename = "ASTRAIMG${System.currentTimeMillis()}.png"
    var fos: OutputStream? = null
    var filePath = ""
    context.contentResolver?.also { resolver ->
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
        val imageUri: Uri? =
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        fos = imageUri?.let { resolver.openOutputStream(it) }
        filePath = imageUri.toString()
    }

    fos?.use {
        this.compress(Bitmap.CompressFormat.PNG, 100, it)
    }

    return filePath
}

fun getFileNameAndInputStream(context: Context, uri: Uri): Pair<String, InputStream?> {
    var inputStream: InputStream? = null
    var fileName = ""
    context.contentResolver?.also { resolver ->
        uri.let {
            inputStream = resolver.openInputStream(it)
            resolver.query(it, null, null, null, null)
        }?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            fileName = cursor.getString(nameIndex)
        }
    }
    return Pair(fileName, inputStream)
}


fun shareImage(
    context: Context,
    uri: Uri,
    isLoading: (Boolean) -> Unit,
) {
    val shareIntent = Intent(Intent.ACTION_SEND)
    shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
    shareIntent.type = "image/jpg"
    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    context.startActivity(Intent.createChooser(shareIntent, "Share with"))
    isLoading(false)
}