package com.bappeda.publication_app.ui.home.ui.ui

import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.iterator
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bappeda.publication_app.R
import com.bappeda.publication_app.databinding.ActivityPageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class PageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPageBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        // Menginisialisasi NavController
        navController = findNavController(R.id.nav_host_fragment_activity_page)

        // Menghubungkan NavController dengan AppBarConfiguration
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_bookmark
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Menghubungkan NavController dengan BottomNavigationView
        navView.setupWithNavController(navController)
    }

}
