package br.com.detudoumpouquinho.view

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.model.Product
import br.com.detudoumpouquinho.productsUtils.Utils
import br.com.detudoumpouquinho.view.adapter.ProdutosAdapter
import br.com.detudoumpouquinho.viewModel.products.ProductsViewModel
import br.com.detudoumpouquinho.viewModel.user.UserViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.products_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ProductsActivity : AppCompatActivity(), View.OnClickListener {

    private val productsViewModel: ProductsViewModel by viewModel()
    private val userViewModel: UserViewModel by viewModel()
    private var options: FirestoreRecyclerOptions<Product>? = null
    private var adapter: ProdutosAdapter? = null
    private val RECORD_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.products_activity)

        MobileAds.initialize(this)

        setObservers()
        productsViewModel.loadProducts()
        supportActionBar?.hide()

        insert_new_product.setOnClickListener(this)
        img_sair.setOnClickListener(this)
        card_notification.setOnClickListener(this)

        setSearchView()
        loadAds()
    }

    private fun setSearchView() {
        searchView.onActionViewExpanded()
        searchView.clearFocus()
        searchView.queryHint = "Pesquisar"

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

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.insert_new_product -> {
                val bottomSheetDialogFragment = InsertProductBottomFragment()
                bottomSheetDialogFragment.isCancelable = false
                bottomSheetDialogFragment.show(supportFragmentManager, "TAG")
            }
            R.id.img_sair -> {
                Utils.alertDialog(this, "Deseja sair da sua conta ?", ::signOut)
            }
            R.id.card_notification -> {
                val i = Intent(this, CreateNewUserActivity::class.java)
                startActivity(i)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val sharedPreferences =
            getSharedPreferences(SignUserActivity.WITHOUT_REGISTRATION, Context.MODE_PRIVATE)

        if (FirebaseAuth.getInstance().currentUser != null) {
            img_sair.visibility = View.VISIBLE
            tv_sair.visibility = View.VISIBLE
            card_notification.visibility = View.GONE
        } else if (sharedPreferences?.getBoolean("semcadastro", false) == true) {
            img_sair.visibility = View.GONE
            tv_sair.visibility = View.GONE
            card_notification.visibility = View.VISIBLE
        }

        FirebaseAuth.getInstance().currentUser?.let {
            userViewModel.searchIdUser(
                it.uid
            )
        } ?: run {
            insert_new_product.isVisible = false
        }
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut().also {
            val i = Intent(this, SignUserActivity::class.java)
            startActivity(i)
            finish()
        }
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

    private fun permissions() {
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

            adapter = ProdutosAdapter(
                options,
                productsViewModel,
                userViewModel,
                this,
                ::showDetailsProduct
            )

            rc_products.adapter = adapter
            rc_products.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            rc_products.setHasFixedSize(true)
            rc_products.isNestedScrollingEnabled = true

            adapter?.startListening()
        }

        userViewModel.searchIdUserListener().observe(this) {
            if (it.identifier != USER) {
                permissions()
                insert_new_product.isVisible = true
            }
        }
    }

    private fun showDetailsProduct(idProduct: String) {
        val intent = Intent(this, ProductDetails::class.java)
        intent.putExtra("position", idProduct)
        startActivity(intent)
    }

    private fun loadAds() {
        val adRequest = AdRequest
            .Builder()
            .build()

        adview.loadAd(adRequest)
    }

    companion object {
        val USER = "USER"
    }
}