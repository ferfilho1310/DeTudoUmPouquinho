package br.com.detudoumpouquinho.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class KoinApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KoinApplication)
            androidLogger(Level.NONE)
            androidFileProperties()
            modules(
                listOf(
                    KoinModules.viewModel,
                    KoinModules.activitys,
                    KoinModules.service
                )
            )
        }

    }

}