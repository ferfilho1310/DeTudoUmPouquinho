package br.com.detudoumpouquinho.onboarding.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import br.com.detudoumpouquinho.R
import br.com.detudoumpouquinho.onboarding.adapter.OnboardingItemsAdapter
import br.com.detudoumpouquinho.onboarding.model.OnboardingItem
import br.com.detudoumpouquinho.view.SendRequestProduct
import br.com.detudoumpouquinho.view.SignUserActivity
import kotlinx.android.synthetic.main.activity_onboarding.*

class OnboardingActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var onboardingItemsAdapter: OnboardingItemsAdapter
    private lateinit var indicatorContainer: LinearLayout
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        window.navigationBarColor = resources.getColor(R.color.dark_blue)
        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences(SKIPPED_ONBOARDING, Context.MODE_PRIVATE)

        setOnboardingItems()
        setupIndicators()
        bt_onboading.setOnClickListener(this)
        skipped.setOnClickListener(this)
    }

    private fun setOnboardingItems() {
        onboardingItemsAdapter = OnboardingItemsAdapter(
            listOf(
                OnboardingItem(
                    onboardingImage = "https://assets2.lottiefiles.com/packages/lf20_v33gmcrb.json",
                    description = "Encontre telefones, roupas, perfumes entre outros tipos de produtos."
                ),
                OnboardingItem(
                    onboardingImage = "https://assets3.lottiefiles.com/packages/lf20_7wwm6az7.json",
                    description = "Selecione o produto e informe seus dados para ser direcionado para nosso WhatsApp."
                )
            )
        )

        val viewPage = findViewById<ViewPager2>(R.id.onboardingViewPager)
        viewPage.adapter = onboardingItemsAdapter
        viewPage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setupCurrentIndicator(position)
            }
        })
        (viewPage.getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }

    private fun setupIndicators() {
        indicatorContainer = findViewById(R.id.indicatoContainer)
        val indicators = arrayOfNulls<ImageView>(onboardingItemsAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive_background
                    )
                )
                it.layoutParams = layoutParams
                indicatorContainer.addView(it)
            }
        }
    }

    private fun setupCurrentIndicator(position: Int) {
        val childCount = indicatorContainer.childCount
        for (i in 0 until childCount) {
            val imageView = indicatorContainer.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_activebackground
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive_background
                    )
                )
            }
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.bt_onboading -> {
                preferencesSkipped()
                startSing()
            }
            R.id.skipped -> {
                preferencesSkipped()
                startSing()
            }
        }
    }

    private fun startSing(){
        val i = Intent(this, SignUserActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun preferencesSkipped(){
        val result = sharedPreferences.edit()
        result.putBoolean("skipped", true)
        result.apply()
    }

    companion object {
        val SKIPPED_ONBOARDING = "SKIPPED"
    }
}