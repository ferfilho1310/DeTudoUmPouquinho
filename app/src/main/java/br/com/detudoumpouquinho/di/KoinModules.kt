package br.com.detudoumpouquinho.di

import br.com.detudoumpouquinho.service.product.FirebaseServiceProducts
import br.com.detudoumpouquinho.service.product.FirebaseServiceProductsContract
import br.com.detudoumpouquinho.service.remoteConfig.RemoteConfig
import br.com.detudoumpouquinho.service.remoteConfig.RemoteConfigContract
import br.com.detudoumpouquinho.service.user.FirebaseServiceUser
import br.com.detudoumpouquinho.service.user.FirebaseServiceUserContract
import br.com.detudoumpouquinho.view.CreateNewUserActivity
import br.com.detudoumpouquinho.view.ProductsActivity
import br.com.detudoumpouquinho.viewModel.products.ProductsViewModel
import br.com.detudoumpouquinho.viewModel.remoteConfig.RemoteConfigViewModel
import br.com.detudoumpouquinho.viewModel.user.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object KoinModules {

    val viewModel = module {
        viewModel { ProductsViewModel(firebaseServiceProducts = get()) }
        viewModel { UserViewModel(firebaseServiceUserContract = get()) }
        viewModel { RemoteConfigViewModel(service = get())}
    }

    val activitys = module {
        single { ProductsActivity() }
        single { CreateNewUserActivity() }
    }

    val service = module {
        factory<FirebaseServiceUserContract> { FirebaseServiceUser() }
        factory<FirebaseServiceProductsContract> { FirebaseServiceProducts() }
        factory<RemoteConfigContract> { RemoteConfig() }
    }
}
