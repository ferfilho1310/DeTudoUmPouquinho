package br.com.detudoumpouquinho.Utils

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

object PhotosUtils {

    fun stringToBitMap(encodedString: String?): Bitmap? {
        return try {
            val encodeByte =
                Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(
                encodeByte, 0,
                encodeByte.size
            )
        } catch (e: Exception) {
            e.message
            null
        }
    }

    fun uriToBitmap(
        imageReturnedIntent: Intent,
        getContentResolver: ContentResolver
    ): String? {

        try {
            val selectedImage: Uri? = imageReturnedIntent.data

            var imageStream: InputStream? = null
            try {
                imageStream = selectedImage?.let {
                    getContentResolver.openInputStream(
                        it
                    )
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            val bmp = BitmapFactory.decodeStream(imageStream)

            var stream: ByteArrayOutputStream? = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, 30, stream)
            val byteArray = stream!!.toByteArray()

            stream.close()
            return Base64.encodeToString(byteArray, Base64.DEFAULT)

        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
}