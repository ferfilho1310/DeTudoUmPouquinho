package br.com.detudoumpouquinho.view.viewHolder

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.Utils.PhotosUtils
import br.com.detudoumpouquinho.model.Product
import com.bumptech.glide.Glide

class ProdutosViewHolder(val view: View, val context: Context) : RecyclerView.ViewHolder(view) {

    fun bind(produtos: Product) {
        val img = view.findViewById<ImageView>(R.id.img_produtos_main)
        val valu = view.findViewById<TextView>(R.id.product_value)
        val description = view.findViewById<TextView>(R.id.title)

        produtos.apply {
            Glide.with(context).load(PhotosUtils.stringToBitMap(image?.get(0))).into(img)
            valu.text = value
            description.text = title
        }
    }
}