package br.com.detudoumpouquinho.view

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.os.bundleOf
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.model.Product
import br.com.detudoumpouquinho.viewModel.products.ProductsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.viewModel


class SendRequestProduct : BottomSheetDialogFragment(), View.OnClickListener {

    val sharedPreferences = context?.getSharedPreferences(ENDERECO, Context.MODE_PRIVATE)
    val viewModel: ProductsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val extras: Product? = arguments?.getParcelable("product")

        val view = inflater.inflate(R.layout.send_request_product_fragment, container)

        val rua = view.findViewById<TextInputEditText>(R.id.ed_street)
        val numero = view.findViewById<TextInputEditText>(R.id.ed_number)
        val bairro = view.findViewById<TextInputEditText>(R.id.ed_bairro)
        val cidade = view.findViewById<TextInputEditText>(R.id.ed_cidade)
        val cep = view.findViewById<TextInputEditText>(R.id.ed_cep)
        val finalizarPedido = view.findViewById<Button>(R.id.bt_finish_request)
        val close = view.findViewById<ImageButton>(R.id.close_send_request_product)

        val sharedEditor = sharedPreferences?.edit()?.apply {
            putString("rua", rua.text.toString())
            putString("numero", numero.text.toString())
            putString("bairro", bairro.text.toString())
            putString("cidade", cidade.text.toString())
            putString("cep", cep.text.toString())
        }
        sharedEditor?.apply()

        finalizarPedido.setOnClickListener(this)
        close.setOnClickListener(this)
        return view
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.bt_finish_request -> {
                val contact = "+5531995394528"
                val url = "https://api.whatsapp.com/send?phone=$contact"
                try {
                    val pm = requireActivity().packageManager
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
                    startWhatsApp(url)
                } catch (e: PackageManager.NameNotFoundException) {
                    startWhatsApp(url)
                }
            }
            R.id.close_send_request_product -> {
                dismiss()
            }
        }
    }

    private fun startWhatsApp(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }

    companion object {
        const val ENDERECO = "endereço"

        val newInstance = SendRequestProduct().apply {
            bundleOf()
        }
    }
}