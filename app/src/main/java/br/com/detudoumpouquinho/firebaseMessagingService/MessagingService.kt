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
            Log.w("TAG", "Falha ao receber o push", it.exception)
            return@addOnCompleteListener
        }

        val token = it.result

        Log.w("TAG", "TOKEN $token")
    }
}