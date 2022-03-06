package br.com.detudoumpouquinho.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.viewModel.ProductsViewModel

class ProductsActivity : AppCompatActivity() {

    private val productsViewModel: ProductsViewModel = ProductsViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.products_activity)

        //productsViewModel.insertProduct()
        setObservers()
    }

    private fun setObservers() {
        productsViewModel.productsLiveData.observe(this) {
            if (it == true) {
                //TODO: Navigate other screen
            } else {
                Toast.makeText(this, "Erro ao cadastrar os produtos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}