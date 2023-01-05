package br.com.detudoumpouquinho.service.user

import android.util.Log
import br.com.detudoumpouquinho.model.User
import br.com.detudoumpouquinho.view.ProductsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseServiceUser : FirebaseServiceUserContract {

    private val firestoreInstance = FirebaseFirestore.getInstance()
    private val firestoreCreateUserInstance = FirebaseAuth.getInstance()

    override fun insertNewUser(user: User): Flow<Boolean> {
        return callbackFlow {
            val listener = firestoreCreateUserInstance
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
                    trySend(true).isFailure
                }.addOnFailureListener {
                    Log.e("ERROR", "Algo deu errado $it")
                    trySend(false).isFailure
                }
            awaitClose {
                listener.isComplete
            }
        }
    }

    override fun signUser(user: User): Flow<Boolean> {
        return callbackFlow {
            val listerner = firestoreCreateUserInstance.signInWithEmailAndPassword(
                user.email.orEmpty(),
                user.password.orEmpty()
            )
                .addOnSuccessListener {
                    trySend(true).isSuccess
                }.addOnFailureListener {
                    Log.e("ERROR", "Algo deu errado $it")
                    trySend(false).isFailure
                }
            awaitClose {
                listerner.isComplete
            }
        }
    }

    override fun searchIdUser(userId: String): Flow<User?> {
        return callbackFlow {
            val listener = firestoreInstance
                .collection("User")
                .document(userId)
                .get()
                .addOnSuccessListener {
                    trySend(it.toObject(User::class.java)).isSuccess
                }.addOnFailureListener {
                    val user: User? = null
                    trySend(user).isFailure
                }
            awaitClose {
                listener.isComplete
            }
        }
    }

    override fun rescuePassWord(email: String): Flow<Boolean> {
        return callbackFlow {
            val listener = firestoreCreateUserInstance
                .sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    trySend(task.isSuccessful).isSuccess
                }.addOnFailureListener {
                    trySend(false).isFailure
                }
            awaitClose {
                listener.isComplete
            }
        }

    }
}