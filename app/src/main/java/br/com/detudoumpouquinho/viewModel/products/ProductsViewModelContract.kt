package br.com.detudoumpouquinho.viewModel.products

import androidx.lifecycle.MutableLiveData
import br.com.detudoumpouquinho.model.Product
import com.google.firebase.firestore.*

interface ProductsViewModelContract {
    fun insertProduct(product: Product)
    fun loadProducts()
    fun searchProduct(nomeProduct: String)
    fun deleteProduct(documentId: DocumentReference)
    fun updateProduct(position: String, product: Product)
    fun buscarProdutosId(idProducto: String)
}