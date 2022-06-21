package br.com.detudoumpouquinho.firebaseMessagingService

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MessagingService: FirebaseMessagingService() {

    val token = FirebaseMessaging.getInstance().token.addOnCompleteListener {
        if (!it.isSuccessful) {
            Log.w("TAG", "Fetching FCM registration token failed", it.exception)
            return@addOnCompleteListener
        }

        // Get new FCM registration token
        val token = it.result

        Log.w("TAG", "TOKEN $token")

        Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
    }
}