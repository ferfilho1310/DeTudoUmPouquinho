package br.com.detudoumpouquinho.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.detudoumpouquinho.model.Products
import br.com.detudoumpouquinho.model.Products.Companion.insertNewProduct

class ProductsViewModel : ViewModel() {

    private val _productsLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val productsLiveData = _productsLiveData

    fun insertProduct(products: Products) {
        runCatching {
            insertNewProduct(products)
        }.onSuccess {
            _productsLiveData.value = it.isSuccessful
        }
    }
}