package com.bappeda.publication_app.ui.home.ui.ui.home
import android.os.Handler
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bappeda.publication_app.R
import com.bappeda.publication_app.adapter.ImageSliderAdapter
import com.bappeda.publication_app.adapter.NotifikasiAdapter
import com.bappeda.publication_app.databinding.FragmentHomeBinding
import com.bappeda.publication_app.local.Preference
import com.bappeda.publication_app.model.PrefViewModel
import com.bappeda.publication_app.model.ViewModelFactory
import com.bappeda.publication_app.response.Detail
import com.bappeda.publication_app.response.Notifikasi
import com.bappeda.publication_app.response.NotifikasiResponse
import com.bappeda.publication_app.service.ApiConfig
import com.bappeda.publication_app.ui.home.ui.ui.pembangunan.DetailActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme")
    private lateinit var pref: PrefViewModel
    private lateinit var toolbar: Toolbar
    private lateinit var viewPager: ViewPager2
    private lateinit var dotsIndicator: WormDotsIndicator
    private var isNotifAvailable = false
    private var isFragmentAttached: Boolean = false

    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        isFragmentAttached = true
    }

    override fun onDetach() {
        super.onDetach()
        isFragmentAttached = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.view_pager)
        dotsIndicator = view.findViewById(R.id.dots_indicator)

        val images = listOf(

            R.drawable.rsz_1img_20220825_124117,
            R.drawable.bappeda,
            R.drawable.bupati,
            R.drawable.ktp,
            R.drawable.pawan5,
            R.drawable.ale,
        )

        val adapter = ImageSliderAdapter(images)
        viewPager.adapter = adapter
        dotsIndicator.setViewPager2(viewPager)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
        }
        setHasOptionsMenu(true)
        val handler = Handler()
        val autoSliderRunnable = object : Runnable {
            override fun run() {
                val currentItem = viewPager.currentItem
                val adapter = viewPager.adapter
                if (currentItem < (adapter?.itemCount ?: 0) - 1) {
                    viewPager.setCurrentItem(currentItem + 1, true)
                } else {
                    viewPager.setCurrentItem(0, true)
                }
                handler.postDelayed(this, 5000)
            }
        }
        handler.postDelayed(autoSliderRunnable, 5000)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.notif -> {
                getNotif()
                true
            }
            R.id.theme_dark -> {
                pref.saveThemes(true)
                Toast.makeText(requireContext(), "Dark Mode telah Aktif", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.theme_light -> {
                pref.saveThemes(false)
                Toast.makeText(requireContext(), "Light Mode telah Aktif", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val prefs = Preference.getInstance(requireContext().dataStore)
        pref = ViewModelProvider(this, ViewModelFactory(prefs)).get(PrefViewModel::class.java)

        pref.getTheme().observe(viewLifecycleOwner) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getNotif() {
        if (!isFragmentAttached) {
            return
        }

        val apiService = ApiConfig.getApiService()
        val call: Call<NotifikasiResponse> = apiService.getNotifikasi()
        call.enqueue(object : Callback<NotifikasiResponse> {
            override fun onResponse(call: Call<NotifikasiResponse>, response: Response<NotifikasiResponse>) {
                if (response.isSuccessful) {
                    val detailData = response.body()
                    if (detailData != null) {
                        val notifdata = detailData.data

                        // Menyimpan notifikasi ke preferensi lokal
                        saveNotifikasiToPreferences(notifdata)

                        showNotifikasiDialog(requireContext(), notifdata)

                        isNotifAvailable = notifdata.isNotEmpty()

                        val menu = binding.toolbar.menu
                        val notifItem = menu.findItem(R.id.notif)

                        if (notifItem != null) {
                            // Mengatur tampilan ikon notifikasi
                            if (isNotifAvailable) {
                                // Jika ada data notifikasi, set ikon berwarna merah
                                notifItem.setIcon(R.drawable.ic_notifications_black_24dp)
                            } else {
                                // Jika tidak ada data notifikasi, set ikon berwarna putih
                                notifItem.setIcon(R.drawable.ic_notifications_black_24dp)
                            }
                        }
                    } else {
                        // Tangani jika detailData null
                    }
                } else {
                    // Tangani jika response tidak berhasil
                }
            }

            override fun onFailure(call: Call<NotifikasiResponse>, t: Throwable) {
                // Gagal menghubungi server
                Log.e("Notifikasi", "Failed to retrieve data: ${t.message}")
            }
        })
    }

    private fun saveNotifikasiToPreferences(notifdata: List<Notifikasi>) {
        val sharedPreferences = requireContext().getSharedPreferences("NotifikasiPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Simpan notifikasi ke preferensi lokal
        val gson = Gson()
        val json = gson.toJson(notifdata)
        editor.putString("notifdata", json)
        editor.apply()
    }

    private fun loadNotifikasiFromPreferences(): List<Notifikasi> {
        val sharedPreferences = requireContext().getSharedPreferences("NotifikasiPrefs", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("notifdata", null)
        val gson = Gson()
        val type = object : TypeToken<List<Notifikasi>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }


    private fun showNotifikasiDialog(context: Context, notifdata: List<Notifikasi>) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_notifikasi, null)
        val recyclerView: RecyclerView = dialogView.findViewById(R.id.notifikasiRecyclerView)
        val emptyTextView: TextView = dialogView.findViewById(R.id.emptyTextView)

        val adapter = NotifikasiAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter.setNotifikasiList(notifdata)

        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setNegativeButton("Tutup") { _, _ ->
                deleteNotifikasi()
            }

        val dialog = dialogBuilder.create()

        // Tambahkan properti dialog
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(true)
        dialog.window?.setGravity(Gravity.CENTER)

        // Periksa apakah daftar notifikasi kosong
        if (notifdata.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyTextView.visibility = View.VISIBLE
            emptyTextView.text = "Tidak ada notifikasi"
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyTextView.visibility = View.GONE
        }

        dialog.show()
    }





    //    private fun deleteNotifikasi() {
//        val apiService = ApiConfig.getApiService()
//        val call: Call<NotifikasiResponse> = apiService.deleteNotifikasi()
//        call.enqueue(object : Callback<NotifikasiResponse> {
//            override fun onResponse(
//                call: Call<NotifikasiResponse>,
//                response: Response<NotifikasiResponse>
//            ) {
//                if (response.isSuccessful) {
//                    // Notifikasi berhasil dihapus
//                    Log.d("Notifikasi", "Notifikasi berhasil dihapus")
//                } else {
//                    // Gagal menghapus notifikasi
//                    Log.e("Notifikasi", "Gagal menghapus notifikasi")
//                }
//            }
//
//            override fun onFailure(call: Call<NotifikasiResponse>, t: Throwable) {
//                // Gagal menghubungi server
//                Log.e("Notifikasi", "Failed to delete notifikasi: ${t.message}")
//            }
//        })
//    }
private fun deleteNotifikasi() {
    // Menghapus notifikasi dari preferensi lokal
    deleteNotifikasiFromPreferences()

    // Tampilkan pesan berhasil dihapus (opsional)
    Log.d("Notifikasi", "Notifikasi berhasil dihapus")
}
    private fun deleteNotifikasiFromPreferences() {
        val sharedPreferences = requireContext().getSharedPreferences("NotifikasiPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Hapus notifikasi dari preferensi lokal
        editor.remove("notifdata")
        editor.apply()
    }

}

