package br.com.detudoumpouquinho.viewModel.products

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.detudoumpouquinho.model.Product
import br.com.detudoumpouquinho.service.product.FirebaseServiceProductsContract
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query


class ProductsViewModel(
    private val firebaseServiceProducts: FirebaseServiceProductsContract
) : ViewModel(),
    ProductsViewModelContract {

    override fun insertProduct(product: Product) {
        firebaseServiceProducts.insertNewProduct(product)
    }

    override fun loadProducts() {
        firebaseServiceProducts.loadProducts()
    }

    override fun loadProdutsListener() = firebaseServiceProducts.loadProductsListener()

    override fun deleteProduct(documentId: DocumentReference) {
        firebaseServiceProducts.deleteProduct(documentId)
    }

    override fun searchProduct(nomeProduct: String) {
        firebaseServiceProducts.buscarProdutos(nomeProduct)
    }

    override fun searchProduct() = firebaseServiceProducts.buscarProdutosListener()

    override fun deleteProductListener() = firebaseServiceProducts.deleteProductListener()

    override fun insertProductListener() = firebaseServiceProducts.insertNewProductListener()

    override fun updateProduct(position: String, product: Product) {
        firebaseServiceProducts.updateProduct(position, product)
    }

    override fun updateProductListener() = firebaseServiceProducts.updateProductListener()

    override fun buscarProdutosId(idProducto: String) {
        firebaseServiceProducts.buscarProdutosId(idProducto)
    }

    override fun buscarProdutosIdListener() = firebaseServiceProducts.buscarProdutosIdListener()
}