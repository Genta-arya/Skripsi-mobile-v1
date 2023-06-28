package com.bappeda.publication_app.ui.home.ui.ui.bookmark

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bappeda.publication_app.R
import com.bappeda.publication_app.adapter.BookmarkAdapter
import com.bappeda.publication_app.databinding.FragmentNotificationsBinding
import com.bappeda.publication_app.response.Detail
import com.google.gson.Gson



class BookmarkFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private lateinit var bookmarkList: MutableList<Detail>
    private lateinit var bookmarkAdapter: BookmarkAdapter
    private lateinit var toolbar: Toolbar
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val sharedPreferences =
            requireContext().getSharedPreferences("FavoritePages", Context.MODE_PRIVATE)
        bookmarkList = mutableListOf()
        val keys = sharedPreferences.all.keys
        val gson = Gson()
        for (key in keys) {
            val jsonString = sharedPreferences.getString(key, null)
            val detail = gson.fromJson(jsonString, Detail::class.java)
            detail?.let { bookmarkList.add(it) }
        }
        bookmarkAdapter = BookmarkAdapter(bookmarkList)
        binding.recyclerViewBookmark.adapter = bookmarkAdapter
        binding.recyclerViewBookmark.layoutManager = LinearLayoutManager(requireContext())
        bookmarkAdapter.setOnItemClickListener(object : BookmarkAdapter.OnItemClickListener {
            override fun onItemClick(detail: Detail) {
                val intent = Intent(requireContext(), DetailBookmarkActivity::class.java)
                intent.putExtra("detail_kecamatan", detail)
                startActivity(intent)
            }
        })
        toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
        setHasOptionsMenu(true)

        checkBookmarkList()

        // Inisialisasi ItemTouchHelper untuk meng-handle swipe ke kanan pada RecyclerView
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Ambil posisi item yang di-swipe
                val position = viewHolder.adapterPosition
                deleteBookmark(position)
            }
        }

        // Hubungkan ItemTouchHelper dengan RecyclerView
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewBookmark)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_delete, menu)
        val deleteMenuItem = menu.findItem(R.id.action_delete)
        deleteMenuItem.isVisible = bookmarkList.isNotEmpty()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                showConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Konfirmasi")
            .setMessage("Apakah Anda yakin ingin menghapus semua Favorite?")
            .setPositiveButton("Ya") { _, _ ->
                deleteAllBookmarks()
            }
            .setNegativeButton("Tidak", null)
            .show()
    }

    private fun showConfirmationDialog(position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Konfirmasi")
            .setMessage("Apakah Anda yakin ingin menghapus bookmark ini?")
            .setPositiveButton("Ya") { _, _ ->
                deleteBookmark(position)
            }
            .setNegativeButton("Tidak", null)
            .show()
    }

    private fun deleteAllBookmarks() {
        val sharedPreferences =
            requireContext().getSharedPreferences("FavoritePages", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        bookmarkList.clear()
        bookmarkAdapter.notifyDataSetChanged()
        checkBookmarkList()
    }

    private fun deleteBookmark(position: Int) {
        val bookmark = bookmarkList[position]
        val key = bookmark.id.toString()

        // Hapus bookmark dari SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("FavoritePages", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()

        // Hapus bookmark dari daftar
        bookmarkList.removeAt(position)
        bookmarkAdapter.notifyItemRemoved(position)

        // Periksa daftar bookmark setelah penghapusan
        checkBookmarkList()

        // Perbarui SharedPreferences dengan daftar bookmark yang sudah diperbarui
        updateSharedPreferences()
    }

    private fun updateSharedPreferences() {
        val sharedPreferences = requireContext().getSharedPreferences("FavoritePages", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Hapus semua data bookmark dari SharedPreferences
        editor.clear()

        // Simpan ulang daftar bookmark yang sudah diperbarui ke SharedPreferences
        val gson = Gson()
        for (bookmark in bookmarkList) {
            val jsonString = gson.toJson(bookmark)
            editor.putString(bookmark.id.toString(), jsonString)
        }

        editor.apply()
    }

    private fun checkBookmarkList() {
        if (bookmarkList.isEmpty()) {
            binding.error.visibility = View.VISIBLE
        } else {
            binding.error.visibility = View.GONE
        }
    }
}