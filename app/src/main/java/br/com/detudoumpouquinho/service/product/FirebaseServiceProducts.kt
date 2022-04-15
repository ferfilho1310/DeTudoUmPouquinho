package br.com.detudoumpouquinho.service.product

import android.util.Log
import androidx.lifecycle.MutableLiveData
import br.com.detudoumpouquinho.model.Product
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import java.util.*

class FirebaseServiceProducts : FirebaseServiceProductsContract {

    private val firestoreInstance = FirebaseFirestore.getInstance()

    private val createNewProductListener: MutableLiveData<Boolean> = MutableLiveData()
    private val loadNewProductListener: MutableLiveData<Query> = MutableLiveData()
    private val deleteProduct: MutableLiveData<Boolean> = MutableLiveData()

    var query: Query? = null

    override fun insertNewProduct(product: Product) {
        val map: MutableMap<String, Any?> = HashMap()

        map["description"] = product.description
        map["image"] = product.image
        map["subtitle"] = product.subtitle
        map["title"] = product.title
        map["value"] = product.value
        map["titleUppercase"] = product.title?.uppercase()

        firestoreInstance
            .collection("Products")
            .add(map)
            .addOnSuccessListener {
                createNewProductListener.value = true
            }.addOnFailureListener {
                createNewProductListener.value = false
            }
    }


    override fun insertNewProductListener() = createNewProductListener

    override fun loadProducts() {

        query = firestoreInstance.collection("Products")

/*
        firestoreInstance.collection("Products").addSnapshotListener { value, _ ->
            val list: ArrayList<Product> = ArrayList()
            value?.documents?.forEach {
                val products = it.data
                list.add(
                    Product(
                        title = products?.get("title").toString(),
                        value = products?.get("value").toString(),
                        subtitle = products?.get("subtitle").toString(),
                        description = products?.get("description").toString(),
                        image = products?.get("image") as List<String>
                    )
                )
            }*/
        loadNewProductListener.value = query
    }

    override fun loadProductsListener(): MutableLiveData<Query> {
        return loadNewProductListener
    }

    override fun deleteProduct(documentId: DocumentReference) {
        firestoreInstance
            .collection("Products")
            .document(documentId.id)
            .delete()
            .addOnSuccessListener {
                deleteProduct.value = true
            }
            .addOnFailureListener {
                deleteProduct.value = false
            }
    }

    override fun deleteProductListener(): MutableLiveData<Boolean> {
        return deleteProduct
    }

    override fun buscarProdutos(nomeProduto: String) {
        query = firestoreInstance.collection("Products").orderBy("titleUppercase").startAt(
            nomeProduto.uppercase(
                Locale.getDefault()
            )
        ).endAt(nomeProduto.uppercase(Locale.getDefault()) + "\uf8ff");

        loadNewProductListener.value = query
    }

    override fun buscarProdutosListener(): MutableLiveData<Query> {
        return loadNewProductListener
    }
}