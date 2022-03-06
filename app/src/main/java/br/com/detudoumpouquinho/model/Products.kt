package br.com.detudoumpouquinho.model

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

data class Products(
    val name: String,
    val price: String,
    val image: String
) {
    companion object {
        fun insertNewProduct(products: Products): Task<DocumentReference> {
            val createProducts = FirebaseFirestore.getInstance()
            val map: MutableMap<String, String?> = HashMap()
            map["name"] = products.name
            map["price"] = products.price
            map["image"] = products.image

            return createProducts
                .collection("Products")
                .add(map)
                .addOnSuccessListener {
                    return@addOnSuccessListener
                }.addOnFailureListener {
                    return@addOnFailureListener
                }
        }
    }
}
