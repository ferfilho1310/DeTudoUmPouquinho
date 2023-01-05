package br.com.detudoumpouquinho.service.remoteConfig

import android.content.Context
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow

interface RemoteConfigContract {
    fun remoteConfigFetch(context: Context?): Flow<String>
}