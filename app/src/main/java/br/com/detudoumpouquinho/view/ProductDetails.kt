package br.com.detudoumpouquinho.view

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.Utils.PhotosUtils
import br.com.detudoumpouquinho.view.adapter.ImageAdapter
import br.com.detudoumpouquinho.viewModel.products.ProductsViewModel
import kotlinx.android.synthetic.main.product_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProductDetails : AppCompatActivity() {

    val viewModelProducts: ProductsViewModel by viewModel()
    private var position = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_details)
        supportActionBar?.hide()

        val bundle = intent.extras
        position = bundle?.getString("position").toString()
        viewModelProducts.buscarProdutosId(position)

        val imageAdapter = ImageAdapter(this)
        viewPager.adapter = imageAdapter
        tablayout_image.setupWithViewPager(viewPager)

        viewModelProducts.buscarProdutosIdListener().observe(this) {
            it.image?.forEach { image ->
                PhotosUtils.stringToBitMap(image).also { imageBitmap ->
                    imageBitmap?.let { it1 -> imageAdapter.setItems(it1) }
                }
            }
        }

        img_close_product_detail.setOnClickListener {
            finish()
        }
    }
}