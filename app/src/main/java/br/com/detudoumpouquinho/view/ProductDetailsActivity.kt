package br.com.detudoumpouquinho.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.databinding.ProductDetailsBinding
import br.com.detudoumpouquinho.model.Product
import br.com.detudoumpouquinho.productsUtils.Utils
import br.com.detudoumpouquinho.view.adapter.ImageAdapter
import br.com.detudoumpouquinho.viewModel.products.ProductsViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
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

    private fun setImageAdapter() {
        imageAdapter = ImageAdapter(this)
        binding.viewPager.adapter = imageAdapter
        binding.tablayoutImage.setupWithViewPager(viewPager, true)
    }

    private fun listeners() {
        binding.imgCloseProductDetail.setOnClickListener(this)
        binding.btFazerPedido.setOnClickListener(this)
    }

    private fun setViewModel() {
        binding.lottieProductDetails.visibility = View.VISIBLE
        binding.viewPager.visibility = View.GONE

        viewModelProducts.searchProductIdLiveData.observe(this) {

            binding.lottieProductDetails.visibility = View.GONE
            binding.viewPager.visibility = View.VISIBLE

            it?.image?.forEach { image ->
                Utils.stringToBitMap(image).also { imageBitmap ->
                    imageBitmap?.let { it1 -> imageAdapter?.setItems(it1) }
                }
            }
            binding.apply {
                titleProduct.text = it?.nameProduct
                valueProduct.text = "R$ ".plus(it?.value)
                descriptionProductDetails.text = it?.description
                lojista.text = it?.seller
            }
            product = it
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