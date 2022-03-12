package br.com.detudoumpouquinho.service.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import br.com.detudoumpouquinho.model.Products
import br.com.detudoumpouquinho.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseServiceUser : FirebaseServiceUserContract {

    private val firestoreInstance = FirebaseFirestore.getInstance()
    private val firestoreCreateUserInstance = FirebaseAuth.getInstance()

    private val createUserListener: MutableLiveData<Boolean> = MutableLiveData()
    private val signUserListener: MutableLiveData<Boolean> = MutableLiveData()

    override fun insertNewProduct(products: Products): Task<DocumentReference> {
        val map: MutableMap<String, String?> = HashMap()
        map["name"] = products.name
        map["price"] = products.price
        map["image"] = products.image

        return firestoreInstance
            .collection("Products")
            .add(map)
            .addOnSuccessListener {
                return@addOnSuccessListener
            }.addOnFailureListener {
                return@addOnFailureListener
            }
    }

    override fun insertNewUser(user: User) {
        firestoreCreateUserInstance
            .createUserWithEmailAndPassword(user.email, user.password)
            .addOnSuccessListener {
                val map: MutableMap<String, String?> = HashMap()
                map["name"] = user.name
                map["email"] = user.email

                firestoreInstance
                    .collection("User")
                    .add(map)

                createUserListener.value = true
            }.addOnFailureListener {
                Log.e("ERROR", "Algo deu errado $it")
                createUserListener.value = false
            }
    }

    override fun signUser(user: User) {
        firestoreCreateUserInstance.signInWithEmailAndPassword(user.email, user.password)
            .addOnSuccessListener {
                signUserListener.value = true
            }.addOnFailureListener {
                Log.e("ERROR", "Algo deu errado $it")
                signUserListener.value = false
            }
    }

    override fun resultCreateUser() = createUserListener

    override fun resultSignUser() = signUserListener
}