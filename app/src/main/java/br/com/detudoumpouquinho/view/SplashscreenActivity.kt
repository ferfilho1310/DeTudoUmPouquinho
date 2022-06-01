package br.com.detudoumpouquinho.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.onboarding.view.OnboardingActivity


@SuppressLint("CustomSplashScreen")
class SplashscreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashscreen)

        supportActionBar?.hide()

        Handler().postDelayed(
            {
                val i = Intent(this, OnboardingActivity::class.java)
                startActivity(i)
                finish()
            }, 3000
        )
    }
}