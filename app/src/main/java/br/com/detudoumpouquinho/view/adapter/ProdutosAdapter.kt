package br.com.detudoumpouquinho.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.model.Product
import br.com.detudoumpouquinho.productsUtils.Utils.alertDialog
import br.com.detudoumpouquinho.view.ProductUpdate
import br.com.detudoumpouquinho.view.viewHolder.ProdutosViewHolder
import br.com.detudoumpouquinho.viewModel.products.ProductsViewModel
import br.com.detudoumpouquinho.viewModel.user.UserViewModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.products_item_view_holder.view.*

class ProdutosAdapter(
    options: FirestoreRecyclerOptions<Product>?,
    var viewModel: ProductsViewModel,
    val userViewModel: UserViewModel,
    var context: Context,
    val listener: (String) -> Unit
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
        viewHolder.bind(model, userViewModel)

        viewHolder.view.delete_products.setOnClickListener {
            alertDialog(context, "Deseja realmente deletar este produto?") {
                deleteProduto(viewHolder.adapterPosition)
            }
        }

        viewHolder.view.edit_products.setOnClickListener {
            alertDialog(context, "Deseja realmente editar os dados deste produto?") {
                val intent = Intent(context, ProductUpdate::class.java)
                intent.putExtra("position", snapshots.getSnapshot(position).reference.id)
                context.startActivity(intent)
            }
        }

        viewHolder.view.setOnClickListener {
            listener.invoke(snapshots.getSnapshot(position).reference.id)
        }
    }

    private fun deleteProduto(position: Int) {
        val product = snapshots.getSnapshot(position).reference
        viewModel.deleteProduct(product)
        viewModel.deleteProductListener().observe(context as LifecycleOwner) {
            if (it == true) {
                Toast.makeText(context, "Produto deletado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Erro ao deletar produto", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}