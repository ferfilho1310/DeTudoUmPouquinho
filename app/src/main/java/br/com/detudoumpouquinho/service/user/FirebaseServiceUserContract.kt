package br.com.detudoumpouquinho.service.user

import androidx.lifecycle.MutableLiveData
import br.com.detudoumpouquinho.model.Products
import br.com.detudoumpouquinho.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference

interface FirebaseServiceUserContract {

    fun insertNewProduct(products: Products): Task<DocumentReference>
    fun insertNewUser(user: User)
    fun signUser(user: User)
    fun resultCreateUser(): MutableLiveData<Boolean>
    fun resultSignUser(): MutableLiveData<Boolean>
}