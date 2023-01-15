package br.com.detudoumpouquinho.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.productsUtils.Response
import br.com.detudoumpouquinho.databinding.InsertProductFragmentBinding
import br.com.detudoumpouquinho.model.Product
import br.com.detudoumpouquinho.productsUtils.Utils
import br.com.detudoumpouquinho.view.adapter.FotosAdapter
import br.com.detudoumpouquinho.view.adapter.ProdutosAdapter
import br.com.detudoumpouquinho.viewModel.products.ProductsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject


class InsertProductBottomFragment(adapter: ProdutosAdapter?) : BottomSheetDialogFragment(), View.OnClickListener {

    private val CAMERA_REQUEST = 1888

    val viewModel: ProductsViewModel by inject()
    val productsAdapter: FotosAdapter = FotosAdapter()
    val photos: ArrayList<String> = arrayListOf()

    private var _binding: InsertProductFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InsertProductFragmentBinding.inflate(inflater)
        binding.productsValue.setRawInputType(Configuration.KEYBOARD_12KEY)

        setObserver()
        setAdapter()
        listeners()

        return binding.root
    }

    private fun setAdapter() {
        binding.rcProductsImagens.apply {
            adapter = productsAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.button -> {
                validateInformationProduct()
            }
            R.id.fb_products_take_photo -> {
                val cameraIntent = Intent(Intent.ACTION_PICK)
                cameraIntent.type = "image/*"
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            }
            R.id.close_insert_product -> {
                dismiss()
            }
        }
    }

    private fun validateInformationProduct() {
        binding.apply {
            when {
                photos.isNullOrEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        "Você não tirou fotos do produto",
                        Toast.LENGTH_LONG
                    ).show()
                }
                productsTitle.text.toString().isEmpty() -> {
                    productsTitle.error = "Informe o nome do produto"
                }
                productsValue.text.toString().isEmpty() -> {
                    productsValue.error = "Informe o valor do produto"
                }
                productDescription.text.toString().isEmpty() -> {
                    productDescription.error = "Crie uma descrição do produto"
                }
                else -> {
                    lottieInsertProduct.visibility = View.VISIBLE
                    button.visibility = View.GONE
                    viewModel.insertProduct(
                        Product(
                            nameProduct = productsTitle.text.toString(),
                            value = productsValue.text.toString(),
                            seller = productsSubtitle.text.toString(),
                            description = productDescription.text.toString(),
                            image = photos,
                            valueFrete = productValueFrete.text.toString(),
                            paymentForm = productPaymentForm.text.toString()
                        )
                    )
                }
            }
        }
    }

    private fun listeners() {
        binding.let {
            it.button.setOnClickListener(this)
            it.fbProductsTakePhoto.setOnClickListener(this)
            it.closeInsertProduct.setOnClickListener(this)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAMERA_REQUEST) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    try {
                        binding.apply {
                            lnlInsertImage.visibility = View.GONE
                            rcProductsImagens.visibility = View.VISIBLE
                        }

                        productsAdapter.listFotos(
                            Utils.uriToBitmap(
                                data!!,
                                requireContext().contentResolver
                            ).orEmpty()
                        )
                        context?.let {
                            Utils.uriToBitmap(data, requireContext().contentResolver)
                                .let {
                                    photos.add(it!!)
                                }
                        }

                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Picture Not taken", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                RESULT_CANCELED -> {
                    Toast.makeText(requireContext(), "Picture was not taken 1 ", Toast.LENGTH_SHORT)
                        .show();
                }
                else -> {
                    Toast.makeText(requireContext(), "Picture was not taken 2 ", Toast.LENGTH_SHORT)
                        .show();
                }
            }
        }
    }

    private fun setObserver() {
        viewModel.insertProductLiveData.observe(requireActivity()) {
            when(it){
                is Response.LOADING -> {
                    binding.lottieInsertProduct.visibility = View.VISIBLE
                    binding.button.visibility = View.GONE
                }
                is Response.SUCCESS -> {
                    val iProductsActivity = Intent(requireContext(),ProductsActivity::class.java)
                    startActivity(iProductsActivity)
                    dismiss()
                }
                is Response.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        "Houve algum problema ao tentar inserir o produto",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.lottieInsertProduct.visibility = View.GONE
                    binding.button.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }

    fun newInstance(adapter: ProdutosAdapter): InsertProductBottomFragment {
        return InsertProductBottomFragment(adapter)
    }
}
