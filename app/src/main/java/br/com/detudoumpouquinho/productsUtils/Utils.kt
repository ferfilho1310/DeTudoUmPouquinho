package br.com.detudoumpouquinho.productsUtils

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

object Utils {

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
        imageReturnedIntent: Intent?,
        getContentResolver: ContentResolver
    ): String? {

        try {
            val selectedImage: Uri? = imageReturnedIntent?.data

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
            bmp.compress(Bitmap.CompressFormat.JPEG, 12, stream)
            val byteArray = stream!!.toByteArray()

            stream.close()
            return Base64.encodeToString(byteArray, Base64.DEFAULT)

        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    fun alertDialog(
        context: Context,
        message: String,
        positiveButton: () -> Unit
    ) {
        AlertDialog.Builder(context)
            .setMessage(message)
            .setNegativeButton("Não") { p0, _ ->
                p0.dismiss()
            }.setPositiveButton("Sim") { _, _ ->
                positiveButton.invoke()
            }.show()
    }

    fun log(
        nameError: String,
        error: Exception
    ) {
        FirebaseCrashlytics.getInstance().log(nameError.plus(error))
    }
}