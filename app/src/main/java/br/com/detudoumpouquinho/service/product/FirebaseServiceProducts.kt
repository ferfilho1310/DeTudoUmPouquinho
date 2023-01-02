package br.com.detudoumpouquinho.service.product

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
            }

            val listener = firestoreInstance
                .collection("Products")
                .add(map)
                .addOnSuccessListener {
                    trySend(true).isSuccess
                }.addOnFailureListener {
                    trySend(false).isFailure
                }

            awaitClose {
                listener.isCanceled
            }
        }
    }

    override fun loadProducts(): Flow<Query> {
        return callbackFlow {
            val query = firestoreInstance.collection("Products")
            trySend(query).isSuccess

            awaitClose {

            }
        }
    }

    override fun deleteProduct(documentId: DocumentReference): Flow<Boolean> {
        return callbackFlow {
            val listener = firestoreInstance
                .collection("Products")
                .document(documentId.id)
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

    override fun searchProducts(nomeProduto: String): Flow<Query> {
        return callbackFlow {
            val query =
                firestoreInstance.collection("Products").orderBy("nameProductUpperCase").startAt(
                    nomeProduto.uppercase(
                        Locale.getDefault()
                    )
                ).endAt(nomeProduto.uppercase(Locale.getDefault()) + "\uf8ff")
            trySend(query).isSuccess
        }
    }

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