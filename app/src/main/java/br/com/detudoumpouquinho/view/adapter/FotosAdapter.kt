package br.com.detudoumpouquinho.view.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.model.Photos
import br.com.detudoumpouquinho.view.viewHolder.FotosViewHolder
import kotlinx.android.synthetic.main.fotos_item_view_holder.view.*

class FotosAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listFotos: ArrayList<String> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun listFotos(foto: String) {
        listFotos.add(foto)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FotosViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fotos_item_view_holder, parent, false),
            parent.context
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderFotos = holder as FotosViewHolder
        holderFotos.bindFotos(listFotos[position])
        holderFotos.view.img_remove_foto.setOnClickListener {
            removeFoto(holder.adapterPosition)
        }
    }

    override fun getItemCount() = listFotos.size

    @SuppressLint("NotifyDataSetChanged")
    private fun removeFoto(position: Int){
        listFotos.removeAt(position)
       notifyDataSetChanged()
    }
}