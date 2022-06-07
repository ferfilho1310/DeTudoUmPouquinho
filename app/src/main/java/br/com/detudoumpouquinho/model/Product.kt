package br.com.detudoumpouquinho.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val id: String? = null,
    val nameProduct: String? = null,
    val value: String? = null,
    val seller: String? = null,
    val description: String? = null,
    val image: List<String>? = arrayListOf(),
    val valueFrete: String? = null,
    val paymentForm: String? = null
) : Parcelable
