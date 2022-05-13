package br.com.detudoumpouquinho.view.viewHolder

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.Utils.PhotosUtils
import com.bumptech.glide.Glide


class FotosViewHolder(val view: View, val context: Context) : RecyclerView.ViewHolder(view) {

    fun bindFotos(fotos: String) {
        val imgFotos = view.findViewById<ImageView>(R.id.img_produtos_fotos)
        Glide.with(context).load(PhotosUtils.stringToBitMap(fotos)).into(imgFotos)
    }
}