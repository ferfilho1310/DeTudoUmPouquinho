package br.com.detudoumpouquinho.view

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.productsUtils.Utils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth

class ProfileBottomFragment : BottomSheetDialogFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.profile_fragment, container, false)

        val entrar = view.findViewById<TextView>(R.id.tv_sign_in)
        val cadastrar = view.findViewById<TextView>(R.id.tv_sign_up)
        val trocarSenha = view.findViewById<TextView>(R.id.tv_change_password)
        val sair = view.findViewById<TextView>(R.id.tv_sign_out)
        val view1 = view.findViewById<View>(R.id.view1)
        val view2 = view.findViewById<View>(R.id.view2)
        val view3 = view.findViewById<View>(R.id.view3)
        val view4 = view.findViewById<View>(R.id.view4)

        val sharedPreferences =
            requireActivity().getSharedPreferences(
                SignUserActivity.WITHOUT_REGISTRATION,
                Context.MODE_PRIVATE
            )

        if (sharedPreferences?.getBoolean("semcadastro", false) == true) {
            entrar.isVisible = false
            cadastrar.isVisible = false
            sair.isVisible = true
            trocarSenha.isVisible = true
            view1.isVisible = false
            view2.isVisible = false
        } else {
            entrar.isVisible = true
            cadastrar.isVisible = true
            sair.isVisible = false
            trocarSenha.isVisible = false
            view3.isVisible = false
            view4.isVisible = false
        }

        entrar.setOnClickListener(this)
        cadastrar.setOnClickListener(this)
        trocarSenha.setOnClickListener(this)
        sair.setOnClickListener(this)

        return view
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_sign_in -> {
                startSign()
                dismiss()
            }
            R.id.tv_sign_up -> {
                startActivity(Intent(requireActivity(), CreateNewUserActivity::class.java))
                dismiss()
            }
            R.id.tv_change_password -> {
                val bottomSheetDialogFragment = RescuePasswordFragment()
                bottomSheetDialogFragment.show(childFragmentManager, "TAG")
            }
            R.id.tv_sign_out -> {
                Utils.alertDialog(requireActivity(), "Deseja sair da sua conta ?", ::signOut)
            }
        }
    }

    private fun signOut() {
        val sharedPreferences =
            requireActivity().getSharedPreferences(
                SignUserActivity.WITHOUT_REGISTRATION,
                Context.MODE_PRIVATE
            )

        FirebaseAuth.getInstance().signOut().also {
            val i = Intent(requireActivity(),ProductsActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            requireActivity().startActivity(i)
            requireActivity().finish()

            sharedPreferences.edit().clear().apply()
            dismiss()
        }
    }

    private fun startSign() {
        val i = Intent(requireActivity(), SignUserActivity::class.java)
        requireActivity().startActivity(i)
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