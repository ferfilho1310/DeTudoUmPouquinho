package br.com.detudoumpouquinho.viewModel.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.detudoumpouquinho.model.Product
import br.com.detudoumpouquinho.productsUtils.Utils
import br.com.detudoumpouquinho.service.product.FirebaseServiceProductsContract
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.Exception


class ProductsViewModel(
    private val firebaseServiceProducts: FirebaseServiceProductsContract
) : ViewModel(),
    ProductsViewModelContract {

    private var _insertProductLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var insertProductLiveData: LiveData<Boolean> = _insertProductLiveData

    private var _loadProductLiveData: MutableLiveData<Query> = MutableLiveData()
    var loadProductLiveData: LiveData<Query> = _loadProductLiveData

    private var _searchProductLiveData: MutableLiveData<Query> = MutableLiveData()
    var searchProductLiveData: LiveData<Query> = _searchProductLiveData

    private var _deleteProductLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var deleteProductLiveData: LiveData<Boolean> = _deleteProductLiveData

    private var _searchProductIdLiveData: MutableLiveData<Product?> = MutableLiveData()
    var searchProductIdLiveData: LiveData<Product?> = _searchProductIdLiveData

    private var _updateProductLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var updateProductLiveData: LiveData<Boolean> = _updateProductLiveData

    private var _isClientRegister: MutableLiveData<Boolean> = MutableLiveData()
    var isClientRegister: LiveData<Boolean> = _isClientRegister

    override fun insertProduct(product: Product) {
        firebaseServiceProducts.insertNewProduct(product)
            .onEach {
                _insertProductLiveData.value = it
            }.catch {
                _insertProductLiveData.value = false
                Utils.log("Erro ao inserir o produto", Exception(it))
            }.launchIn(viewModelScope)
    }

    override fun loadProducts() {
        firebaseServiceProducts.loadProducts()
            .onEach {
                _loadProductLiveData.value = it
            }.catch {
                Utils.log("Erro ao carregar os produtos", Exception(it))
            }.launchIn(viewModelScope)
    }

    override fun deleteProduct(documentId: DocumentReference) {
        firebaseServiceProducts.deleteProduct(documentId)
            .onEach {
                _deleteProductLiveData.value = it
            }.catch {
                Utils.log("Erro ao deletar produto", Exception(it))
            }.launchIn(viewModelScope)
    }

    override fun searchProduct(nomeProduct: String) {
        firebaseServiceProducts.searchProducts(nomeProduct)
            .onEach {
                _loadProductLiveData.value = it
            }.catch {
                Utils.log("Erro ao buscar os produtos ", Exception(it))
            }.launchIn(viewModelScope)
    }

    override fun updateProduct(position: String, product: Product) {
        firebaseServiceProducts.updateProduct(position, product)
            .onEach {
                _updateProductLiveData.value = it
            }.catch {
                Utils.log("Erro ao atualizar o produto ", Exception(it))
            }.launchIn(viewModelScope)
    }

    override fun buscarProdutosId(idProducto: String) {
        firebaseServiceProducts.searchProductId(idProducto)
            .onEach {
                _searchProductIdLiveData.value = it
            }.catch {
                Utils.log("Erro ao buscar o produto por ID ", Exception(it))
            }.launchIn(viewModelScope)
    }

    override fun doRequest(isClientRegister: Boolean?) {
        _isClientRegister.value = isClientRegister
    }
}