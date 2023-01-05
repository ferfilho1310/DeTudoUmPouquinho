package br.com.detudoumpouquinho.service.user

import androidx.lifecycle.MutableLiveData
import br.com.detudoumpouquinho.model.User
import kotlinx.coroutines.flow.Flow

interface FirebaseServiceUserContract {

    fun insertNewUser(user: User): Flow<Boolean>
    fun signUser(user: User): Flow<Boolean>
    fun searchIdUser(userId: String): Flow<User?>
    fun rescuePassWord(email: String): Flow<Boolean>
}