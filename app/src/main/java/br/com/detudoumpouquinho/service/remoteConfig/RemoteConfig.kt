package br.com.detudoumpouquinho.service.remoteConfig

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class RemoteConfig: RemoteConfigContract {

    var mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
    var celularLiveData = MutableLiveData<String>()

    override fun fetchCelular(context: Context?) {
        (context as Activity?)?.let {
            mFirebaseRemoteConfig.fetch()
                .addOnCompleteListener(it) { task -> // If is successful, activated fetched
                    if (task.isSuccessful) {
                        mFirebaseRemoteConfig.activate()
                    } else {
                        Log.d("Error", "Não foi possível buscar dados no remote config")
                    }
                    celularLiveData.value = mFirebaseRemoteConfig.getString("celular")
                }
        }
    }

    override fun fetchCelularListener(): MutableLiveData<String> {
        return celularLiveData
    }

}