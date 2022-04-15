package br.com.detudoumpouquinho.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.model.Photos
import br.com.detudoumpouquinho.view.viewHolder.FotosViewHolder

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
                .inflate(R.layout.fotos_item_view_holder, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderFotos = holder as FotosViewHolder
        holderFotos.bindFotos(listFotos[position])
    }

    override fun getItemCount() = listFotos.size
}