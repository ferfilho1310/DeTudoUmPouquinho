package br.com.detudoumpouquinho.viewModel.products

import androidx.lifecycle.MutableLiveData
import br.com.detudoumpouquinho.model.Product
import com.google.firebase.firestore.*

interface ProductsViewModelContract {
    fun insertProduct(product: Product)
    fun insertProductListener(): MutableLiveData<Boolean>
    fun loadProducts()
    fun loadProdutsListener(): MutableLiveData<Query>
    fun searchProduct(nomeProduct: String)
    fun searchProduct(): MutableLiveData<Query>
    fun deleteProduct(documentId: DocumentReference)
    fun deleteProductListener(): MutableLiveData<Boolean>
    fun updateProduct(position: String, product: Product)
    fun updateProductListener(): MutableLiveData<Boolean>
}