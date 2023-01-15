package br.com.detudoumpouquinho.viewModel.remoteConfig

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.detudoumpouquinho.productsUtils.Response
import br.com.detudoumpouquinho.productsUtils.Utils
import br.com.detudoumpouquinho.service.remoteConfig.RemoteConfigContract
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.Exception

class RemoteConfigViewModel(
    var service: RemoteConfigContract
): ViewModel(), RemoteConfigViewModelContract {

    private val _celularLiveData: MutableLiveData<Response<String>> = MutableLiveData()
    var celularLiveData: LiveData<Response<String>> = _celularLiveData

    override fun fetchCelular(context: Context?) {
        service.remoteConfigFetch(context)
            .onEach {
                _celularLiveData.value = Response.SUCCESS(it)
            }.catch {
                _celularLiveData.value = Response.ERROR("")
                Utils.log("Não foi possível encontrar o celular", Exception(it) )
            }.launchIn(viewModelScope)
    }
}