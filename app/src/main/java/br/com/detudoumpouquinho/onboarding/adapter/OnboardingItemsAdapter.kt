package br.com.detudoumpouquinho.onboarding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.onboarding.model.OnboardingItem
import br.com.detudoumpouquinho.onboarding.viewHolder.OnboardigItemsViewHolder

class OnboardingItemsAdapter(val onboardingItem: List<OnboardingItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OnboardigItemsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.onboarding_item_container,
                    parent,
                    false
                ),
            parent.context
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderItems = holder as OnboardigItemsViewHolder
        holderItems.bind(onboardingItem[position])
    }

    override fun getItemCount(): Int {
        return onboardingItem.size
    }
}