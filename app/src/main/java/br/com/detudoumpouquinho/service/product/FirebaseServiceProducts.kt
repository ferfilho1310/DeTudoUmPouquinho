package br.com.detudoumpouquinho.service.product

import android.util.Log
import androidx.lifecycle.MutableLiveData
import br.com.detudoumpouquinho.model.Product
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.*
import kotlin.collections.ArrayList

@ExperimentalCoroutinesApi
class FirebaseServiceProducts : FirebaseServiceProductsContract {

    private val firestoreInstance = FirebaseFirestore.getInstance()

    private val loadNewProductListener by lazy { MutableLiveData<Query>() }
    private val updateProduct by lazy { MutableLiveData<Boolean>() }

    override fun insertNewProduct(product: Product): Flow<Boolean> {
        return callbackFlow {
            val map: MutableMap<String, Any?> = HashMap()

            product.let {
                map["description"] = it.description
                map["image"] = it.image
                map["seller"] = it.seller
                map["nameProduct"] = it.nameProduct
                map["value"] = it.value
                map["nameProductUpperCase"] = it.nameProduct?.uppercase()
                map["valueFrete"] = it.valueFrete
                map["paymentForm"] = it.paymentForm
                map["id"] = ""
            }

            val listener = firestoreInstance
                .collection("Products")
                .add(map)
                .addOnSuccessListener {
                    updateUniqueProduct(it.id)
                    Log.i("PRODUCT_ID", "Id do produto salvo ${it.id}")
                    trySend(true).isSuccess
                }.addOnFailureListener {
                    trySend(false).isFailure
                }

            awaitClose {
                listener.isCanceled
            }
        }
    }

    private fun updateUniqueProduct(id: String) {
        val map: MutableMap<String, Any?> = HashMap()
        map["id"] = id
        firestoreInstance
            .collection("Products")
            .document(id)
            .update(map)
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }
    }

    override fun loadProducts(): Flow<ArrayList<Product>> {
        return callbackFlow {
            val listener = firestoreInstance
                .collection("Products")
                .get()
                .addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                trySend(products as ArrayList<Product>).isSuccess
            }.addOnFailureListener {
                trySend(arrayListOf()).isFailure
            }

            awaitClose {
                listener.isComplete
            }
        }
    }

    override fun deleteProduct(documentId: String): Flow<Boolean> {
        return callbackFlow {
            val listener = firestoreInstance
                .collection("Products")
                .document(documentId)
                .delete()
                .addOnSuccessListener {
                    trySend(true).isSuccess
                }
                .addOnFailureListener {
                    trySend(false).isFailure
                }
            awaitClose {
                listener.isComplete
            }
        }

    }

    /*override fun searchProducts(nomeProduto: String): Flow<Query> {
        return callbackFlow {
            val query =
                firestoreInstance.collection("Products").orderBy("nameProductUpperCase").startAt(
                    nomeProduto.uppercase(
                        Locale.getDefault()
                    )
                ).endAt(nomeProduto.uppercase(Locale.getDefault()) + "\uf8ff")
            trySend(query).isSuccess
        }
    }*/

    override fun updateProduct(documentId: String, model: Product): Flow<Boolean> {
        return callbackFlow {
            val map: MutableMap<String, Any?> = HashMap()

            model.let {
                map["description"] = it.description
                map["image"] = it.image
                map["seller"] = it.seller
                map["nameProduct"] = it.nameProduct
                map["value"] = it.value
                map["nameProductUpperCase"] = it.nameProduct?.uppercase()
                map["valueFrete"] = it.valueFrete
                map["paymentForm"] = it.paymentForm
            }

            val listener = firestoreInstance
                .collection("Products")
                .document(documentId)
                .update(map)
                .addOnSuccessListener {
                    trySend(true).isSuccess
                }
                .addOnFailureListener {
                    trySend(false).isFailure
                }
            awaitClose {
                listener.isComplete
            }
        }
    }

    override fun searchProductId(idProducto: String): Flow<Product?> {
        return callbackFlow {
            val listerner = firestoreInstance
                .collection("Products")
                .document(idProducto)
                .get()
                .addOnSuccessListener {
                    trySend(it.toObject(Product::class.java)).isSuccess
                }.addOnFailureListener {
                    val product: Product? = null
                    trySend(product).isFailure
                }
            awaitClose {
                listerner.isComplete
            }
        }
    }
}