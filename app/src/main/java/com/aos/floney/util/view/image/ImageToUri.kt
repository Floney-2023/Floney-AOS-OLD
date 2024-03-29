package com.aos.floney.util.view.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.widget.ImageView
import java.io.File
import java.io.FileOutputStream


object ImageToUri {

    fun bitmapToUri(context: Context, bitmap: Bitmap): Uri? {
        val file = File(context.cacheDir, "temp_image.png")
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return Uri.fromFile(file)
    }

    fun cropBitmapToSquare(bitmap: Bitmap): Bitmap {
        val size = Math.min(bitmap.width, bitmap.height)
        val x = (bitmap.width - size) / 2
        val y = (bitmap.height - size) / 2
        return Bitmap.createBitmap(bitmap, x, y, size, size)
    }

    fun saveProfileImageToFile(context: Context, imageView: ImageView): Uri? {
        // ImageView에서 Bitmap 가져오기
        imageView.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(imageView.drawingCache)
        imageView.isDrawingCacheEnabled = false

        val squaredBitmap = cropBitmapToSquare(bitmap)
        return bitmapToUri(context, squaredBitmap)
    }
}