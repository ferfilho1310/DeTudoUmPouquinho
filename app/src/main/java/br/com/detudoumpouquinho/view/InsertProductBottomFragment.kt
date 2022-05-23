package br.com.detudoumpouquinho.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Dialog
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.Utils.PhotosUtils
import br.com.detudoumpouquinho.model.Product
import br.com.detudoumpouquinho.view.adapter.FotosAdapter
import br.com.detudoumpouquinho.viewModel.products.ProductsViewModel
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_insert_product.*
import org.koin.android.ext.android.inject


class InsertProductBottomFragment : BottomSheetDialogFragment() {

    private val CAMERA_REQUEST = 1888

    val viewModel: ProductsViewModel by inject()
    val adapter: FotosAdapter = FotosAdapter()
    val photos: ArrayList<String> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.activity_insert_product, container, true)

        val viewf = view.findViewById<FloatingActionButton>(R.id.fb_products_take_photo)
        val rc = view.findViewById<RecyclerView>(R.id.rc_products_imagens)
        val closeButton = view.findViewById<ImageButton>(R.id.close_insert_product)
        val insertbutton = view.findViewById<Button>(R.id.button)
        val title = view.findViewById<TextInputEditText>(R.id.products_title)
        val value = view.findViewById<TextInputEditText>(R.id.products_value)
        val subtitle = view.findViewById<TextInputEditText>(R.id.products_subtitle)
        val description = view.findViewById<TextInputEditText>(R.id.product_description)
        val lottie = view.findViewById<LottieAnimationView>(R.id.lottie_insert_product)
        val frete = view.findViewById<TextInputEditText>(R.id.product_value_frete)
        val formaDePagamento = view.findViewById<TextInputEditText>(R.id.product_payment_form)

        viewf.setOnClickListener {
            val cameraIntent = Intent(Intent.ACTION_PICK)
            cameraIntent.type = "image/*"
            startActivityForResult(cameraIntent, CAMERA_REQUEST)
        }

        closeButton.setOnClickListener {
            dismiss()
        }

        insertbutton.setOnClickListener {
            lottie.visibility = View.VISIBLE
            insertbutton.visibility = View.GONE
            viewModel.insertProduct(
                Product(
                    title = title.text.toString(),
                    value = value.text.toString(),
                    subtitle = subtitle.text.toString(),
                    description = description.text.toString(),
                    image = photos,
                    valueFrete = frete.text.toString(),
                    paymentForm = formaDePagamento.text.toString()
                )
            )
        }

        viewModel.insertProductListener().observe(requireActivity()) {
            if (it == true) {
                lottie.visibility = View.GONE
                insertbutton.visibility = View.VISIBLE
                dismiss()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Houve algum problema ao tentar inserir o produto",
                    Toast.LENGTH_SHORT
                ).show()
                lottie_insert_product.visibility = View.GONE
                insertbutton.visibility = View.VISIBLE
            }
        }

        rc.adapter = adapter
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rc.layoutManager = layoutManager
        rc.setHasFixedSize(true)

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAMERA_REQUEST) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    try {

                        adapter.listFotos(PhotosUtils.uriToBitmap(data!!, requireContext().contentResolver).orEmpty())
                        context?.let {
                            PhotosUtils.uriToBitmap(data, requireContext().contentResolver)
                                .let { photos.add(it!!) }
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }
}