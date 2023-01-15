package br.com.detudoumpouquinho.viewModel.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.detudoumpouquinho.productsUtils.Response
import br.com.detudoumpouquinho.model.Product
import br.com.detudoumpouquinho.productsUtils.Utils
import br.com.detudoumpouquinho.service.product.FirebaseServiceProductsContract
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.Exception


class ProductsViewModel(
    private val firebaseServiceProducts: FirebaseServiceProductsContract
) : ViewModel(),
    ProductsViewModelContract {

    private var _insertProductLiveData: MutableLiveData<Response<Boolean>> = MutableLiveData()
    var insertProductLiveData: LiveData<Response<Boolean>> = _insertProductLiveData

    private var _loadProductLiveData: MutableLiveData<Response<ArrayList<Product>>> = MutableLiveData()
    var loadProductLiveData: LiveData<Response<ArrayList<Product>>> = _loadProductLiveData

    private var _deleteProductLiveData: MutableLiveData<Response<Boolean>> = MutableLiveData()
    var deleteProductLiveData: LiveData<Response<Boolean>> = _deleteProductLiveData

    private var _searchProductIdLiveData: MutableLiveData<Response<Product?>> = MutableLiveData()
    var searchProductIdLiveData: LiveData<Response<Product?>> = _searchProductIdLiveData

    private var _updateProductLiveData: MutableLiveData<Response<Boolean>> = MutableLiveData()
    var updateProductLiveData: LiveData<Response<Boolean>> = _updateProductLiveData

    private var _isClientRegister: MutableLiveData<Boolean> = MutableLiveData()
    var isClientRegister: LiveData<Boolean> = _isClientRegister

    override fun insertProduct(product: Product) {
        _insertProductLiveData.value = Response.LOADING()
        firebaseServiceProducts.insertNewProduct(product)
            .onEach {
                _insertProductLiveData.value = Response.SUCCESS(it)
            }.catch {
                _insertProductLiveData.value = Response.ERROR(false)
                Utils.log("Erro ao inserir o produto", Exception(it))
            }.launchIn(viewModelScope)
    }

    override fun loadProducts() {
        _loadProductLiveData.value = Response.LOADING()
        firebaseServiceProducts.loadProducts()
            .onEach {
                _loadProductLiveData.value = Response.SUCCESS(it)
            }.catch {
                it.message
                _loadProductLiveData.value = Response.ERROR(arrayListOf())
                Utils.log("Erro ao carregar os produtos", Exception(it))
            }.launchIn(viewModelScope)
    }

    override fun deleteProduct(documentId: String?) {
        _deleteProductLiveData.value = Response.LOADING()
        firebaseServiceProducts.deleteProduct(documentId.orEmpty())
            .onEach {
                _deleteProductLiveData.value = Response.SUCCESS(it)
            }.catch {
                _deleteProductLiveData.value = Response.ERROR(false)
                Utils.log("Erro ao deletar produto", Exception(it))
            }.launchIn(viewModelScope)
    }

    override fun updateProduct(position: String, product: Product) {
        _updateProductLiveData.value = Response.LOADING()
        firebaseServiceProducts.updateProduct(position, product)
            .onEach {
                _updateProductLiveData.value = Response.SUCCESS(it)
            }.catch {
                _updateProductLiveData.value = Response.ERROR(false)
                Utils.log("Erro ao atualizar o produto ", Exception(it))
            }.launchIn(viewModelScope)
    }

    override fun buscarProdutosId(idProduct: String) {
        _searchProductIdLiveData.value = Response.LOADING()
        firebaseServiceProducts.searchProductId(idProduct)
            .onEach {
                _searchProductIdLiveData.value = Response.SUCCESS(it)
            }.catch {
                _searchProductIdLiveData.value = Response.ERROR(null)
                Utils.log("Erro ao buscar o produto por ID ", Exception(it))
            }.launchIn(viewModelScope)
    }

    override fun doRequest(isClientRegister: Boolean?) {
        _isClientRegister.value = isClientRegister
    }
}