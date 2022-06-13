package br.com.detudoumpouquinho.view

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.productsUtils.Utils
import br.com.detudoumpouquinho.model.Product
import br.com.detudoumpouquinho.viewModel.remoteConfig.RemoteConfigViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.viewModel

class SendRequestProduct : BottomSheetDialogFragment(), View.OnClickListener {

    var product: Product = Product()
    val remoteConfig: RemoteConfigViewModel by viewModel()
    var celular = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val sharedPreferences = requireActivity().getSharedPreferences(ENDERECO, Context.MODE_PRIVATE)
        val extras: Product? = arguments?.getParcelable("product")

        product = extras!!
        val view = inflater.inflate(R.layout.send_request_product_fragment, container)

        val rua = view.findViewById<TextInputEditText>(R.id.ed_street)
        val numero = view.findViewById<TextInputEditText>(R.id.ed_number)
        val bairro = view.findViewById<TextInputEditText>(R.id.ed_bairro)
        val cidade = view.findViewById<TextInputEditText>(R.id.ed_cidade)
        val estado = view.findViewById<TextInputEditText>(R.id.ed_estado)
        val cep = view.findViewById<TextInputEditText>(R.id.ed_cep)
        val finalizarPedido = view.findViewById<Button>(R.id.bt_finish_request)
        val close = view.findViewById<ImageButton>(R.id.close_send_request_product)
        val imgProduct = view.findViewById<ImageView>(R.id.img_preview_request_product)
        val nameProduct = view.findViewById<TextView>(R.id.tv_name_product)
        val priceProduct = view.findViewById<TextView>(R.id.tv_value_product)

        sharedPreferences?.apply {
            rua.setText(getString("rua", ""))
            numero.setText(getString("numero", ""))
            bairro.setText(getString("bairro", ""))
            cidade.setText(getString("cidade", ""))
            estado.setText(getString("estado", ""))
            cep.setText(getString("cep", ""))
        }

        extras.apply {
            Glide.with(requireActivity()).load(Utils.stringToBitMap(image?.get(0)))
                .into(imgProduct)
            nameProduct.text = this.nameProduct
            priceProduct.text = "R$ ".plus(value)
        }

        remoteConfig.fetchCelularListener().observe(requireActivity()) {
            celular = it
        }

        finalizarPedido.setOnClickListener {

            val text = StringBuilder()
            val textNoSalve = StringBuilder()

            val result = sharedPreferences?.edit()

            result?.apply {
                putString("rua", rua.text.toString())
                putString("numero", numero.text.toString())
                putString("bairro", bairro.text.toString())
                putString("cidade", cidade.text.toString())
                putString("estado", estado.text.toString())
                putString("cep", cep.text.toString())
            }.also {
                it?.apply()
            }

            val endereco = if (sharedPreferences != null) {
                endereco(
                    sharedPreferences,
                    text
                )
            } else {
                textNoSalve.apply {
                    append("Olá, Gostaria de fazer o pedido de um(a) ${product.nameProduct} ")
                    append("no valor de ${product.value}.\n\n")
                    append("O endereço para envio é:\n Rua: ${rua.text.toString()}\n")
                    append("Número: ${numero.text.toString()}\n")
                    append("Bairro: ${bairro.text.toString()}\n")
                    append("Cidade: ${cidade.text.toString()}\n")
                    append("Estado: ${estado.text.toString()}\n")
                    append("CEP: ${cep.text.toString()}\n")
                }
            }
            sendMessageWhatsApp(endereco, celular)
        }

        close.setOnClickListener(this)
        return view
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.close_send_request_product -> {
                dismiss()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        remoteConfig.fetchCelular(requireContext())
    }

    private fun sendMessageWhatsApp(endereco: StringBuilder, contact: String) {

        val url = "https://api.whatsapp.com/send?phone=$contact&text=${endereco}"

        try {
            val pm = requireActivity().packageManager
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            startWhatsApp(url)
        } catch (e: PackageManager.NameNotFoundException) {
            startWhatsApp(url)
        }
    }

    private fun endereco(
        sharedPreferences: SharedPreferences?,
        text: StringBuilder
    ): StringBuilder {
        return text.apply {
            text.append("Olá, Gostaria de fazer o pedido de um(a) ${product.nameProduct} ")
            text.append("no valor de R$ ${product.value}.\n\n")
            text.append("O endereço para envio é:\n Rua: ${sharedPreferences?.getString("rua", "")}\n")
            text.append("Número: ${sharedPreferences?.getString("numero", "")}\n")
            text.append("Cidade: ${sharedPreferences?.getString("cidade", "")}\n")
            text.append("Estado: ${sharedPreferences?.getString("estado", "")}\n")
            text.append("CEP: ${sharedPreferences?.getString("cep", "")}\n")
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
    }
}