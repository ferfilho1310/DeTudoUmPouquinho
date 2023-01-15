package br.com.detudoumpouquinho.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.productsUtils.Response
import br.com.detudoumpouquinho.databinding.ProductUpdateActivityBinding
import br.com.detudoumpouquinho.productsUtils.Utils
import br.com.detudoumpouquinho.model.Product
import br.com.detudoumpouquinho.view.adapter.FotosAdapter
import br.com.detudoumpouquinho.viewModel.products.ProductsViewModel
import kotlinx.android.synthetic.main.product_update_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductUpdateActivity : AppCompatActivity(), View.OnClickListener {

    private val adapter by lazy { FotosAdapter() }
    private val productsViewModel: ProductsViewModel by viewModel()
    private var position = ""
    private val photos: ArrayList<String> = arrayListOf()
    private val CAMERA_REQUEST = 1888

    private lateinit var binding: ProductUpdateActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProductUpdateActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val bundle = intent.extras
        position = bundle?.getString("position").toString()

        productsViewModel.buscarProdutosId(position)

        listeners()
        setObserver()
        setViewModel()
        setAdapter()
    }

    private fun setViewModel() {
        productsViewModel.searchProductIdLiveData.observe(this) { product ->
            when (product) {
                is Response.LOADING -> {
                    lottie_update_product.visibility = View.VISIBLE
                    bt_update_procut.visibility = View.GONE
                }
                is Response.SUCCESS -> {
                    lottie_update_product.visibility = View.GONE
                    bt_update_procut.visibility = View.VISIBLE
                    product.data?.let {
                        it.image?.forEach { image ->
                            adapter.listFotos(image)
                            photos.add(image)
                        }
                        binding.apply {
                            titleUpdated.setText(it.nameProduct)
                            subtitleUpdated.setText(it.seller)
                            descriptionUpdate.setText(it.description)
                            valueUpdate.setText(it.value)
                            productValueFreteUpdate.setText(it.valueFrete)
                            productPaymentFormUpdate.setText(it.paymentForm)
                        }
                    }
                }
                is Response.ERROR -> {
                    Toast.makeText(
                        this,
                        "Houve algum problema ao atualizar o produto",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun setAdapter() {
        binding.rcProductsImagensUpdate.adapter = adapter
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rcProductsImagensUpdate.layoutManager = layoutManager
        binding.rcProductsImagensUpdate.setHasFixedSize(true)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.img_close_product_update -> intentProductActivity()
            R.id.fb_products_take_photo_update -> {
                val cameraIntent = Intent(Intent.ACTION_PICK)
                cameraIntent.type = "image/*"
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            }
            R.id.bt_update_procut -> {
                when {
                    title_updated.text.toString().isEmpty() -> {
                        title_updated.error = "Digite o titulo"
                    }
                    subtitle_updated.text.toString().isEmpty() -> {
                        subtitle_updated.error = "Digite o nome do subtitulo"
                    }
                    else -> {
                        val produto = Product(
                            nameProduct = title_updated.text.toString(),
                            seller = subtitle_updated.text.toString(),
                            image = photos,
                            value = value_update.text.toString(),
                            description = description_update.text.toString(),
                            paymentForm = product_payment_form_update.text.toString(),
                            valueFrete = product_value_frete_update.text.toString()
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
                            Utils.uriToBitmap(data!!, contentResolver).orEmpty()
                        )
                        Utils.uriToBitmap(data, contentResolver)
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
        img_close_product_update.setOnClickListener(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        intentProductActivity()
    }

    private fun setObserver() {
        productsViewModel.updateProductLiveData.observe(this) {
            when (it) {
                is Response.LOADING -> {
                    binding.apply {
                        lottieUpdateProduct.visibility = View.VISIBLE
                        btUpdateProcut.visibility = View.GONE
                    }
                }
                is Response.SUCCESS -> {
                    binding.apply {
                        lottieUpdateProduct.visibility = View.GONE
                        btUpdateProcut.visibility = View.VISIBLE
                    }
                    intentProductActivity()
                    Toast.makeText(this, "Produto atualizado", Toast.LENGTH_SHORT).show();
                }
                is Response.ERROR -> {
                    lottie_update_product.visibility = View.GONE
                    bt_update_procut.visibility = View.VISIBLE
                    Toast.makeText(this, "Falha ao atualizar o produto", Toast.LENGTH_SHORT)
                        .show();
                }
            }
        }
    }

    private fun intentProductActivity() {
        val intent = Intent(this, ProductsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}