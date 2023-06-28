package com.bappeda.publication_app.ui.home.ui.ui.bookmark

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.bappeda.publication_app.R
import com.bappeda.publication_app.adapter.ImagePagerAdapter
import com.bappeda.publication_app.response.Detail
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class DetailBookmarkActivity : AppCompatActivity() {
    private lateinit var textViewTitle: TextView
    private lateinit var textViewDescription: TextView
    private lateinit var textViewPembangunan: TextView
    private lateinit var textViewLokasi: TextView
    private lateinit var textViewTahun: TextView
    private lateinit var textViewDana: TextView
    private lateinit var textViewKoordinat: TextView
    private lateinit var viewPager: ViewPager2
    private lateinit var dotsIndicator: WormDotsIndicator
    private lateinit var toolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_bookmark)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textViewTitle = findViewById(R.id.textViewTitles)
        textViewPembangunan = findViewById(R.id.textViewPembangunan)
        textViewDescription = findViewById(R.id.textViewDescriptions)
        textViewLokasi = findViewById(R.id.textViewLokasi)
        textViewTahun = findViewById(R.id.textViewTahun)
        textViewDana = findViewById(R.id.textViewDana)
        textViewKoordinat = findViewById(R.id.textViewKoordinat)
        viewPager = findViewById(R.id.view_pager)
        dotsIndicator = findViewById(R.id.dots_indicator)
        val detail = intent.getParcelableExtra<Detail>("detail_kecamatan")
        val imageUrl = "http://192.168.1.21:3002/halaman/${detail?.id}/${detail?.gambar1}"
        Log.d("cek_data", imageUrl)
        if (detail != null) {
            textViewTitle.text = detail.namaKecamatan
            textViewDescription.text = detail.bidang
            textViewPembangunan.text = detail.judul
            textViewLokasi.text = detail.lokasi
            textViewTahun.text = detail.tahun
            textViewDana.text = detail.anggaran
            textViewKoordinat.text = detail.koordinat
            val imagePagerAdapter = ImagePagerAdapter(detail)
            viewPager.adapter = imagePagerAdapter
            dotsIndicator.setViewPager2(viewPager)
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}
