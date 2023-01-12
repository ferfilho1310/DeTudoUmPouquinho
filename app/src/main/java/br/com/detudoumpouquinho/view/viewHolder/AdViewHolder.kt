package br.com.detudoumpouquinho.view.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.detudoumpouquinho.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class AdViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    val adView = view.findViewById<AdView>(R.id.adview_products)

    fun loadAds() {
        val adRequest = AdRequest
            .Builder()
            .build()

        adView.loadAd(adRequest)
    }
}