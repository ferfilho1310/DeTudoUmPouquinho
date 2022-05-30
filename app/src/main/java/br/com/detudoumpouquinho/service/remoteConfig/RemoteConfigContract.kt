package br.com.detudoumpouquinho.service.remoteConfig

import android.content.Context
import androidx.lifecycle.MutableLiveData

interface RemoteConfigContract {
    fun fetchCelular(context: Context?)
    fun fetchCelularListener(): MutableLiveData<String>
}