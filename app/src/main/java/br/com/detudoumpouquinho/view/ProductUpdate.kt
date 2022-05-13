package br.com.detudoumpouquinho.view

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.Utils.PhotosUtils
import br.com.detudoumpouquinho.model.Product
import br.com.detudoumpouquinho.view.adapter.FotosAdapter
import br.com.detudoumpouquinho.viewModel.products.ProductsViewModel
import kotlinx.android.synthetic.main.product_updat_view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductUpdate : AppCompatActivity(), View.OnClickListener {

    private val adapter by lazy { FotosAdapter() }
    private val productsViewModel: ProductsViewModel by viewModel()
    private var position = ""
    private val photos: ArrayList<String> = arrayListOf()
    private val CAMERA_REQUEST = 1888

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_updat_view)

        val bundle = intent.extras
        position = bundle?.getString("position").toString()
        productsViewModel.buscarProdutosId(position)

        listeners()
        setObserver()

        productsViewModel.buscarProdutosIdListener().observe(this) { product ->
            product?.let {
                it.image?.forEach { image ->
                    adapter.listFotos(image)
                    photos.add(image)
                }
                title_updated.setText(it.title)
                subtitle_updated.setText(it.subtitle)
                description_update.setText(it.description)
                value_update.setText(it.value)
            }
        }

        rc_products_imagens_update.adapter = adapter
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rc_products_imagens_update.layoutManager = layoutManager
        rc_products_imagens_update.setHasFixedSize(true)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.fb_products_take_photo_update -> {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            }
            R.id.bt_update_procut -> {
                when {
                    title_updated.text.toString().isEmpty() -> {
                        title_updated.error = "Digite o titulo"
                    }
                    subtitle_updated.text.toString().isEmpty() -> {
                        subtitle_updated.error = "Digite o subtitulo"
                    }
                    else -> {
                        val produto = Product(
                            title = title_updated.text.toString(),
                            subtitle = subtitle_updated.text.toString(),
                            image = photos,
                            value = value_update.text.toString(),
                            description = description_update.text.toString()
                        )
                        productsViewModel.updateProduct(position, produto)
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    try {
                        adapter.listFotos(
                            PhotosUtils.uriToBitmap(data!!, contentResolver).orEmpty()
                        )
                        PhotosUtils.uriToBitmap(data, contentResolver)
                            .let { photos.add(it!!) }

                    } catch (e: Exception) {
                        Toast.makeText(this, "Picture Not taken $e", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                RESULT_CANCELED -> {
                    Toast.makeText(this, "Picture was not taken 1 ", Toast.LENGTH_SHORT)
                        .show();
                }
                else -> {
                    Toast.makeText(this, "Picture was not taken 2 ", Toast.LENGTH_SHORT)
                        .show();
                }
            }
        }
    }

    private fun listeners() {
        fb_products_take_photo_update.setOnClickListener(this)
        bt_update_procut.setOnClickListener(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        intentProductActivity()
    }

    private fun setObserver() {
        productsViewModel.updateProductListener().observe(this) {
            if (it == true) {
                intentProductActivity()
                Toast.makeText(this, "Produto atualizado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Falha ao atualizar produto", Toast.LENGTH_SHORT)
                    .show();
            }
        }
    }

    private fun intentProductActivity() {
        val intent = Intent(this, ProductsActivity::class.java)
        startActivity(intent)
        finish()
    }
}