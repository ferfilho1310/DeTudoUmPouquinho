package br.com.detudoumpouquinho.view

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.model.Product
import br.com.detudoumpouquinho.view.adapter.ProdutosAdapter
import br.com.detudoumpouquinho.viewModel.products.ProductsViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.products_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductsActivity : AppCompatActivity(), View.OnClickListener {

    private val productsViewModel: ProductsViewModel by viewModel()
    private var options: FirestoreRecyclerOptions<Product>? = null
    private var adapter: ProdutosAdapter? = null
    private val RECORD_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.products_activity)
        permissions()

        supportActionBar?.hide()

        insert_new_product.setOnClickListener(this)
        productsViewModel.loadProducts()
        setObservers()

        searchView.onActionViewExpanded();
        searchView.clearFocus()
        searchView.queryHint = "Pesquisar produto"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(p0: String?): Boolean {
                productsViewModel.searchProduct(p0.orEmpty())
                return false
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }
        })
    }

    private fun makeRequestRead() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(READ_EXTERNAL_STORAGE),
            RECORD_REQUEST_CODE
        )
    }

    private fun makeRequestWrite() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(WRITE_EXTERNAL_STORAGE),
            RECORD_REQUEST_CODE
        )
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.insert_new_product -> {
                val bottomSheetDialogFragment = InsertProductBottomFragment()
                bottomSheetDialogFragment.show(supportFragmentManager, "TAG")
            }
        }
    }

    private  fun permissions() {
        val permissionWrite = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
        val permissionRead = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)

        if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
            makeRequestWrite()
        }

        if (permissionRead != PackageManager.PERMISSION_GRANTED) {
            makeRequestRead()
        }
    }

    private fun setObservers() {
        productsViewModel.loadProdutsListener().observe(this) {
            options = FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(it, Product::class.java)
                .build()

            adapter = ProdutosAdapter(options, productsViewModel, this)

            rc_products.adapter = adapter
            rc_products.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            rc_products.setHasFixedSize(true)

            adapter?.startListening()
        }
    }
}