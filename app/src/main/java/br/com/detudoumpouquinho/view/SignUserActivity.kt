package br.com.detudoumpouquinho.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.model.User
import br.com.detudoumpouquinho.viewModel.user.UserViewModel
import kotlinx.android.synthetic.main.sign_user_activity.*
import org.koin.android.ext.android.inject

class SignUserActivity : AppCompatActivity(), View.OnClickListener {

    private val userViewModel: UserViewModel by inject()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_user_activity)

        window.navigationBarColor = resources.getColor(R.color.dark_blue)
        supportActionBar?.hide()

        setListeners()
        setObservers()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.bt_sign_user -> setSignUserInformation()
            R.id.img_back -> finish()
        }
    }

    private fun startProductActivity() {
        val intent = Intent(this, ProductsActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setListeners() {
        bt_sign_user.setOnClickListener(this)
        img_back.setOnClickListener(this)
    }

    private fun setObservers() {
        userViewModel.signUserListener().observe(this) {
            if (it == true) {
                lottie.visibility = View.GONE
                bt_sign_user.visibility = View.VISIBLE

                startProductActivity()

                val sharedPreferences =
                    getSharedPreferences(WITHOUT_REGISTRATION, Context.MODE_PRIVATE)
                val edit = sharedPreferences.edit()
                edit.putBoolean("semcadastro", true)
                edit.apply()

            } else {
                Toast.makeText(
                    this,
                    "Verifique o e-mail e a senha digitada.",
                    Toast.LENGTH_SHORT
                ).show()
                lottie.visibility = View.GONE
                bt_sign_user.visibility = View.VISIBLE

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
                lottie.visibility = View.VISIBLE
                bt_sign_user.visibility = View.GONE
                userViewModel.signUser(
                    User(
                        email = sign_email.text.toString(),
                        password = sign_password.text.toString()
                    )
                )
            }
        }
    }

    companion object {
        const val WITHOUT_REGISTRATION = "WITHOUT_REGISTRATION"
    }
}