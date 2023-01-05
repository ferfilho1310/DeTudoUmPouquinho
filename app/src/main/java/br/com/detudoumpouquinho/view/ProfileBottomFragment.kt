package br.com.detudoumpouquinho.view

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.databinding.ProfileFragmentBinding
import br.com.detudoumpouquinho.productsUtils.Utils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth

class ProfileBottomFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!

    private var sharedPreferences: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileFragmentBinding.inflate(layoutInflater)

        listeners()
        showRegisterOrSignIn()
        return binding.root
    }

    private fun showRegisterOrSignIn() {
        sharedPreferences =
            requireActivity().getSharedPreferences(
                SignUserActivity.WITHOUT_REGISTRATION,
                Context.MODE_PRIVATE
            )

        if (sharedPreferences?.getBoolean("semcadastro", false) == true) {
            visibilityViews(
                tvSignVisibility = false,
                tvSignUpVisibility = false,
                tvChangePasswordVisibility = true,
                tvSignOutVisibility = true,
                view1Visibility = false,
                view2Visibility = false
            )
        } else {
            visibilityViews(
                tvSignVisibility = true,
                tvSignUpVisibility = true,
                tvChangePasswordVisibility = false,
                tvSignOutVisibility = false,
                view3Visibility = false,
                view4Visibility = false
            )
        }
    }

    private fun visibilityViews(
        tvSignVisibility: Boolean,
        tvSignUpVisibility: Boolean,
        tvChangePasswordVisibility: Boolean,
        tvSignOutVisibility: Boolean,
        view1Visibility: Boolean = true,
        view2Visibility: Boolean = true,
        view3Visibility: Boolean = true,
        view4Visibility: Boolean = true
    ) {
        binding.apply {
            tvSignIn.isVisible = tvSignVisibility
            tvSignUp.isVisible = tvSignUpVisibility
            tvSignOut.isVisible = tvSignOutVisibility
            tvChangePassword.isVisible = tvChangePasswordVisibility
            view1.isVisible = view1Visibility
            view2.isVisible = view2Visibility
            view3.isVisible = view3Visibility
            view4.isVisible = view4Visibility
        }
    }


    private fun listeners() {
        binding.tvSignIn.setOnClickListener(this)
        binding.tvSignUp.setOnClickListener(this)
        binding.tvChangePassword.setOnClickListener(this)
        binding.tvSignOut.setOnClickListener(this)
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
            val i = Intent(requireActivity(), ProductsActivity::class.java)
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