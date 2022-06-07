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

        sharedPreferences =
            getSharedPreferences(OnboardingActivity.SKIPPED_ONBOARDING, Context.MODE_PRIVATE)

        result = sharedPreferences.getBoolean("skipped", false)

        supportActionBar?.hide()

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
        val i = Intent(this, SignUserActivity::class.java)
        startActivity(i)
        finish()
    }
}