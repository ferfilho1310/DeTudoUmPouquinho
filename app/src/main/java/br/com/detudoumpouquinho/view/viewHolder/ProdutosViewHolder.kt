package br.com.detudoumpouquinho.view.viewHolder

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.productsUtils.Utils
import br.com.detudoumpouquinho.model.Product
import br.com.detudoumpouquinho.view.ProductsActivity.Companion.USER
import br.com.detudoumpouquinho.viewModel.user.UserViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ProdutosViewHolder(val view: View, val context: Context) : RecyclerView.ViewHolder(view) {

    val imgProduct = view.findViewById<ImageView>(R.id.img_produtos_main)
    val valueProduct = view.findViewById<TextView>(R.id.product_value)
    val title = view.findViewById<TextView>(R.id.title)
    val paymentFormView = view.findViewById<TextView>(R.id.payment_form)

    fun imageProduct(product: Product) {
        product.apply {
            Glide.with(context)
                .load(Utils.stringToBitMap(image?.get(0)))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .centerCrop()
                .into(imgProduct)
            valueProduct.text = SIMBOL.REAIS.plus(value)
            title.text = nameProduct

            if(!paymentForm.isNullOrEmpty()){
                paymentFormView.text = paymentForm
                paymentFormView.isVisible = true
            }
        }
    }

    fun deleteAndUpdateIsVisible(userViewModel: UserViewModel?) {
        userViewModel?.searchIdUser?.observe(context as LifecycleOwner) {
            if (it.identifier != USER) {
                view.findViewById<LinearLayout>(R.id.linearLayout5).isVisible = true
            }
        }
    }

    object SIMBOL {
        const val REAIS = "R$"
    }
}