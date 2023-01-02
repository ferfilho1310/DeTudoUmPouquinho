package br.com.detudoumpouquinho.service.user

import androidx.lifecycle.MutableLiveData
import br.com.detudoumpouquinho.model.User
import kotlinx.coroutines.flow.Flow

interface FirebaseServiceUserContract {

    fun insertNewUser(user: User)
    fun signUser(user: User)
    fun resultCreateUser(): MutableLiveData<Boolean>
    fun resultSignUser(): MutableLiveData<Boolean>
    fun searchIdUser(userId: String): Flow<User?>
    fun rescuePassWord(email: String)
    fun rescuePassWordListener(): MutableLiveData<Boolean>
}