package br.com.detudoumpouquinho.service.product

import androidx.lifecycle.MutableLiveData
import br.com.detudoumpouquinho.model.Product
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

class FirebaseServiceProducts : FirebaseServiceProductsContract {

    private val firestoreInstance = FirebaseFirestore.getInstance()

    private val createNewProductListener by lazy { MutableLiveData<Boolean>() }
    private val loadNewProductListener by lazy { MutableLiveData<Query>() }
    private val deleteProduct by lazy { MutableLiveData<Boolean>() }
    private val updateProduct by lazy { MutableLiveData<Boolean>() }
    private val buscarProducoIdListener by lazy { MutableLiveData<Product>() }

    var query: Query? = null

    override fun insertNewProduct(product: Product) {
        val map: MutableMap<String, Any?> = HashMap()

        product.let {
            map["description"] = it.description
            map["image"] = it.image
            map["seller"] = it.seller
            map["nameProduct"] = it.nameProduct
            map["value"] = it.value
            map["nameProductUpperCase"] = it.nameProduct?.uppercase()
            map["valueFrete"] = it.valueFrete
            map["paymentForm"] = it.paymentForm
        }

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
        ).endAt(nomeProduto.uppercase(Locale.getDefault()) + "\uf8ff")

        loadNewProductListener.value = query
    }

    override fun buscarProdutosListener(): MutableLiveData<Query> {
        return loadNewProductListener
    }

    override fun updateProduct(documentId: String, model: Product) {
        val map: MutableMap<String, Any?> = HashMap()

        model.apply {
            map["description"] = description
            map["image"] = image
            map["subtitle"] = seller
            map["title"] = nameProduct
            map["value"] = value
            map["titleUppercase"] = nameProduct?.uppercase()
            map["valueFrete"] = valueFrete
            map["paymentForm"] = paymentForm
        }

        firestoreInstance
            .collection("Products")
            .document(documentId)
            .update(map)
            .addOnSuccessListener {
                updateProduct.value = true
            }
            .addOnFailureListener {
                updateProduct.value = false
            }
    }

    override fun updateProductListener(): MutableLiveData<Boolean> {
        return updateProduct
    }

    override fun buscarProdutosId(idProducto: String) {
        firestoreInstance
            .collection("Products")
            .document(idProducto)
            .get()
            .addOnSuccessListener {
                buscarProducoIdListener.value = it.toObject(Product::class.java)
            }.addOnFailureListener {
                val product: Product? = null
                buscarProducoIdListener.value = product
            }
    }

    override fun buscarProdutosIdListener(): MutableLiveData<Product> {
        return buscarProducoIdListener
    }
}