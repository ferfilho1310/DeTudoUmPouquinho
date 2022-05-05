package br.com.detudoumpouquinho.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
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
import kotlinx.android.synthetic.main.activity_insert_product.*
import kotlinx.android.synthetic.main.product_updat_view.*
import kotlinx.android.synthetic.main.products_item_view_holder.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProducUpdate : AppCompatActivity(), View.OnClickListener {

    private val adapter by lazy { FotosAdapter() }
    private val productsViewModel: ProductsViewModel by viewModel()
    private var position = ""
    private val photos: ArrayList<String> = arrayListOf()
    private val CAMERA_REQUEST = 1888

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_updat_view)

        val bundle = intent.extras
        val model = bundle?.getParcelable<Product>("produto")
        position = bundle?.getString("position").toString()

        listeners()
        setObserver()

        title_updated.setText(model?.title)
        subtitle_updated.setText(model?.subtitle)
        description_update.setText(model?.description)

        rc_products_imagens_update.adapter = adapter
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rc_products_imagens_update.layoutManager = layoutManager
        rc_products_imagens_update.setHasFixedSize(true)

        model?.image?.forEach {
            adapter.listFotos(it)
            photos.add(it)
        }
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
                        val pic: Bitmap? = data?.getParcelableExtra("data")
                        adapter.listFotos(
                            PhotosUtils.bitMapToString(pic).orEmpty()
                        )
                        photos.add(PhotosUtils.bitMapToString(pic).orEmpty())

                    } catch (e: Exception) {
                        Toast.makeText(this, "Picture Not taken", Toast.LENGTH_LONG)
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
        finish()
    }

    private fun setObserver() {
        productsViewModel.updateProductListener().observe(this) {
            if (it == true) {
                finish()
                Toast.makeText(this, "Producto atualizado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Falha ao atualizar produto", Toast.LENGTH_SHORT)
                    .show();
            }
        }
    }
}