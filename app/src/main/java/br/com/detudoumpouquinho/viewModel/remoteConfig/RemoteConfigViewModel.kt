package br.com.detudoumpouquinho.viewModel.remoteConfig

import android.content.Context
import androidx.lifecycle.ViewModel
import br.com.detudoumpouquinho.service.remoteConfig.RemoteConfigContract

class RemoteConfigViewModel(
    var service: RemoteConfigContract
): ViewModel(), RemoteConfigViewModelContract {

    override fun fetchCelular(context: Context?) {
        service.fetchCelular(context)
    }

    override fun fetchCelularListener() = service.fetchCelularListener()
}