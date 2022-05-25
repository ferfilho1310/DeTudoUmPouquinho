package br.com.detudoumpouquinho.view

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.Utils.PhotosUtils
import br.com.detudoumpouquinho.model.Product
import br.com.detudoumpouquinho.view.adapter.ImageAdapter
import br.com.detudoumpouquinho.viewModel.products.ProductsViewModel
import kotlinx.android.synthetic.main.product_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProductDetails : AppCompatActivity() {

    val viewModelProducts: ProductsViewModel by viewModel()
    private var position = ""
    var product: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_details)
        supportActionBar?.hide()

        val bundle = intent.extras
        position = bundle?.getString("position").toString()
        viewModelProducts.buscarProdutosId(position)

        val imageAdapter = ImageAdapter(this)
        viewPager.adapter = imageAdapter
        tablayout_image.setupWithViewPager(viewPager,true)

        lottie_product_details.visibility = View.VISIBLE
        viewPager.visibility = View.GONE

        viewModelProducts.buscarProdutosIdListener().observe(this) {
            lottie_product_details.visibility = View.GONE
            viewPager.visibility = View.VISIBLE
            it.image?.forEach { image ->
                PhotosUtils.stringToBitMap(image).also { imageBitmap ->
                    imageBitmap?.let { it1 -> imageAdapter.setItems(it1) }
                }
            }
            title_product.text = it.title
            value_product.text = it.value
            description_product_details.text = it.description
            product = it
        }

        img_close_product_detail.setOnClickListener {
            finish()
        }

        bt_fazer_pedido.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("product",product)

            val bottomSheetDialogFragment = SendRequestProduct()
            bottomSheetDialogFragment.isCancelable = false
            bottomSheetDialogFragment.arguments = bundle
            bottomSheetDialogFragment.show(supportFragmentManager, "TAG")
        }
    }
}