package br.com.detudoumpouquinho.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.productsUtils.Response
import br.com.detudoumpouquinho.databinding.ProductDetailsBinding
import br.com.detudoumpouquinho.model.Product
import br.com.detudoumpouquinho.productsUtils.Utils
import br.com.detudoumpouquinho.view.adapter.ImageAdapter
import br.com.detudoumpouquinho.viewModel.products.ProductsViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.product_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProductDetailsActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModelProducts: ProductsViewModel by viewModel()
    private var position = ""
    private var product: Product? = null
    private lateinit var binding: ProductDetailsBinding
    private var imageAdapter: ImageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.navigationBarColor = resources.getColor(R.color.dark_blue)
        supportActionBar?.hide()

        MobileAds.initialize(this)

        val bundle = intent.extras
        position = bundle?.getString("position").toString()
        viewModelProducts.buscarProdutosId(position)

        loadAds()
        setImageAdapter()
        setViewModel()
        listeners()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_close_product_detail -> {
                val iProductsActivity = Intent(this, ProductsActivity::class.java)
                startActivity(iProductsActivity)
                finish()
            }
            R.id.bt_fazer_pedido -> {
                val sharedPreferences =
                    getSharedPreferences(
                        SignUserActivity.WITHOUT_REGISTRATION,
                        Context.MODE_PRIVATE
                    )
                viewModelProducts.doRequest(sharedPreferences?.getBoolean("semcadastro", false))
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val iProductsActivity = Intent(this, ProductsActivity::class.java)
        startActivity(iProductsActivity)
        finish()
    }

    private fun productNotFound() {
        product?.let {
            binding.apply {
                nestedScrollView.isVisible = true
                viewPager.isVisible = true
                adviewProductDetails.isVisible = true
                btFazerPedido.isVisible = true
                tvProductNotFound.isVisible = false
            }
        } ?: run {
            binding.apply {
                nestedScrollView.isVisible = false
                viewPager.isVisible = false
                adviewProductDetails.isVisible = false
                btFazerPedido.isVisible = false
                tvProductNotFound.isVisible = true
            }
        }
    }

    private fun setImageAdapter() {
        imageAdapter = ImageAdapter(this)
        binding.viewPager.adapter = imageAdapter
        binding.tablayoutImage.setupWithViewPager(viewPager, true)
    }

    private fun listeners() {
        binding.imgCloseProductDetail.setOnClickListener(this)
        binding.btFazerPedido.setOnClickListener(this)
    }


    private fun eventClicked(product: Product) {
        val parameters = Bundle().apply {
            putParcelable("Product_Clicked", product)
        }

        FirebaseAnalytics.getInstance(this).logEvent("product_clicked", parameters)
    }

    private fun setViewModel() {
        binding.lottieProductDetails.visibility = View.VISIBLE
        binding.viewPager.visibility = View.GONE

        viewModelProducts.searchProductIdLiveData.observe(this) {
            when (it) {
                is Response.LOADING -> {
                    binding.lottieProductDetails.visibility = View.VISIBLE
                    binding.viewPager.visibility = View.GONE
                }
                is Response.SUCCESS -> {
                    binding.lottieProductDetails.visibility = View.GONE
                    binding.viewPager.visibility = View.VISIBLE

                    it.data?.image?.forEach { image ->
                        Utils.stringToBitMap(image).also { imageBitmap ->
                            imageBitmap?.let { it1 -> imageAdapter?.setItems(it1) }
                        }
                    }
                    binding.apply {
                        titleProduct.text = it.data?.nameProduct
                        valueProduct.text = "R$ ".plus(it.data?.value)
                        descriptionProductDetails.text = it.data?.description
                        lojista.text = it.data?.seller
                    }

                    product = it.data

                    productNotFound()
                }
                is Response.ERROR -> {
                    Toast.makeText(
                        this,
                        "Houve algum erro ao carregar os detalhes do produto",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        viewModelProducts.isClientRegister.observe(this) {
            val bundle = Bundle()
            if (it != true) {
                val i = Intent(this, CreateNewUserActivity::class.java)
                startActivity(i)
            } else {
                bundle.putParcelable("product", product)

                val bottomSheetDialogFragment = SendRequestProductBottomSheet()
                bottomSheetDialogFragment.arguments = bundle
                bottomSheetDialogFragment.show(supportFragmentManager, "TAG")
            }
        }
    }

    private fun loadAds() {
        val adRequest = AdRequest
            .Builder()
            .build()

        binding.adviewProductDetails.loadAd(adRequest)
    }
}