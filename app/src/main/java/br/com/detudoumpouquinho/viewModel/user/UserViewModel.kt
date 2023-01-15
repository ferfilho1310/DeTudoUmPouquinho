package br.com.detudoumpouquinho.viewModel.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.detudoumpouquinho.productsUtils.Response
import br.com.detudoumpouquinho.model.User
import br.com.detudoumpouquinho.productsUtils.Utils
import br.com.detudoumpouquinho.service.user.FirebaseServiceUserContract
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class UserViewModel(
    private val firebaseServiceUserContract: FirebaseServiceUserContract
) : ViewModel(),
    UserViewModelContract {

    private val _searchIdUser: MutableLiveData<User> = MutableLiveData()
    var searchIdUser: LiveData<User> = _searchIdUser

    private val _createUser: MutableLiveData<Response<Boolean>> = MutableLiveData()
    var createUser: LiveData<Response<Boolean>> = _createUser

    private val _signUser: MutableLiveData<Response<Boolean>> = MutableLiveData()
    var signUser: LiveData<Response<Boolean>> = _signUser

    private val _rescuePasswordUser: MutableLiveData<Response<Boolean>> = MutableLiveData()
    var rescuePasswordUser: LiveData<Response<Boolean>> = _rescuePasswordUser

    private val _signUserAnonimous: MutableLiveData<Response<Boolean>> = MutableLiveData()
    var signUserAnonimous: LiveData<Response<Boolean>> = _signUserAnonimous

    override fun createUser(user: User) {
        _createUser.value = Response.LOADING()
        firebaseServiceUserContract.insertNewUser(user)
            .onEach {
                _createUser.value = Response.SUCCESS(it)
            }.catch {
                _createUser.value = Response.ERROR(false)
                Utils.log("Erro ao criar o usuário", Exception(it))
            }.launchIn(viewModelScope)
    }

    override fun signUser(user: User) {
        _signUser.value = Response.LOADING()
        firebaseServiceUserContract.signUser(user)
            .onEach {
                _signUser.value = Response.SUCCESS(it)
            }.catch {
                _signUser.value = Response.ERROR(false)
                Utils.log("Erro ao tentar fazer o login", Exception(it))
            }.launchIn(viewModelScope)
    }

    override fun searchIdUser(userId: String) {
        firebaseServiceUserContract.searchIdUser(userId)
            .onEach {
                it?.let { user ->
                    _searchIdUser.value = user
                }
            }.catch {
                Utils.log("Erro ao buscar o usuário", Exception(it))
            }.launchIn(viewModelScope)
    }

    override fun rescuePassWord(email: String) {
        _rescuePasswordUser.value = Response.LOADING()
        firebaseServiceUserContract.rescuePassWord(email)
            .onEach {
                _rescuePasswordUser.value = Response.SUCCESS(it)
            }.catch {
                _rescuePasswordUser.value = Response.ERROR(false)
                Utils.log("Erro ao recuperar a senha do usuário", Exception(it))
            }.launchIn(viewModelScope)
    }

    override fun signUserAnonimous() {
        firebaseServiceUserContract.signUserAnonimous()
            .onEach {
                _signUserAnonimous.value = Response.SUCCESS(it)
            }.catch {
                _signUserAnonimous.value = Response.ERROR(false)
            }.launchIn(viewModelScope)
    }
}