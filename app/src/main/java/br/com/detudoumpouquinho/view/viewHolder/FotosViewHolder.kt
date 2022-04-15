package br.com.detudoumpouquinho.view.viewHolder

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.Utils.PhotosUtils
import br.com.detudoumpouquinho.model.Photos

class FotosViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindFotos(fotos: String) {
        val imgFotos = view.findViewById<ImageView>(R.id.img_produtos_fotos)
        imgFotos.setImageBitmap(PhotosUtils.stringToBitMap(fotos))
    }
}