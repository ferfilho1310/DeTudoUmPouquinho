package br.com.detudoumpouquinho.viewModel.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.detudoumpouquinho.model.User
import br.com.detudoumpouquinho.productsUtils.Utils
import br.com.detudoumpouquinho.service.user.FirebaseServiceUserContract
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.Exception

class UserViewModel(
    private val firebaseServiceUserContract: FirebaseServiceUserContract
) : ViewModel(),
    UserViewModelContract {

    private val _searchIdUser: MutableLiveData<User> = MutableLiveData()
    var searchIdUser: LiveData<User> = _searchIdUser

    private val _createUser: MutableLiveData<Boolean> = MutableLiveData()
    var createUser: LiveData<Boolean> = _createUser

    private val _signUser: MutableLiveData<Boolean> = MutableLiveData()
    var signUser: LiveData<Boolean> = _signUser

    private val _rescuePasswordUser: MutableLiveData<Boolean> = MutableLiveData()
    var rescuePasswordUser: LiveData<Boolean> = _rescuePasswordUser

    override fun createUser(user: User) {
        firebaseServiceUserContract.insertNewUser(user)
            .onEach {
                _createUser.value = it
            }.catch {
                Utils.log("Erro ao criar o usuário", Exception(it))
            }.launchIn(viewModelScope)
    }

    override fun signUser(user: User) {
        firebaseServiceUserContract.signUser(user)
            .onEach {
                _signUser.value = it
            }.catch {
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
        firebaseServiceUserContract.rescuePassWord(email)
            .onEach {
                _rescuePasswordUser.value = it
            }.catch {
                Utils.log("Erro ao recuperar a senha do usuário", Exception(it))
            }.launchIn(viewModelScope)
    }
}