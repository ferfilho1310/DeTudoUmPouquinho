package br.com.detudoumpouquinho.view

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.databinding.ProductsActivityBinding
import br.com.detudoumpouquinho.model.Product
import br.com.detudoumpouquinho.view.adapter.ProdutosAdapter
import br.com.detudoumpouquinho.viewModel.products.ProductsViewModel
import br.com.detudoumpouquinho.viewModel.user.UserViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.products_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductsActivity : AppCompatActivity(), View.OnClickListener {

    private val productsViewModel: ProductsViewModel by viewModel()
    private val userViewModel: UserViewModel by viewModel()
    private var options: FirestoreRecyclerOptions<Product>? = null
    private var productsAdapter: ProdutosAdapter? = null
    private val RECORD_REQUEST_CODE = 101
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var binding: ProductsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProductsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        firebaseAnalytics = Firebase.analytics
        window.navigationBarColor = resources.getColor(R.color.dark_blue)
        MobileAds.initialize(this)

        productsViewModel.loadProducts()

        binding.insertNewProduct.setOnClickListener(this)
        binding.imgProfile.setOnClickListener(this)

        setObservers()
        setSearchView()
        loadAds()
        setViewModel()
    }

    private fun setSearchView() {
        binding.searchView.apply {
            onActionViewExpanded()
            clearFocus()
            queryHint = SEARCH

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(p0: String?): Boolean {
                    productsViewModel.searchProduct(p0.orEmpty())
                    return false
                }

                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }
            })
        }

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.insert_new_product -> {
                val bottomSheetDialogFragment = InsertProductBottomFragment()
                bottomSheetDialogFragment.isCancelable = false
                bottomSheetDialogFragment.show(supportFragmentManager, "TAG")
            }
            R.id.img_profile -> {
                val bottomSheetDialogFragment = ProfileBottomFragment()
                bottomSheetDialogFragment.show(supportFragmentManager, "TAG")
            }
        }
    }

    override fun onStart() {
        super.onStart()

        FirebaseAuth.getInstance().currentUser?.let {
            userViewModel.searchIdUser(
                it.uid
            )
        } ?: run {
            binding.insertNewProduct.isVisible = false
        }

        if (productsAdapter != null) {
            productsAdapter!!.startListening()
        }
    }

    override fun onStop() {
        super.onStop()
        if (productsAdapter != null) {
            productsAdapter!!.stopListening()
        }
    }

    override fun onResume() {
        super.onResume()

        productsViewModel.loadProducts()

        if(productsAdapter != null) {
            productsAdapter!!.startListening()
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
        productsViewModel.loadProductLiveData.observe(this) {
            options = FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(it, Product::class.java)
                .build()

            productsAdapter = ProdutosAdapter(
                options,
                userViewModel,
                this,
                object : ProdutosAdapter.ProductsListener {
                    override fun deleteProduct(productId: DocumentReference) {
                        productsViewModel.deleteProduct(productId)
                    }

                    override fun clickProduct(productId: String) {
                        showDetailsProduct(productId)
                    }

                    override fun editProduct(productId: String) {
                        updateProduct(productId)
                    }
                }
            )

            binding.rcProducts.apply {
                adapter = productsAdapter
                layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                setHasFixedSize(true)
                isNestedScrollingEnabled = true
            }

            productsAdapter?.startListening()
        }
    }

    private fun setViewModel() {
        userViewModel.searchIdUser.observe(this) {
            if (it.identifier != USER) {
                permissions()
                insert_new_product.isVisible = true
            }
        }

        productsViewModel.deleteProductLiveData.observe(this) {
            if (it == true) {
                Toast.makeText(this, "Produto deletado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao deletar produto", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun showDetailsProduct(productId: String) {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra("position", productId)
        startActivity(intent)
    }

    private fun updateProduct(productId: String) {
        val intent = Intent(this, ProductUpdateActivity::class.java)
        intent.putExtra("position", productId)
        startActivity(intent)
    }

    private fun loadAds() {
        val adRequest = AdRequest
            .Builder()
            .build()

        adview.loadAd(adRequest)
    }

    companion object {
        const val USER = "USER"
        const val SEARCH = "Pesquisar"
    }
}