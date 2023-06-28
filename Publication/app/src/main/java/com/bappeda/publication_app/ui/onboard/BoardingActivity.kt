package com.bappeda.publication_app.ui.onboard
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bappeda.publication_app.databinding.ActivityBoardingBinding
import com.bappeda.publication_app.ui.home.ui.ui.PageActivity


class BoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.nextButton.setOnClickListener {
            navigateToMainActivity()
        }
    }

    private fun navigateToMainActivity() {
        val intent= Intent(this, PageActivity::class.java)
        startActivity(intent)
        finish()
    }
}
