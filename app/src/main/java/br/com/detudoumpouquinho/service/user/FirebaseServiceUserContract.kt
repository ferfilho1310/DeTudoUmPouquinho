package br.com.detudoumpouquinho.service.user

import androidx.lifecycle.MutableLiveData
import br.com.detudoumpouquinho.model.User

interface FirebaseServiceUserContract {

    fun insertNewUser(user: User)
    fun signUser(user: User)
    fun resultCreateUser(): MutableLiveData<Boolean>
    fun resultSignUser(): MutableLiveData<Boolean>
    fun searchIdUser(userId: String)
    fun searchIdUserListener(): MutableLiveData<User>
    fun rescuePassWord(email: String)
    fun rescuePassWordListener(): MutableLiveData<Boolean>
}