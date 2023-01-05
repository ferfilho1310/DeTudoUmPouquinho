package br.com.detudoumpouquinho.service.remoteConfig

import android.app.Activity
import android.content.Context
import android.util.Log
import br.com.detudoumpouquinho.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RemoteConfigService: RemoteConfigContract {

    val frc = FirebaseRemoteConfig.getInstance()

    override fun remoteConfigFetch(context: Context?): Flow<String> {
        return callbackFlow {
            frc.setDefaultsAsync(R.xml.remote_config_defaults)
            val listener = frc.fetch(30)
                .addOnCompleteListener(context as Activity) { task ->
                    if (task.isSuccessful) {
                        frc.fetchAndActivate()
                        trySend(frc.getString("celular")).isSuccess
                    } else {
                        trySend("").isFailure
                        Log.e("Error", "Erro ao fazer o fetch no remote config ${task.exception}")
                    }
                }
            awaitClose {
                listener.isCanceled
            }
        }
    }

}