package br.com.detudoumpouquinho.view.viewHolder

import android.content.Context
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.Utils.PhotosUtils
import br.com.detudoumpouquinho.model.Product
import br.com.detudoumpouquinho.view.ProductsActivity
import br.com.detudoumpouquinho.viewModel.user.UserViewModel
import com.bumptech.glide.Glide

class ProdutosViewHolder(val view: View, val context: Context) : RecyclerView.ViewHolder(view) {

    fun bind(produtos: Product, viewModel: UserViewModel) {
        val img = view.findViewById<ImageView>(R.id.img_produtos_main)
        val valu = view.findViewById<TextView>(R.id.product_value)
        val description = view.findViewById<TextView>(R.id.title)
        val delete = view.findViewById<ImageButton>(R.id.delete_products)
        val update = view.findViewById<ImageButton>(R.id.edit_products)
        val paymentFormView = view.findViewById<TextView>(R.id.payment_form)

        viewModel.searchIdUserListener().observe(context as LifecycleOwner) {
            if(it.identifier != ProductsActivity.USER){
                delete.isVisible = true
                update.isVisible = true
            }
        }

        produtos.apply {
            Glide.with(context).load(PhotosUtils.stringToBitMap(image?.get(0))).into(img)
            valu.text = value
            description.text = title
            paymentFormView.text = paymentForm
        }
    }
}