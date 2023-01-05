package br.com.detudoumpouquinho.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.onboarding.view.OnboardingActivity


@SuppressLint("CustomSplashScreen")
class SplashscreenActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    var result: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashscreen)

        supportActionBar?.hide()
        window.navigationBarColor = resources.getColor(R.color.light_blue)

        sharedPreferences =
            getSharedPreferences(OnboardingActivity.SKIPPED.SKIPPED_ONBOARDING, Context.MODE_PRIVATE)

        result = sharedPreferences.getBoolean("skipped", false)

        Handler().postDelayed(
            {
                if (result) {
                    startSing()
                } else {
                    val i = Intent(this, OnboardingActivity::class.java)
                    startActivity(i)
                    finish()
                }
            },
            3000
        )
    }

    private fun startSing() {
        val i = Intent(this, ProductsActivity::class.java)
        startActivity(i)
        finish()
    }
}