package br.com.detudoumpouquinho.view

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.productsUtils.Response
import br.com.detudoumpouquinho.databinding.SendRequestProductFragmentBinding
import br.com.detudoumpouquinho.productsUtils.Utils
import br.com.detudoumpouquinho.model.Product
import br.com.detudoumpouquinho.viewModel.remoteConfig.RemoteConfigViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SendRequestProductBottomSheet : BottomSheetDialogFragment(), View.OnClickListener {

    private var product = Product()
    private val remoteConfig: RemoteConfigViewModel by viewModel()
    private var celular = ""
    private var sharedPreferences: SharedPreferences? = null
    private var extras: Product? = null

    private var _binding: SendRequestProductFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SendRequestProductFragmentBinding.inflate(inflater)

        sharedPreferences = requireActivity().getSharedPreferences(ENDERECO, Context.MODE_PRIVATE)
        extras = arguments?.getParcelable("product")

        product = extras!!

        fillFieldsWithDataSharedPreferences()
        imageOfProduct()
        fetchCelPhoneRemoteConfig()
        fillSharedPreferencesWithDataClient()
        listener()

        return binding.root
    }

    private fun fillFieldsWithDataSharedPreferences() {
        sharedPreferences?.apply {
            binding.run {
                edStreet.setText(getString("rua", ""))
                edNumber.setText(getString("numero", ""))
                edBairro.setText(getString("bairro", ""))
                edCidade.setText(getString("cidade", ""))
                edEstado.setText(getString("estado", ""))
                edCep.setText(getString("cep", ""))
            }
        }
    }

    private fun imageOfProduct() {
        extras?.apply {
            Glide.with(requireActivity())
                .load(Utils.stringToBitMap(image?.get(0)))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .centerCrop()
                .into(binding.imgPreviewRequestProduct)
            binding.tvNameProduct.text = nameProduct.orEmpty()
            binding.tvValueProduct.text = "R$ ".plus(value)
        }
    }

    private fun fetchCelPhoneRemoteConfig() {
        remoteConfig.celularLiveData.observe(requireActivity()) {
            when (it) {
                is Response.SUCCESS -> {
                    celular = it.data.orEmpty()
                }
                is Response.ERROR -> {
                    Log.e("ERROR", "Houve um erro ao buscar o numero do telefone no remoteConfig")
                }
                else -> Unit
            }
        }
    }

    private fun fillSharedPreferencesWithDataClient() {
        val fillDataClient = sharedPreferences?.edit()

        fillDataClient?.apply {
            binding.run {
                putString("rua", edStreet.text.toString())
                putString("numero", edNumber.text.toString())
                putString("bairro", edBairro.text.toString())
                putString("cidade", edCidade.text.toString())
                putString("estado", edEstado.text.toString())
                putString("cep", edCep.text.toString())
            }
        }.also {
            it?.apply()
        }
    }

    private fun buildMessageSendMessageWhatsApp() {
        val templateRequestSalvedSharedPreferences = StringBuilder()
        val textNoSalveInSharedPreferences = StringBuilder()

        val endereco = if (sharedPreferences != null) {
            templateRequest(sharedPreferences, templateRequestSalvedSharedPreferences)
        } else {
            textNoSalveInSharedPreferences.apply {
                binding.run {
                    append("Olá, Gostaria de fazer o pedido de um(a) ${product.nameProduct} ")
                    append("no valor de ${product.value}.\n\n")
                    append("O endereço para envio é:\n Rua: ${edStreet.text.toString()}\n")
                    append("Número: ${edNumber.text.toString()}\n")
                    append("Bairro: ${edBairro.text.toString()}\n")
                    append("Cidade: ${edCidade.text.toString()}\n")
                    append("Estado: ${edEstado.text.toString()}\n")
                    append("CEP: ${edCep.text.toString()}\n")
                }
            }
        }
        sendMessageWhatsApp(endereco, celular)
    }

    private fun listener() {
        binding.btFinishRequest.setOnClickListener(this)
        binding.closeSendRequestProduct.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.close_send_request_product -> {
                dismiss()
            }
            R.id.bt_finish_request -> {
                userInformationValidate()
            }
        }
    }

    private fun userInformationValidate() {
        binding.apply {
            when {
                edStreet.text.toString().isEmpty() -> {
                    edStreet.error = "Informe a rua"
                }
                edNumber.text.toString().isEmpty() -> {
                    edNumber.error = "Informe o numero"
                }
                edBairro.text.toString().isEmpty() -> {
                    edBairro.error = "Informe o bairro"
                }
                edCidade.text.toString().isEmpty() -> {
                    edCidade.error = "Informe a cidade"
                }
                edEstado.text.toString().isEmpty() -> {
                    edEstado.error = "Informe o estado"
                }
                edCep.text.toString().isEmpty() -> {
                    edCep.error = "Informe o cep"
                }
                else -> {
                    buildMessageSendMessageWhatsApp()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        remoteConfig.fetchCelular(requireContext())
    }

    private fun sendMessageWhatsApp(informationUser: StringBuilder, phoneNumber: String) {
        val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=${informationUser}"
        try {
            val pm = requireActivity().packageManager
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            startWhatsApp(url)
        } catch (e: PackageManager.NameNotFoundException) {
            startWhatsApp(url)
        }
    }

    private fun templateRequest(
        sharedPreferences: SharedPreferences?,
        templateRequest: StringBuilder
    ): StringBuilder {
        return templateRequest.apply {
            templateRequest.append("Olá, Gostaria de fazer o pedido de um(a) ${product.nameProduct} ")
            templateRequest.append("no valor de R$ ${product.value}.\n\n")
            templateRequest.append(
                "O endereço para envio é:\n Rua: ${
                    sharedPreferences?.getString(
                        "rua",
                        ""
                    )
                }\n"
            )
            templateRequest.append("Número: ${sharedPreferences?.getString("numero", "")}\n")
            templateRequest.append("Cidade: ${sharedPreferences?.getString("cidade", "")}\n")
            templateRequest.append("Estado: ${sharedPreferences?.getString("estado", "")}\n")
            templateRequest.append("CEP: ${sharedPreferences?.getString("cep", "")}\n")
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ENDERECO = "endereço"
    }
}