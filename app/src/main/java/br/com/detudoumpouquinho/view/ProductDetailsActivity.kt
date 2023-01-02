package br.com.detudoumpouquinho.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.model.Product
import br.com.detudoumpouquinho.productsUtils.Utils
import br.com.detudoumpouquinho.view.adapter.ImageAdapter
import br.com.detudoumpouquinho.viewModel.products.ProductsViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.product_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProductDetailsActivity : AppCompatActivity() {

    val viewModelProducts: ProductsViewModel by viewModel()
    private var position = ""
    var product: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_details)

        window.navigationBarColor = resources.getColor(R.color.dark_blue)
        supportActionBar?.hide()

        MobileAds.initialize(this)
        loadAds()

        val bundle = intent.extras
        position = bundle?.getString("position").toString()
        viewModelProducts.buscarProdutosId(position)

        val imageAdapter = ImageAdapter(this)
        viewPager.adapter = imageAdapter
        tablayout_image.setupWithViewPager(viewPager, true)

        lottie_product_details.visibility = View.VISIBLE
        viewPager.visibility = View.GONE

        viewModelProducts.searchProductIdLiveData.observe(this) {
            lottie_product_details.visibility = View.GONE
            viewPager.visibility = View.VISIBLE
            it?.image?.forEach { image ->
                Utils.stringToBitMap(image).also { imageBitmap ->
                    imageBitmap?.let { it1 -> imageAdapter.setItems(it1) }
                }
            }
            title_product.text = it?.nameProduct
            value_product.text = "R$ ".plus(it?.value)
            description_product_details.text = it?.description
            lojista.text = it?.seller
            product = it
        }

        img_close_product_detail.setOnClickListener {
            finish()
        }

        bt_fazer_pedido.setOnClickListener {
            val sharedPreferences = getSharedPreferences(SignUserActivity.WITHOUT_REGISTRATION, Context.MODE_PRIVATE)
            val bundle = Bundle()
            if (sharedPreferences?.getBoolean("semcadastro", false) != true) {
                val i = Intent(this, CreateNewUserActivity::class.java)
                startActivity(i)
            } else {
                bundle.putParcelable("product", product)

                val bottomSheetDialogFragment = SendRequestProduct()
                bottomSheetDialogFragment.arguments = bundle
                bottomSheetDialogFragment.show(supportFragmentManager, "TAG")
            }
        }
    }

    private fun loadAds() {
        val adRequest = AdRequest
            .Builder()
            .build()

        adview_product_details.loadAd(adRequest)
    }
}