package br.com.detudoumpouquinho.service.product

import androidx.lifecycle.MutableLiveData
import br.com.detudoumpouquinho.model.Product
import com.google.firebase.firestore.*

interface FirebaseServiceProductsContract {

    fun insertNewProduct(product: Product)
    fun insertNewProductListener(): MutableLiveData<Boolean>
    fun loadProducts()
    fun loadProductsListener(): MutableLiveData<Query>
    fun deleteProduct(documentId: DocumentReference)
    fun deleteProductListener(): MutableLiveData<Boolean>
    fun buscarProdutos(nomeProduto: String)
    fun buscarProdutosListener(): MutableLiveData<Query>
    fun updateProduct(documentId: String, model: Product)
    fun updateProductListener(): MutableLiveData<Boolean>
}