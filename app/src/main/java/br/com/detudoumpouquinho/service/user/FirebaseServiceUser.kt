package br.com.detudoumpouquinho.service.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import br.com.detudoumpouquinho.model.User
import br.com.detudoumpouquinho.view.ProductsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseServiceUser : FirebaseServiceUserContract {

    private val firestoreInstance = FirebaseFirestore.getInstance()
    private val firestoreCreateUserInstance = FirebaseAuth.getInstance()

    private val createUserListener: MutableLiveData<Boolean> = MutableLiveData()
    private val signUserListener: MutableLiveData<Boolean> = MutableLiveData()
    private val userListener: MutableLiveData<User> = MutableLiveData()

    override fun insertNewUser(user: User) {
        firestoreCreateUserInstance
            .createUserWithEmailAndPassword(user.email.orEmpty(), user.password.orEmpty())
            .addOnSuccessListener {
                val map: MutableMap<String, String?> = HashMap()
                map["name"] = user.name
                map["email"] = user.email
                map["identifier"] = ProductsActivity.USER

                firestoreInstance
                    .collection("User")
                    .document(firestoreCreateUserInstance.uid.orEmpty())
                    .set(map)

                createUserListener.value = true
            }.addOnFailureListener {
                Log.e("ERROR", "Algo deu errado $it")
                createUserListener.value = false
            }
    }

    override fun signUser(user: User) {
        firestoreCreateUserInstance.signInWithEmailAndPassword(user.email.orEmpty(), user.password.orEmpty())
            .addOnSuccessListener {
                signUserListener.value = true
            }.addOnFailureListener {
                Log.e("ERROR", "Algo deu errado $it")
                signUserListener.value = false
            }
    }

    override fun searchIdUser(userId: String) {
        firestoreInstance
            .collection("User")
            .document(userId)
            .get()
            .addOnSuccessListener {
                userListener.value = it.toObject(User::class.java)
            }.addOnFailureListener {
                val user: User? = null
                userListener.value = user
            }
    }

    override fun resultCreateUser() = createUserListener

    override fun resultSignUser() = signUserListener

    override fun searchIdUserListener() = userListener
}