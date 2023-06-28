package com.bappeda.publication_app.ui.home.ui.ui.search

import KecamatanAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bappeda.publication_app.R
import com.bappeda.publication_app.databinding.FragmentDashboardBinding
import com.bappeda.publication_app.response.DetailKecamatanResponse
import com.bappeda.publication_app.response.KecamatanResponse
import com.bappeda.publication_app.service.ApiConfig
import com.bappeda.publication_app.service.ApiService
import com.bappeda.publication_app.ui.home.ui.ui.pembangunan.ListActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class SearchFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private var isIntentLaunched = false

    private lateinit var adapter: KecamatanAdapter
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        getKecamatanData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isIntentLaunched = false
    }

    private fun setupRecyclerView() {
        adapter = KecamatanAdapter(emptyList()) { idKecamatan ->
            getDetailKecamatan(idKecamatan)
        }

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
    }

    private fun getKecamatanData() {
        apiService = ApiConfig.getApiService()
        val call: Call<KecamatanResponse> = apiService.getKecamatan()
        call.enqueue(object : Callback<KecamatanResponse> {
            override fun onResponse(
                call: Call<KecamatanResponse>,
                response: Response<KecamatanResponse>
            ) {
                if (response.isSuccessful) {
                    val kecamatanResponse = response.body()
                    val data = kecamatanResponse?.data

                    if (isAdded) {
                        if (data.isNullOrEmpty()) {
                            requireActivity().runOnUiThread {
                                showNoDataView()
                            }
                        } else {
                            adapter.setData(data)
                            showRecyclerView()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<KecamatanResponse>, t: Throwable) {
                if (isAdded) {
                    requireActivity().runOnUiThread {
                        showNoDataView()
                    }
                }
            }
        })
    }

    private fun getDetailKecamatan(idKecamatan: String) {
        if (!isIntentLaunched) {
            isIntentLaunched = true
            apiService = ApiConfig.getApiService()
            val call: Call<DetailKecamatanResponse> = apiService.getListPembangunan(idKecamatan)
            call.enqueue(object : Callback<DetailKecamatanResponse> {
                override fun onResponse(
                    call: Call<DetailKecamatanResponse>,
                    response: Response<DetailKecamatanResponse>
                ) {
                    if (response.isSuccessful) {
                        val detailKecamatanResponse = response.body()

                        if (detailKecamatanResponse != null && detailKecamatanResponse.data != null && detailKecamatanResponse.data.isNotEmpty()) {
                            val detailKecamatanList = detailKecamatanResponse.data
                            val intent = Intent(requireContext(), ListActivity::class.java)
                            intent.putParcelableArrayListExtra("detail_kecamatan", ArrayList(detailKecamatanList))
                            startActivity(intent)

                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Koneksi server terputus",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Data Pembangunan tidak ditemukan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    isIntentLaunched = false
                }

                override fun onFailure(call: Call<DetailKecamatanResponse>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "Koneksi server terputus",
                        Toast.LENGTH_SHORT
                    ).show()
                    isIntentLaunched = false
                }
            })
        }
    }

    private fun showNoDataView() {
        if (binding != null) {
            // ...
            binding.noDataLayout.visibility = View.VISIBLE
            binding.retryButton.visibility = View.VISIBLE
            binding.loadingProgressBar.visibility = View.GONE
            binding.error.visibility = View.VISIBLE

            val retryButton: Button = binding.root.findViewById(R.id.retryButton)
            retryButton.setOnClickListener {
                binding.retryButton.visibility = View.GONE
                binding.recyclerView.visibility = View.GONE
                binding.searchView.visibility = View.GONE
                binding.noDataLayout.visibility = View.VISIBLE
                binding.loadingProgressBar.visibility = View.VISIBLE
                getKecamatanData()
            }
        }
    }

    private fun showRecyclerView() {
        binding.recyclerView.visibility = View.VISIBLE
        binding.searchView.visibility = View.VISIBLE
        binding.noDataLayout.visibility = View.GONE
    }
}