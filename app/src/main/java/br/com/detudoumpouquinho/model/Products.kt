package br.com.detudoumpouquinho.model

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

data class Products(
    val name: String,
    val price: String,
    val image: String
)
