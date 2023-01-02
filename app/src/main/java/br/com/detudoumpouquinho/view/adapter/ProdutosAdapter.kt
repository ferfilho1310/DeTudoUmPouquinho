package br.com.detudoumpouquinho.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.model.Product
import br.com.detudoumpouquinho.view.viewHolder.ProdutosViewHolder
import br.com.detudoumpouquinho.viewModel.user.UserViewModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.synthetic.main.products_item_view_holder.view.*

class ProdutosAdapter(
    options: FirestoreRecyclerOptions<Product>?,
    val userViewModel: UserViewModel,
    var context: Context,
    val listener: ProductsListener
) : FirestoreRecyclerAdapter<Product, RecyclerView.ViewHolder>(options!!) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProdutosViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.products_item_view_holder, parent, false),
            parent.context
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, model: Product) {
        val viewHolder = holder as ProdutosViewHolder
        viewHolder.apply {
            imageProduct(model)
            deleteAndUpdateIsVisible(userViewModel)
        }

        viewHolder.view.delete_products.setOnClickListener {
            listener.deleteProduct(snapshots.getSnapshot(viewHolder.adapterPosition).reference)
        }

        viewHolder.view.edit_products.setOnClickListener {
            listener.editProduct(snapshots.getSnapshot(position).reference.id)
        }

        viewHolder.view.setOnClickListener {
            listener.clickProduct(snapshots.getSnapshot(position).reference.id)
        }
    }

    interface ProductsListener {
        fun deleteProduct(productId: DocumentReference)
        fun clickProduct(productId: String)
        fun editProduct(productId: String)
    }
}