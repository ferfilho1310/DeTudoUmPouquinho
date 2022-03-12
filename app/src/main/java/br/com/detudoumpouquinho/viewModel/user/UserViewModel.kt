package br.com.detudoumpouquinho.viewModel.user

import androidx.lifecycle.ViewModel
import br.com.detudoumpouquinho.model.User
import br.com.detudoumpouquinho.service.user.FirebaseServiceUserContract

class UserViewModel(
    private val firebaseServiceUserContract: FirebaseServiceUserContract
) : ViewModel(),
    UserViewModelContract {

    override fun createUser(user: User) {
        firebaseServiceUserContract.insertNewUser(user)
    }

    override fun signUser(user: User) {
        firebaseServiceUserContract.signUser(user)
    }

    override fun createUserListener() = firebaseServiceUserContract.resultCreateUser()

    override fun signUserListener() = firebaseServiceUserContract.resultSignUser()
}