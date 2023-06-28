package com.bappeda.publication_app.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bappeda.publication_app.R
import com.bappeda.publication_app.databinding.ActivitySplashBinding
import com.bappeda.publication_app.ui.onboard.BoardingActivity
import com.bumptech.glide.Glide

class SplashActivity : AppCompatActivity() {
    lateinit var handler: Handler
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        var  panel_IMG_back = binding.ivCircleSplash
        Glide
            .with(this)
            .load(R.drawable.image)
            .into(panel_IMG_back)

        handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val intent= Intent(this,BoardingActivity::class.java)
            startActivity(intent)
            finish()
        },2000)
    }
}