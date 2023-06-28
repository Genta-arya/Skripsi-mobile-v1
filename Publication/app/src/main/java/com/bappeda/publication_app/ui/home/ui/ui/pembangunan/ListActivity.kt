package com.bappeda.publication_app.ui.home.ui.ui.pembangunan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bappeda.publication_app.R
import com.bappeda.publication_app.adapter.PembangunanAdapter
import com.bappeda.publication_app.response.Detail
import com.bappeda.publication_app.response.DetailKecamatan
import com.bappeda.publication_app.response.DetailKecamatanResponse
import com.bappeda.publication_app.service.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class ListActivity : AppCompatActivity(), PembangunanAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PembangunanAdapter
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        recyclerView = findViewById(R.id.recyclerView_pembangunan)
        recyclerView.layoutManager = LinearLayoutManager(this)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val detailKecamatanList =
            intent.getParcelableArrayListExtra<DetailKecamatan>("detail_kecamatan")

        adapter = PembangunanAdapter(detailKecamatanList!!)
        adapter.setOnItemClickListener(this)
        recyclerView.adapter = adapter
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

    override fun onItemClick(detailKecamatan: DetailKecamatan) {
        showDetailPage(detailKecamatan.id.toString())
    }

    private fun showDetailPage(id: String) {
        val apiService = ApiConfig.getApiService()
        val call: Call<Detail> = apiService.getDetailPembangunan(id)
        call.enqueue(object : Callback<Detail> {
            override fun onResponse(call: Call<Detail>, response: Response<Detail>) {
                if (response.isSuccessful) {
                    val detailData = response.body()
                    if (detailData != null) {
                        val detail = detailData
                        val intent = Intent(this@ListActivity, DetailActivity::class.java)
                        intent.putExtra("detail_kecamatan", detail)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@ListActivity,
                            "Koneksi server terputus",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@ListActivity,
                        "Koneksi server terputus",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Detail>, t: Throwable) {
                Toast.makeText(
                    this@ListActivity,
                    "Koneksi server terputus",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }




}

