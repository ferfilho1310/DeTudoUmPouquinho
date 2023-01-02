package br.com.detudoumpouquinho.viewModel.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.detudoumpouquinho.model.User
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

    override fun createUser(user: User) {
        firebaseServiceUserContract.insertNewUser(user)
    }

    override fun signUser(user: User) {
        firebaseServiceUserContract.signUser(user)
    }

    override fun searchIdUser(userId: String) {
        firebaseServiceUserContract.searchIdUser(userId)
            .onEach {
                it?.let { user ->
                    _searchIdUser.value = user
                }
            }.catch {

            }.launchIn(viewModelScope)
    }

    override fun rescuePassWord(email: String) {
        firebaseServiceUserContract.rescuePassWord(email)
    }

    override fun createUserListener() = firebaseServiceUserContract.resultCreateUser()

    override fun signUserListener() = firebaseServiceUserContract.resultSignUser()

    override fun rescuePassWordListener() = firebaseServiceUserContract.rescuePassWordListener()
}