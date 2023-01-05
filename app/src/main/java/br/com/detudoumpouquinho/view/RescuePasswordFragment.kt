package br.com.detudoumpouquinho.view

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.databinding.RescuePasswordFragmentBinding
import br.com.detudoumpouquinho.viewModel.user.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.rescue_password_fragment.*
import org.koin.android.ext.android.inject

class RescuePasswordFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private val viewModel: UserViewModel by inject()

    private var _binding: RescuePasswordFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RescuePasswordFragmentBinding.inflate(layoutInflater)

        listeners()
        setViewModel()
        return binding.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_rescue_password -> {
                if (binding.rescueEmail.text.toString().isEmpty()) {
                    binding.rescueEmail.error = "Informe um e-mail"
                } else {
                    binding.btRescuePassword.visibility = View.GONE
                    binding.lottieRescuePassword.visibility = View.VISIBLE

                    viewModel.rescuePassWord(binding.rescueEmail.text.toString())
                }
            }
            R.id.img_close_rescue_password -> dismiss()
        }
    }
    private fun listeners() {
        binding.btRescuePassword.setOnClickListener(this)
        binding.imgCloseRescuePassword.setOnClickListener(this)
    }

    private fun setViewModel() {
        viewModel.rescuePasswordUser.observe(this) {
            if (it == true) {
                dismiss()

                val i = Intent(
                    requireActivity(),
                    SignUserActivity::class.java
                )
                requireActivity().startActivity(i)
                requireActivity().finish()

                val sharedPreferences =
                    requireActivity().getSharedPreferences(
                        SignUserActivity.WITHOUT_REGISTRATION,
                        Context.MODE_PRIVATE
                    )

                FirebaseAuth.getInstance().signOut().also {
                    sharedPreferences.edit().clear().apply()
                }

                binding.btRescuePassword.visibility = View.VISIBLE
                lottieRescuePassword.visibility = View.GONE

                Toast.makeText(
                    context,
                    "Foi enviado um e-mail de recuperação para o seu e-mail cadastrado",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                binding.btRescuePassword.visibility = View.VISIBLE
                lottieRescuePassword.visibility = View.GONE
                Toast.makeText(context, "Verifique o e-mail digitado", Toast.LENGTH_SHORT)
                    .show()
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
}