package br.com.detudoumpouquinho.service.product

import androidx.lifecycle.MutableLiveData
import br.com.detudoumpouquinho.model.Product
import com.google.firebase.firestore.*
import kotlinx.coroutines.flow.Flow

interface FirebaseServiceProductsContract {

    fun insertNewProduct(product: Product): Flow<Boolean>
    fun loadProducts(): Flow<Query>
    fun deleteProduct(documentId: DocumentReference): Flow<Boolean>
    fun searchProducts(nomeProduto: String): Flow<Query>
    fun updateProduct(documentId: String, model: Product): Flow<Boolean>
    fun searchProductId(idProducto: String): Flow<Product?>
}