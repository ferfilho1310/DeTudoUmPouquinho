package br.com.detudoumpouquinho.viewModel.user

import br.com.detudoumpouquinho.model.User

interface UserViewModelContract {
    fun createUser(user: User)
    fun signUser(user: User)
    fun searchIdUser(userId: String)
    fun rescuePassWord(email: String)
    fun signUserAnonimous()
}