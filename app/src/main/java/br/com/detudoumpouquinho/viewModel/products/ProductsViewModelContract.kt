package br.com.detudoumpouquinho.viewModel.products

import br.com.detudoumpouquinho.model.Product
import com.google.firebase.firestore.*

interface ProductsViewModelContract {
    fun insertProduct(product: Product)
    fun loadProducts()
    fun deleteProduct(documentId: String?)
    fun updateProduct(position: String, product: Product)
    fun buscarProdutosId(idProducto: String)
    fun doRequest(isClientRegister: Boolean?)
}