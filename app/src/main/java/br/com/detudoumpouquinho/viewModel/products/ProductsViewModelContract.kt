package br.com.detudoumpouquinho.viewModel.products

import br.com.detudoumpouquinho.model.Products

interface ProductsViewModelContract {
    fun insertProduct(products: Products)
}