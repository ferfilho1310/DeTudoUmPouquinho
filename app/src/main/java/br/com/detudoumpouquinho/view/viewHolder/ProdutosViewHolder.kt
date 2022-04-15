package br.com.detudoumpouquinho.view.viewHolder

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.Utils.PhotosUtils
import br.com.detudoumpouquinho.model.Product
import br.com.detudoumpouquinho.view.adapter.ProdutosAdapter
import com.google.firebase.firestore.FirebaseFirestore

class ProdutosViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(produtos: Product) {
        val img = view.findViewById<ImageView>(R.id.img_produtos_main)
        val valu = view.findViewById<TextView>(R.id.value)
        val description = view.findViewById<TextView>(R.id.title)

        produtos.apply {
            img.setImageBitmap(PhotosUtils.stringToBitMap(image?.get(0)))
            valu.text = value
            description.text = title
        }
    }
}