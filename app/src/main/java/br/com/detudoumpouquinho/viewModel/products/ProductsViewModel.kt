package br.com.detudoumpouquinho.viewModel.products

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.detudoumpouquinho.model.Products
import br.com.detudoumpouquinho.service.user.FirebaseServiceUser


class ProductsViewModel(
    private val firebaseServiceUserContract: FirebaseServiceUser
) : ViewModel(), ProductsViewModelContract {

    private val _productsLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val productsLiveData = _productsLiveData

    override fun insertProduct(products: Products) {
        runCatching {
            firebaseServiceUserContract.insertNewProduct(products)
        }.onSuccess {
            _productsLiveData.value = it.isSuccessful
        }
    }
}