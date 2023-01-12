package br.com.detudoumpouquinho.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.model.Product
import br.com.detudoumpouquinho.view.viewHolder.AdViewHolder
import br.com.detudoumpouquinho.view.viewHolder.ProdutosViewHolder
import br.com.detudoumpouquinho.viewModel.user.UserViewModel
import kotlinx.android.synthetic.main.products_item_view_holder.view.*

class ProdutosAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var productsListFiltered: ArrayList<Product> = ArrayList()
    var products: ArrayList<Product> = arrayListOf()
    var listener: ProductsListener? = null
    var userViewModel: UserViewModel? = null
    var context: Context? = null

    fun addProducts(
        list: ArrayList<Product>,
        userViewModel: UserViewModel,
        context: Context,
        listener: ProductsListener
    ) {
        products.clear()
        products.addAll(list)
        productsListFiltered = list
        this.userViewModel = userViewModel
        this.listener = listener
        this.context = context
    }

    fun filterList(list: ArrayList<Product>) {
        products = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProdutosViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.products_item_view_holder, parent, false),
                parent.context
            )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProdutosViewHolder -> {
                holder.apply {
                    imageProduct(products[position])
                    deleteAndUpdateIsVisible(userViewModel)
                }

                holder.view.delete_products.setOnClickListener {
                    listener?.deleteProduct(products[position].id)
                }

                holder.view.edit_products.setOnClickListener {
                    listener?.editProduct(products[position].id)
                }

                holder.view.setOnClickListener {
                    listener?.clickProduct(products[position].id)
                }
            }
        }
    }

    override fun getItemCount() = products.size

    interface ProductsListener {
        fun deleteProduct(productId: String?)
        fun clickProduct(productId: String?)
        fun editProduct(productId: String?)
    }
}