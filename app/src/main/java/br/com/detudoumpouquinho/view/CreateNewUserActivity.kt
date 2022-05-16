package br.com.detudoumpouquinho.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.model.User
import br.com.detudoumpouquinho.viewModel.user.UserViewModel
import kotlinx.android.synthetic.main.create_new_user_activity.*
import org.koin.android.ext.android.inject

class CreateNewUserActivity : AppCompatActivity(), View.OnClickListener {

    private val userViewModel: UserViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_new_user_activity)

        supportActionBar?.hide()

        setObservers()
        setListeners()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.create_user_button -> {
                setInformationUser()
            }
            R.id.back_sign_user -> {
                backSignActivity()
            }
        }
    }

    private fun setListeners() {
        create_user_button.setOnClickListener(this)
        back_sign_user.setOnClickListener(this)
    }

    private fun setObservers() {
        userViewModel.createUserListener().observe(this) {
            if (it == true) {
                backSignActivity()
            } else {
                Toast.makeText(
                    this,
                    "Verifique seu e-mail ou se a senha tem mais de 6 caracteres.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setInformationUser() {
        when {
            create_name.text.toString().isEmpty() -> {
                create_name.error = "Informe seu nome"
            }
            create_email.text.toString().isEmpty() -> {
                create_email.error = "Informe seu e-mail"
            }
            create_password.text.toString().isEmpty() -> {
                create_password.error = "Informe sua senha"
            }
            create_confirm_password.toString().isEmpty() -> {
                create_confirm_password.error = "Confirme sua senha"
            }
            create_password.text.toString() != create_confirm_password.text.toString() -> {
                Toast.makeText(
                    this,
                    "As senhas estão diferentes",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                userViewModel.createUser(
                    User(
                        name = create_name.text.toString(),
                        email = create_email.text.toString(),
                        password = create_password.text.toString(),
                        confirmPassword = create_confirm_password.text.toString()
                    )
                )
            }
        }
    }

    private fun backSignActivity() {
        startActivity(Intent(this, SignUserActivity::class.java))
        finish()
    }
}