package br.com.detudoumpouquinho.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.viewModel.user.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.rescue_password_fragment.*
import org.koin.android.ext.android.inject

class RescuePasswordFragment : BottomSheetDialogFragment() {

    private val viewModel: UserViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.rescue_password_fragment, container, false)

        val edEmail = view.findViewById<TextInputEditText>(R.id.rescue_email)
        val btRescuePassWord = view.findViewById<Button>(R.id.bt_rescue_password)
        val closeRescue = view.findViewById<ImageButton>(R.id.img_close_rescue_password)

        btRescuePassWord.setOnClickListener {
            if (edEmail.text.toString().isEmpty()) {
                edEmail.error = "Informe um e-mail"
            } else {
                btRescuePassWord.visibility = View.GONE
                lottieRescuePassword.visibility = View.VISIBLE

                viewModel.rescuePassWord(edEmail.text.toString())
            }
        }

        closeRescue.setOnClickListener {
            dismiss()
        }

        viewModel.rescuePassWordListener().observe(this) {
            if (it == true) {
                dismiss()
                btRescuePassWord.visibility = View.VISIBLE
                lottieRescuePassword.visibility = View.GONE
                Toast.makeText(
                    context,
                    "Foi enviado um e-mail de reset da senha para o e-mail informado",
                    Toast.LENGTH_LONG
                ).show();
            } else {
                btRescuePassWord.visibility = View.VISIBLE
                lottieRescuePassword.visibility = View.GONE
                Toast.makeText(context, "Verifique o e-mail digitado", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return view
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