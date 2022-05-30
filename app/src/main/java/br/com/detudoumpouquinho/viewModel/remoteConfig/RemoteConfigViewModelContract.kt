package br.com.detudoumpouquinho.viewModel.remoteConfig

import android.content.Context
import androidx.lifecycle.MutableLiveData

interface RemoteConfigViewModelContract {
    fun fetchCelular(context: Context?)
    fun fetchCelularListener(): MutableLiveData<String>
}