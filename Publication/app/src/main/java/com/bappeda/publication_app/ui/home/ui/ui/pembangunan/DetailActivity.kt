package com.bappeda.publication_app.ui.home.ui.ui.pembangunan

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.bappeda.publication_app.R
import com.bappeda.publication_app.adapter.ImagePagerAdapter
import com.bappeda.publication_app.response.Detail
import com.google.gson.Gson
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator


class DetailActivity : AppCompatActivity() {
    private lateinit var detailData: Detail
    private lateinit var textViewTitle: TextView
    private lateinit var textViewDescription: TextView
    private lateinit var textViewPembangunan: TextView
    private lateinit var textViewLokasi: TextView
    private lateinit var textViewTahun: TextView
    private lateinit var textViewDana: TextView
    private lateinit var textViewKoordinat: TextView
    private lateinit var viewPager: ViewPager2
    private lateinit var dotsIndicator: WormDotsIndicator
    private lateinit var toggleButton: ToggleButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Mendapatkan data dari Intent
        detailData = intent.getParcelableExtra("detail_kecamatan")!!


        textViewTitle = findViewById(R.id.textViewTitles)
        textViewPembangunan = findViewById(R.id.textViewPembangunan)
        textViewDescription = findViewById(R.id.textViewDescriptions)
        textViewLokasi = findViewById(R.id.textViewLokasi)
        textViewTahun = findViewById(R.id.textViewTahun)
        textViewDana = findViewById(R.id.textViewDana)
        textViewKoordinat = findViewById(R.id.textViewKoordinat)

        viewPager = findViewById(R.id.view_pager)
        dotsIndicator = findViewById(R.id.dots_indicator)
        toggleButton = findViewById(R.id.toggleButton)

        val imageUrl = "http://192.168.1.21:3002/halaman/${detailData.id}/${detailData.gambar1}"
        Log.d("cek_data", imageUrl)
        textViewTitle.text = detailData.namaKecamatan
        textViewDescription.text = detailData.bidang
        textViewPembangunan.text = detailData.judul
        textViewLokasi.text = detailData.lokasi
        textViewTahun.text = detailData.tahun
        textViewDana.text = detailData.anggaran
        textViewKoordinat.text = detailData.koordinat
        val imagePagerAdapter = ImagePagerAdapter(detailData)
        viewPager.adapter = imagePagerAdapter
        dotsIndicator.setViewPager2(viewPager)


        toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val isHalamanDisimpanSebelumnya = isHalamanDalamFavorit()
                simpanHalamanKeFavorit(isHalamanDisimpanSebelumnya)
            } else {
                hapusHalamanDariFavorit()
                Toast.makeText(this, "Halaman dihapus dari favorit", Toast.LENGTH_SHORT).show()
            }
        }

        val isHalamanFavorit = isHalamanDalamFavorit()
        toggleButton.isChecked = isHalamanFavorit
        if (isHalamanFavorit) {
            toggleButton.setBackgroundResource(R.drawable.bookmar_true)
        } else {
            toggleButton.setBackgroundResource(R.drawable.bookmark_false)
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
    private fun simpanHalamanKeFavorit(isHalamanDisimpanSebelumnya: Boolean) {
        val gson = Gson()
        val jsonString = gson.toJson(detailData)
        val sharedPreferences = getSharedPreferences("FavoritePages", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("halaman_${detailData.id}", jsonString)
        editor.apply()
        toggleButton.setBackgroundResource(R.drawable.bookmar_true)
        if (!isHalamanDisimpanSebelumnya) {
            Toast.makeText(this, "Halaman ditambahkan ke favorit", Toast.LENGTH_SHORT).show()
        }
    }
    private fun hapusHalamanDariFavorit() {
        val sharedPreferences = getSharedPreferences("FavoritePages", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("halaman_${detailData.id}")
        editor.apply()

        toggleButton.setBackgroundResource(R.drawable.bookmark_false)
    }
    private fun isHalamanDalamFavorit(): Boolean {
        val sharedPreferences = getSharedPreferences("FavoritePages", Context.MODE_PRIVATE)
        return sharedPreferences.contains("halaman_${detailData.id}")
    }


}

