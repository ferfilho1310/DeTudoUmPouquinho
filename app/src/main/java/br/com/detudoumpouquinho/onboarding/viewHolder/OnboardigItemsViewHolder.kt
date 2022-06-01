package br.com.detudoumpouquinho.onboarding.viewHolder

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.onboarding.model.OnboardingItem
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide

class OnboardigItemsViewHolder(val view: View, val context: Context) :
    RecyclerView.ViewHolder(view) {

    private val image = view.findViewById<LottieAnimationView>(R.id.img_onboarding)
    private val description = view.findViewById<TextView>(R.id.textDescription)

    fun bind(onboardingItem: OnboardingItem) {
        image.setAnimationFromUrl(onboardingItem.onboardingImage)
        description.text = onboardingItem.description
    }
}