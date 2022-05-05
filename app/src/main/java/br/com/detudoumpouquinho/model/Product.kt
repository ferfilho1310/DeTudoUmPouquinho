package br.com.detudoumpouquinho.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val id: String? = null,
    val title: String? = null,
    val value: String? = null,
    val subtitle: String? = null,
    val description: String? = null,
    val image: List<String>? = arrayListOf()
) : Parcelable
