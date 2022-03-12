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
import kotlinx.android.synthetic.main.sign_user_activity.*
import org.koin.android.ext.android.inject

class SignUserActivity : AppCompatActivity(), View.OnClickListener {

    private val userViewModel: UserViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_user_activity)

        supportActionBar?.hide()

        setListeners()
        setObservers()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tx_sign_up -> {
                startActivity(Intent(this, CreateNewUserActivity::class.java))
            }
            R.id.bt_sign_user -> {
                setSignUserInformation()
            }
        }
    }

    private fun setListeners() {
        tx_sign_up.setOnClickListener(this)
        bt_sign_user.setOnClickListener(this)
    }

    private fun setObservers() {
        userViewModel.signUserListener().observe(this) {
            if (it == true) {

            } else {
                Toast.makeText(
                    this,
                    "Verifique o e-mail e a senha digitada.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setSignUserInformation() {
        when {
            sign_email.text.toString().isEmpty() -> {
                sign_email.error = "Informe seu e-mail"
            }
            sign_password.text.toString().isEmpty() -> {
                sign_password.error = "Informe sua senha"
            }
            else -> {
                userViewModel.signUser(
                    User(
                        email = sign_email.text.toString(),
                        password = sign_password.text.toString()
                    )
                )
            }
        }
    }
}